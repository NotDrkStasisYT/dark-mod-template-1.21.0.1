package net.drk.command.shield;

import net.minecraft.server.network.ServerPlayerEntity;
import net.drk.command.shield.styles.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerShieldStyleManager {
    private static final Map<ServerPlayerEntity, ShieldStyle> playerStyles = new HashMap<>();

    // Default to the default style
    public static ShieldStyle getStyle(ServerPlayerEntity player) {
        return playerStyles.getOrDefault(player, new DefaultStyle());
    }

    public static void setStyle(ServerPlayerEntity player, ShieldStyle style) {
        playerStyles.put(player, style);
    }
}
