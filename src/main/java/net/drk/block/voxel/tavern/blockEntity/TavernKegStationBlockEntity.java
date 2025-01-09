package net.drk.block.voxel.tavern.blockEntity;

import net.drk.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.drk.block.entities.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TavernKegStationBlockEntity extends BlockEntity {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private UUID playerUUID = null; // Store the UUID of the player who inserted the item
    private int processTime = 0; // Processing time for the current item
    private int maxProcessTime = 0; // Maximum time required for the current item
    private float rotation = 0;

    public TavernKegStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TAVERN_KEG_STATION, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("ProcessTime", processTime);
        nbt.putInt("MaxProcessTime", maxProcessTime);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        processTime = nbt.getInt("ProcessTime");
        maxProcessTime = nbt.getInt("MaxProcessTime");
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    // Insert an item into the keg
    public boolean insertItem(ItemStack stack, PlayerEntity player) {
        if (!hasItem()) {
            inventory.set(0, stack.split(1)); // Insert one item and remove it from the stack
            setProcessingTime(stack); // Set the processing time based on the item
            this.playerUUID = player.getUuid(); // Store the player's UUID who inserted the item
            markDirty();
            return true;
        } else {
            System.out.println("Tried inserting item, but slot is already filled.");
        }
        return false;
    }

    // Check if the keg has an item
    public boolean hasItem() {
        return !inventory.get(0).isEmpty();
    }


    // Extract the finished item from the keg
    public ItemStack extractItem(PlayerEntity player) {
        if (hasItem() && isProcessingComplete() && player.getUuid().equals(this.playerUUID)) {
            ItemStack finishedItem = getProcessedItem(inventory.get(0)); // Get the processed item
            inventory.set(0, ItemStack.EMPTY); // Remove the item
            resetProcessingTime();
            markDirty();

            if (world != null && !world.isClient) {
                world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
                this.sync(); // Explicitly send a block entity update
            }

            // Play sound for the player
            world.playSound(null, getPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, player.getSoundCategory(), 1.0F, 1.0F); // Example: level up sound

            // Send a message to the player
            return finishedItem;
        }
        return ItemStack.EMPTY; // No finished item to extract or player is not the one who inserted the item
    }

    public void sync() {
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(getPos());
        }
    }


    // Get the item currently in the keg
    public ItemStack getCurrentItem() {
        return inventory.get(0);
    }
    public ItemStack getProcessedItem() {
        return getProcessedItem(inventory.get(0));
    }

    // Set processing time based on the item type
    private void setProcessingTime(ItemStack stack) {
        if (stack.getItem() == Items.WHEAT) {
            maxProcessTime = 2000; // 10 seconds
        } else if (stack.getItem() == Items.POTATO) {
            maxProcessTime = 300; // 15 seconds
        } else if (stack.getItem() == ModItems.ECLIPSE_SPEAR) {
            maxProcessTime = 400; // 20 seconds
        } else {
            maxProcessTime = 100; // Default
        }
        processTime = 0; // Reset current processing time
    }

    // Reset processing time when the item is removed
    private void resetProcessingTime() {
        processTime = 0;
        maxProcessTime = 0;
    }

    // Check if processing is complete
    public boolean isProcessingComplete() {
        return processTime >= maxProcessTime;
    }
    public boolean isDone() {
        return maxProcessTime >= 1;
    }

    // Tick method to handle processing
    public static void tick(World world, BlockPos pos, BlockState state, TavernKegStationBlockEntity keg) {
        if (keg.hasItem()) {
            if (keg.processTime < keg.maxProcessTime) {
                keg.processTime++;
            }
            if (keg.isProcessingComplete() && keg.isDone()) {
                keg.resetProcessingTime();
                System.out.println("Processing complete!");  // Debugging line

                // Check if the UUID is null before using it
                if (keg.playerUUID != null) {
                    PlayerEntity player = world.getPlayerByUuid(keg.playerUUID);
                    if (player != null) {
                        world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, player.getSoundCategory(), 1.0F, 1.0F); // Example sound
                        player.sendMessage(Text.literal("Your item is ready: " + keg.getProcessedItem(keg.inventory.get(0)).getName().getString()), false);
                    } else {
                        System.out.println("Player with UUID " + keg.playerUUID + " not found!");  // Debugging line
                    }
                } else {
                    System.out.println("Player UUID is null!");  // Debugging line
                }
            }
            // Ensure it's marked dirty after updates
            keg.markDirty();
            // Also request block update if necessary
            world.updateListeners(keg.getPos(), state, state, 3);
        } else {
            System.out.println("No item in keg!");  // Debugging line
        }
    }



    // Get max processing time for the current item
    public int getMaxProcessingTime() {
        return maxProcessTime;
    }

    public int getRemainingProcessingTime() {
        return maxProcessTime - processTime;
    }
    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    // Get the processed item based on the current item in the keg
    public ItemStack getProcessedItem(ItemStack stack) {
        if (stack.getItem() == Items.WHEAT) {
            return new ItemStack(Items.BREAD); // Wheat -> Bread
        } else if (stack.getItem() == Items.POTATO) {
            return new ItemStack(Items.BAKED_POTATO); // Potato -> Baked Potato
        } else if (stack.getItem() == ModItems.ECLIPSE_SPEAR) {
            return new ItemStack(ModItems.GRAVEBANE); // Custom item processing
        }
        return ItemStack.EMPTY; // Default case: no processing for other items
    }
}
