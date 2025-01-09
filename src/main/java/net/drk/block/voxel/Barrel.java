package net.drk.block.voxel;

import net.drk.block.advanced.BarrelBlockEntity;
import net.drk.block.entities.ModBlockEntities;
import net.drk.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Barrel extends Block implements BlockEntityProvider {
    private static final Random RANDOM = new Random();
    private static final Map<String, Integer> WEAPON_COOLDOWNS = new HashMap<>();
    private static final Map<UUID, Map<String, Long>> PLAYER_COOLDOWNS = new HashMap<>();

    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 15); // 16 directions (0-15)
    static {
        // Set cooldowns in milliseconds for different weapons
        WEAPON_COOLDOWNS.put("item.minecraft.netherite_sword", 1000); // 1 second
        WEAPON_COOLDOWNS.put("diamond_sword", 1200);  // 1.2 seconds
        WEAPON_COOLDOWNS.put("iron_sword", 1500);    // 1.5 seconds
        WEAPON_COOLDOWNS.put("stone_sword", 2000);   // 2 seconds
        WEAPON_COOLDOWNS.put("item.minecraft.wooden_sword", 2500);  // 2.5 seconds
        WEAPON_COOLDOWNS.put("item.drkmod.eclipse_spear", 800);  // 0.8 seconds
        WEAPON_COOLDOWNS.put("fire_sword", 500);     // 0.5 seconds
        WEAPON_COOLDOWNS.put("trident", 1000);       // 1 second
    }
    // Constructor
    public Barrel(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ROTATION, 0)); // Default rotation 0
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
            if (entity instanceof net.drk.block.advanced.BarrelBlockEntity barrelBlockEntity) {
                ItemStack heldItem = player.getMainHandStack();
                String weaponKey = heldItem.getTranslationKey();
                if (isOnCooldown(player, weaponKey)) {
                    player.sendMessage(Text.literal("This weapon is on cooldown!"), true);
                    return ActionResult.FAIL;
                } else {
                    int damage = getDamageForWeapon(heldItem); // Determine weapon damage
                    barrelBlockEntity.damageBlock(damage);
                }
                if (barrelBlockEntity.isDestroyed()) {
                    dropLoot((ServerWorld) world, pos); // Drop loot when barrel is destroyed
                    world.breakBlock(pos, false); // Break the barrel block
                }

                applyCooldown(player, weaponKey);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }


    private boolean isOnCooldown(PlayerEntity player, String weaponKey) {
        if (!WEAPON_COOLDOWNS.containsKey(weaponKey)) {
            return false; // No cooldown for unrecognized weapons
        }

        UUID playerId = player.getUuid();
        PLAYER_COOLDOWNS.putIfAbsent(playerId, new HashMap<>());

        long currentTime = System.currentTimeMillis();
        long lastUseTime = PLAYER_COOLDOWNS.get(playerId).getOrDefault(weaponKey, 0L);

        return (currentTime - lastUseTime) < WEAPON_COOLDOWNS.get(weaponKey);
    }
    private void applyCooldown(PlayerEntity player, String weaponKey) {
        UUID playerId = player.getUuid();
        PLAYER_COOLDOWNS.putIfAbsent(playerId, new HashMap<>());
        PLAYER_COOLDOWNS.get(playerId).put(weaponKey, System.currentTimeMillis());

        // Visual cooldown for the item in the player's hand
        ItemStack heldItem = player.getMainHandStack();
        if (!heldItem.isEmpty() && heldItem.getTranslationKey().equals(weaponKey)) {
            int cooldownTicks = WEAPON_COOLDOWNS.get(weaponKey) / 50; // Convert milliseconds to ticks (20 ticks = 1 second)
            player.getItemCooldownManager().set(heldItem.getItem(), cooldownTicks);
        }
    }


    private int getDamageForWeapon(ItemStack itemStack) {
        // Adjust damage values based on weapon type
        String itemKey = itemStack.getTranslationKey();
        if (itemKey.contains("netherite_sword")) return 5;
        if (itemKey.contains("diamond_sword")) return 4;
        if (itemKey.contains("iron_sword")) return 3;
        if (itemKey.contains("stone_sword")) return 2;
        if (itemKey.contains("wooden_sword")) return 1;
        if (itemKey.contains("eclipse_spear")) return 9;
        if (itemKey.contains("fire_sword")) return 18;
        if (itemKey.contains("trident")) return 12;



        return 0; // No damage for non-sword items
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
        lootItems.add(new LootItem(new ItemStack(ModItems.NATURE_SWORD), 1000));   // Priority 1 for Nature Sword

        lootItems.add(new LootItem(new ItemStack(Items.COOKED_BEEF), 2250));       // Priority for Cooked Beef
        lootItems.add(new LootItem(new ItemStack(ModItems.GRAVEBANE), 2250));         // Priority for Gravebane
        lootItems.add(new LootItem(new ItemStack(Items.COOKED_CHICKEN), 2250));    // Priority for Cooked Chicken
        lootItems.add(new LootItem(new ItemStack(ModItems.FIRE_SWORD), 2250));        // Priority for Fire Sword

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
        return new BarrelBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type == ModBlockEntities.BARREL) {
            return (world1, pos, state1, entity) -> {
                if (entity instanceof BarrelBlockEntity barrelEntity) {
                    BarrelBlockEntity.tick(world1, pos, state1, barrelEntity);
                }
            };
        }
        return null;
    }

    // Inner class to store loot item and its chance
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0.001f, 0f, 1.0f, 1f, 1.0f);
    }
}
