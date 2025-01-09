package net.drk.block.voxel.logs.block;

import net.drk.block.voxel.logs.blockEntity.AcaciaLogBlockEntity;
import net.drk.block.entities.ModBlockEntities;
import net.drk.util.WeaponUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AcaciaLog extends Block implements BlockEntityProvider {
    private static final Random RANDOM = new Random();
    private static final Map<UUID, Map<String, Long>> PLAYER_COOLDOWNS = new HashMap<>();

    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 15); // 16 directions (0-15)

    // Constructor
    public AcaciaLog(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ROTATION, 0)); // Default rotation 0
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null) {
            // Get yaw and map it to 16 directions
            float yaw = player.getYaw() % 360;
            if (yaw < 0) yaw += 360; // Ensure yaw is positive
            int rotation = Math.round(yaw / 22.5f) % 16; // 360° / 16 = 22.5° per increment
            return this.getDefaultState().with(ROTATION, rotation);
        }
        return this.getDefaultState();
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION); // Add rotation property to block state
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // Ensure server-side processing only
        if (!world.isClient) {
            // Get the block entity to track health
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof AcaciaLogBlockEntity acaciaLogBlockEntity) {
                ItemStack heldItem = player.getMainHandStack();
                String weaponKey = heldItem.getTranslationKey();

                if (isOnCooldown(player, weaponKey)) {
                    return ActionResult.FAIL;
                } else {
                    // Use WeaponUtils to get the damage value
                    int damage = WeaponUtils.getDamageForLogWeapon(heldItem.getTranslationKey());
                    acaciaLogBlockEntity.damageBlock(damage);
                }

                if (acaciaLogBlockEntity.isDestroyed()) {
                    dropLoot((ServerWorld) world, pos);
                    world.breakBlock(pos, false);
                }

                applyCooldown(player, weaponKey);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }


    private boolean isOnCooldown(PlayerEntity player, String weaponKey) {
        if (WeaponUtils.getCooldownForLogWeapon(weaponKey) == 0) {
            return false; // No cooldown for unrecognized weapons
        }

        UUID playerId = player.getUuid();
        PLAYER_COOLDOWNS.putIfAbsent(playerId, new HashMap<>());

        long currentTime = System.currentTimeMillis();
        long lastUseTime = PLAYER_COOLDOWNS.get(playerId).getOrDefault(weaponKey, 0L);

        return (currentTime - lastUseTime) < WeaponUtils.getCooldownForLogWeapon(weaponKey);
    }
    private void applyCooldown(PlayerEntity player, String weaponKey) {
        UUID playerId = player.getUuid();
        PLAYER_COOLDOWNS.putIfAbsent(playerId, new HashMap<>());
        PLAYER_COOLDOWNS.get(playerId).put(weaponKey, System.currentTimeMillis());

        // Visual cooldown for the item in the player's hand
        ItemStack heldItem = player.getMainHandStack();
        if (!heldItem.isEmpty() && heldItem.getTranslationKey().equals(weaponKey)) {
            int cooldownTicks = WeaponUtils.getCooldownForLogWeapon(weaponKey) / 50; // Convert milliseconds to ticks
            player.getItemCooldownManager().set(heldItem.getItem(), cooldownTicks);
        }
    }


    public static class LootItem {
        private final ItemStack item;
        private final int priority;  // The priority determines the chance of dropping

        public LootItem(ItemStack item, int priority) {
            this.item = item;
            this.priority = priority;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getPriority() {
            return priority;
        }
    }


    public void dropLoot(ServerWorld world, BlockPos pos) {
        // List of loot items with their priorities
        List<LootItem> lootItems = new ArrayList<>();
        lootItems.add(new LootItem(new ItemStack(Items.STICK), 1000));   // Priority 1 for Nature Sword
        lootItems.add(new LootItem(new ItemStack(Items.OAK_PLANKS), 500));   // Priority 1 for Nature Sword



        // Calculate the total priority sum


        int totalPriority = 0;
        for (LootItem lootItem : lootItems) {
            totalPriority += lootItem.getPriority();
        }

        // 50% chance for no loot
        int chance = RANDOM.nextInt(100); // Random roll between 0 and 99
        if (chance < 50) {
            return; // No loot drop
        }

        // Calculate how many items to drop (1, 2, or 3 items) based on the defined percentages
        int numItemsToDrop = 1; // Default to 1 item
        int dropChance = RANDOM.nextInt(100); // Random roll between 0 and 99

        // 25% chance for 2 items
        if (dropChance < 25) {
            numItemsToDrop = 2;
        }
        // 10% chance for 3 items
        else if (dropChance < 35) {
            numItemsToDrop = 3;
        }

        // Drop the selected number of items based on priority
        List<ItemStack> droppedItems = new ArrayList<>();
        for (int i = 0; i < numItemsToDrop; i++) {
            int lootRoll = RANDOM.nextInt(totalPriority);  // Random roll based on total priority
            int accumulatedPriority = 0;

            // Select the loot item based on priority
            for (LootItem lootItem : lootItems) {
                accumulatedPriority += lootItem.getPriority();
                if (lootRoll < accumulatedPriority) {
                    // Item is selected, add to droppedItems list
                    droppedItems.add(lootItem.getItem());
                    break;
                }
            }
        }

        // Drop all selected items
        for (ItemStack droppedItem : droppedItems) {
            Block.dropStack(world, pos, droppedItem);
        }
    }


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AcaciaLogBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type == ModBlockEntities.ACACIA_LOG) {
            return (world1, pos, state1, entity) -> {
                if (entity instanceof AcaciaLogBlockEntity acaciaLogBlockEntity) {
                    AcaciaLogBlockEntity.tick(world1, pos, state1, acaciaLogBlockEntity);
                }
            };
        }
        return null;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.2f, 0.001f, -0.2f, 0.8f, 0.5f, 1.2f);
    }
    // Inner class to store loot item and its chance
}
