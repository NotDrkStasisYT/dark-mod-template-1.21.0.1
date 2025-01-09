package net.drk.item.other.weapons;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;
import net.drk.PowerManager;
import net.drk.item.ModItems;
import net.drk.item.ModToolMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gravebane extends SwordItem {

    private static final Map<PlayerEntity, Long> cooldownMap = new HashMap<>();
    private static final long COOLDOWN_TIME = 10000; // 10 seconds in milliseconds
    private static final double RADIUS = 5.0;
    private static final PowerManager powerManager = new PowerManager(); // Instance of PowerManager

    public Gravebane(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }


    // Register the item callback with cooldown and power-based damage
    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient) {
                return TypedActionResult.pass(player.getStackInHand(hand)); // Skip client-side
            }

            ItemStack itemStack = player.getStackInHand(hand);

            if (itemStack.getItem() == ModItems.BLOODFIRE_SPEAR) {
                long currentTime = System.currentTimeMillis();
                Long lastUsedTime = cooldownMap.get(player);

                // Check if cooldown is active
                if (lastUsedTime != null && currentTime - lastUsedTime < COOLDOWN_TIME) {
                    long timeLeft = (COOLDOWN_TIME - (currentTime - lastUsedTime)) / 1000;
                    ((ServerPlayerEntity) player).sendMessage(Text.literal("You need to wait " + timeLeft + " seconds before using the BLOODFIRE_SPEAR again."), false);
                    return TypedActionResult.pass(itemStack); // Cooldown not expired
                }

                // Send the chat message
                ((ServerPlayerEntity) player).sendMessage(Text.literal("You have used the BLOODFIRE_SPEAR!"), false);

                // Get entities in a 5-block radius
                BlockPos playerPos = player.getBlockPos();
                Box box = new Box(playerPos).expand(RADIUS); // 5 block radius
                List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, entity -> !entity.equals(player));

                // Apply damage based on player's power
                String playerName = player.getName().getString();
                for (LivingEntity entity : entities) {
                    powerManager.applyDamageBasedOnPowerGravebane(entity, playerName);
                }

                // Spawn fire particles
                spawnFireParticleCircle((ServerWorld) world, player);

                // Update cooldown timestamp
                cooldownMap.put(player, currentTime);
                return TypedActionResult.success(itemStack); // Indicate success
            }

            return TypedActionResult.pass(itemStack); // Pass along if not the correct item
        });
    }

    // Method to spawn fire particles around the player
    private static void spawnFireParticleCircle(ServerWorld world, PlayerEntity player) {
        double initialRadius = 1.0; // Start close to the player
        double maxRadius = 5.0; // Maximum radius for particle expansion
        int particleCount = 30;
        int steps = 10;
        int delayTicks = 2; // Delay between each step

        for (int step = 0; step <= steps; step++) {
            double currentRadius = initialRadius + ((maxRadius - initialRadius) * step / steps);
            world.getServer().getOverworld().getServer().execute(() -> {
                Vec3d playerPos = player.getPos();
                for (int i = 0; i < particleCount; i++) {
                    double angle = 2 * Math.PI * i / particleCount;
                    double xOffset = currentRadius * Math.cos(angle);
                    double zOffset = currentRadius * Math.sin(angle);

                    world.spawnParticles(ParticleTypes.FLAME, playerPos.x + xOffset, playerPos.y, playerPos.z + zOffset, 1, 0, 0, 0, 0);
                }
            });
        }
    }

}