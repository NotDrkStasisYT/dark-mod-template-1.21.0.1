package net.drk.Z28;

import net.drk.Z28.EffectManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DynamicNicknameUpdater {

    private static final Map<ServerPlayerEntity, AtomicInteger> playerTickCounter = new HashMap<>();
    private static final int UPDATE_INTERVAL = 10; // Update every 10 ticks (0.5 seconds)

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                AtomicInteger ticks = playerTickCounter.computeIfAbsent(player, k -> new AtomicInteger(0));

                if (ticks.get() >= UPDATE_INTERVAL) {
                    // Update the player's name above their head
                    updatePlayerNameAboveHead(player);
                    ticks.set(0); // Reset tick counter
                } else {
                    ticks.incrementAndGet();
                }
            }
        });
    }

    // Function to update the player's name above their head
    private static void updatePlayerNameAboveHead(ServerPlayerEntity player) {
        // Get the styled nickname from EffectManager
        String newStyledNickname = EffectManager.getStyledNickname(player);

        // Set the player's custom name (this is the name that appears above their head)
        player.setCustomName(Text.literal(newStyledNickname));

        // Make sure the custom name is always visible
        player.setCustomNameVisible(true); // Ensure the custom name is visible to others
    }
}
