package net.drk.player.DoubleJump;

import net.drk.PowerManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.math.BigInteger;

public class DoubleJumpHandler {

    private static boolean canDoubleJump = false; // Tracks if the player is allowed to double jump
    private static boolean hasDoubleJumped = false; // Tracks if the player has already double-jumped
    private static boolean jumpKeyWasPressed = false; // Tracks the state of the jump key

    public static void registerDoubleJump() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) return;

            // Reset the double jump state when the player is on the ground
            if (player.isOnGround()) {
                resetJumpState();
            }

            // Check if the jump key is pressed
            boolean jumpKeyPressed = client.options.jumpKey.isPressed();

            // If jump key was released and pressed again in mid-air, perform double jump
            if (jumpKeyPressed && !jumpKeyWasPressed) { // Key pressed again after release
                if (!player.isOnGround() && canDoubleJump && !hasDoubleJumped) {
                    performDoubleJump(player);  // Perform the double jump
                    hasDoubleJumped = true;     // Mark that the player has double-jumped
                }
            }

            // Allow the double jump after the player leaves the ground (i.e., after first jump)
            if (!player.isOnGround() && !hasDoubleJumped) {
                canDoubleJump = true;
            }

            // Update jump key state for the next tick
            jumpKeyWasPressed = jumpKeyPressed;
        });
    }

    private static void performDoubleJump(PlayerEntity player) {
        Vec3d jumpBoost = new Vec3d(0, 0.5, 0);
        player.addVelocity(jumpBoost.x, jumpBoost.y, jumpBoost.z);
        player.velocityModified = true;

        spawnCloudUnderPlayer(player); // Optional: Keeps the particle effect

        // Debugging: Log double jump action
        System.out.println("Double jump performed by player: " + player.getName().getString());
    }

    private static void resetJumpState() {
        // Reset jump flags when the player touches the ground
        canDoubleJump = false;
        hasDoubleJumped = false;
        jumpKeyWasPressed = false; // Reset key press tracking
    }

    private static void spawnCloudUnderPlayer(PlayerEntity player) {
        // Get the world where the player is located
        var world = player.getEntityWorld();

        // Check if the world is client-side (particles are client-side only)
        if (world.isClient) {
            // Spawn cloud particles at the player's feet
            for (int i = 0; i < 20; i++) {  // Spawn multiple particles for a cloud effect
                world.addParticle(
                        net.minecraft.particle.ParticleTypes.CLOUD,  // Cloud particle type
                        player.getX(),  // X position (player's X)
                        player.getY() - 0.5,  // Y position (a bit below the player)
                        player.getZ(),  // Z position (player's Z)
                        (world.random.nextDouble() - 0.5) * 0.2,  // X velocity
                        0.1,  // Y velocity
                        (world.random.nextDouble() - 0.5) * 0.2   // Z velocity
                );
            }
        }
    }
}
