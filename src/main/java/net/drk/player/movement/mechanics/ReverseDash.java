package net.drk.player.movement.mechanics;

import net.minecraft.entity.player.PlayerEntity;

public class ReverseDash {
    public static void perform(PlayerEntity player) {
        // Reverse player's velocity
        player.addVelocity(-player.getVelocity().x, 0, -player.getVelocity().z);
        player.velocityModified = true;
    }
}
