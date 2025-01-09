package net.drk.other.keybinds.mechanics;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class DashKeyBinding {
    private static KeyBinding dashKey;
    private static int dodgesRemaining = 2; // Number of dodges available

    public static void register() {
        // Register the keybinding (e.g., "G" key)
        dashKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.drkmod.dash", // Name for the key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G, // Default key is "G"
                "category.drkmod.dash" // Category for the key
        ));

        // Register a tick event to check for key presses and player state
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;
            if (player != null) {
                // Check if dash key was pressed and if dash conditions are met
                if (dashKey.wasPressed()) {
                    if (!player.isOnGround() && dodgesRemaining > 0) {
                        performDash(player);
                        dodgesRemaining--; // Decrease the number of dodges remaining
                    }
                }

                // Replenish dodges if the player is on the ground
                if (player.isOnGround() && dodgesRemaining < 2) {
                    replenishDodges();
                }
            }
        });

        // Register an event to detect when the player hits an entity
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;
            if (player != null && !player.isOnGround()) {
                // This is a basic example; you might need additional logic for proper hit detection
                // Replenish dodges if the player hits an entity while in the air
                if (player.getAttackCooldownProgress(0.5F) > 0) {
                    replenishDodgesIfHit();
                }
            }
        });
    }

    private static void performDash(PlayerEntity player) {
        // Get the player's direction (looking vector)
        Vec3d lookVector = player.getRotationVec(1.0F);

        // Scale the vector to create the dash effect (adjust multiplier for dash distance)
        Vec3d dashVector = lookVector.multiply(2.0); // Change 2.0 to whatever dash speed you want

        // Set player's velocity to the dash vector
        player.setVelocity(player.getVelocity().add(dashVector));

        // Make sure to apply the changes on the server side
        player.velocityModified = true;
    }

    private static void replenishDodges() {
        // Replenish dodges only if not at max dodges
        if (dodgesRemaining < 2) {
            dodgesRemaining = 2; // Replenish all dodges
        }
    }

    private static void replenishDodgesIfHit() {
        // Replenish dodges if the player has hit an entity while in the air
        if (dodgesRemaining < 2) {
            dodgesRemaining = 2; // Replenish all dodges
        }
    }
}
