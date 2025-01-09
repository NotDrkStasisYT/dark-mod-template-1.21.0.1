package net.drk.command.shield.max;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

public class MaxShieldColorManager {
    private static final Map<ServerPlayerEntity, Formatting> playerMaxShieldColors = new HashMap<>();

    // Default to GREEN
    public static Formatting getMaxShieldColor(ServerPlayerEntity player) {
        return playerMaxShieldColors.getOrDefault(player, Formatting.GREEN);
    }

    public static void setMaxShieldColor(ServerPlayerEntity player, Formatting color) {
        playerMaxShieldColors.put(player, color);
    }
}
