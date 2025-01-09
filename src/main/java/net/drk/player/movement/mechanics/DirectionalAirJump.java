package net.drk.player.movement.mechanics;

import net.minecraft.entity.player.PlayerEntity;

public class DirectionalAirJump {
    public static void perform(PlayerEntity player) {
        // Allow a small directional boost based on current velocity and look direction
        var lookVec = player.getRotationVector();
        double boost = 0.6; // Moderate boost
        player.addVelocity(lookVec.x * boost, 0.4, lookVec.z * boost); // Y slightly higher for lift
        player.velocityModified = true;
    }
}
