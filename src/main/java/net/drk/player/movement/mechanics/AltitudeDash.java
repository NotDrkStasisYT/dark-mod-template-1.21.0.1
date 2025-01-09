package net.drk.player.movement.mechanics;

import net.minecraft.entity.player.PlayerEntity;

public class AltitudeDash {
    public static void perform(PlayerEntity player) {
        // Dash based on current look direction and altitude control
        var lookVec = player.getRotationVector();
        double dashStrength = 1.2; // Forward push
        player.addVelocity(lookVec.x * dashStrength, lookVec.y * 0.8, lookVec.z * dashStrength);
        player.velocityModified = true;
    }
}
