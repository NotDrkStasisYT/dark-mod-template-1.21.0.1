package net.drk.event;

import net.drk.command.FriendListManager;
import net.drk.player.ShieldManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HungerListener extends FriendListManager {

    private static final double DEFAULT_MOVEMENT_SPEED = 0.1; // Default player movement speed

    private static final ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService scheduler3 = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService scheduler4 = Executors.newScheduledThreadPool(1);

    public static void register() {
        // Schedule shield regeneration tasks with different intervals
        scheduleShieldRegeneration(scheduler1, 5, 15, 20);  // For players with hunger >= 15
        scheduleShieldRegeneration(scheduler2, 10, 10, 14); // For players with hunger between 10 and 14
        scheduleShieldRegeneration(scheduler3, 15, 5, 9);   // For players with hunger between 5 and 9
        scheduleShieldRegeneration(scheduler4, 20, 0, 4);   // For players with hunger < 5

        // Register player hunger logic
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                int hungerLevel = player.getHungerManager().getFoodLevel();
                applyMovementDebuff(player, hungerLevel);
            }
        });
    }

    // Schedule shield regeneration with a specific interval for players within a specific hunger range
    private static void scheduleShieldRegeneration(ScheduledExecutorService scheduler, long intervalInSeconds, int minHunger, int maxHunger) {
        scheduler.scheduleAtFixedRate(() -> {
            if (server != null) {
                server.execute(() -> {
                    for (ServerWorld world : server.getWorlds()) {
                        for (ServerPlayerEntity player : world.getPlayers()) {
                            int hungerLevel = player.getHungerManager().getFoodLevel();
                            if (hungerLevel >= minHunger && hungerLevel <= maxHunger) {
                                ShieldManager shieldManager = ShieldManager.getInstance();
                                BigInteger regenRate = shieldManager.getShieldRegenerationRate(player);
                                if (regenRate.compareTo(BigInteger.ZERO) > 0) {
                                    shieldManager.regenerateShield(player, regenRate);
                                }
                            }
                        }
                    }
                });
            }
        }, 0, intervalInSeconds, TimeUnit.SECONDS);
    }

    // Method to slow down the player based on their hunger level
    private static void applyMovementDebuff(ServerPlayerEntity player, int hungerLevel) {
        double speedModifier;

        // Reduce movement speed based on hunger level (scaled between 0.04 and 0.1)
        if (hungerLevel >= 15) {
            speedModifier = 1.0;  // Full speed when hunger is 15 or more
        } else if (hungerLevel >= 10) {
            speedModifier = 0.8;  // Slightly slower when hunger is between 10 and 14
        } else if (hungerLevel >= 5) {
            speedModifier = 0.6;  // More slowdown when hunger is between 5 and 9
        } else {
            speedModifier = 0.4;  // Severe slowdown when hunger is less than 5
        }

        // Apply the movement speed modifier based on the default speed (0.1)
        player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(DEFAULT_MOVEMENT_SPEED * speedModifier);
    }
}
