package net.drk.player.movement;

import net.drk.player.movement.mechanics.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MovementManager {
    private static KeyBinding airJumpKey;
    private static KeyBinding dashKey;
    private static KeyBinding reverseDashKey;
    private static KeyBinding temporalDashKey;
    private static KeyBinding momentumDisplaceKey;

    public static void initClient() {
        registerKeyBindings();
        registerEventListeners();
    }

    private static void registerKeyBindings() {
        airJumpKey = new KeyBinding(
                "key.drkmod.air_jump", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, "category.drkmod.movement"
        );
        dashKey = new KeyBinding(
                "key.drkmod.dash", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.drkmod.movement"
        );
        reverseDashKey = new KeyBinding(
                "key.drkmod.reverse_dash", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F, "category.drkmod.movement"
        );
        temporalDashKey = new KeyBinding(
                "key.drkmod.temporal_dash", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "category.drkmod.movement"
        );
        momentumDisplaceKey = new KeyBinding(
                "key.drkmod.momentum_displace", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_E, "category.drkmod.movement"
        );
    }

    private static void registerEventListeners() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            var player = client.player;

            if (player == null) return;

            while (airJumpKey.wasPressed()) {
                DirectionalAirJump.perform(player);
            }

            while (dashKey.wasPressed()) {
                AltitudeDash.perform(player);
            }

            while (reverseDashKey.wasPressed()) {
                ReverseDash.perform(player);
            }

            while (temporalDashKey.wasPressed()) {
                TemporalDashChain.perform(player);
            }

            while (momentumDisplaceKey.wasPressed()) {
                MomentumDisplacement.perform(player);
            }
        });
    }
}
