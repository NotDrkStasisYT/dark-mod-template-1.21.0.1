package net.drk.player.movement.mechanics;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class TemporalDashChain {
    private static final int MAX_CHAIN = 3; // Max dashes in a chain
    private static int currentChain = 0;
    private static long lastDashTime = 0;

    public static void perform(PlayerEntity player) {
        long currentTime = System.currentTimeMillis();

        if (currentChain > 0 && currentTime - lastDashTime > 500) {
            // Reset chain if time gap is too large
            currentChain = 0;
        }

        if (currentChain < MAX_CHAIN) {
            var lookVec = player.getRotationVector();
            double dashStrength = 0.8 + (currentChain * 0.2); // Slight increase in each chain
            player.addVelocity(lookVec.x * dashStrength, lookVec.y * dashStrength, lookVec.z * dashStrength);
            player.velocityModified = true;

            currentChain++;
            lastDashTime = currentTime;
        } else {
            player.sendMessage(Text.literal("Dash chain complete!")); // Debug feedback
        }
    }
}
