//package net.drk.player.movement.mechanics;
//
//import net.drk.player.movement.MovementUtils;
//import net.minecraft.client.network.ClientPlayerEntity;
//
//public class DashMechanic {
//    public static void execute() {
//        ClientPlayerEntity player = MovementUtils.getPlayer();
//        if (player != null) {
//            double dashStrength = 1.5;
//            // Adjust altitude based on key input
//            double yVelocity = player.input.jumping ? dashStrength : (player.input.sneaking ? -dashStrength : 0);
//            player.addVelocity(player.getRotationVec(1.0F).x * dashStrength, yVelocity, player.getRotationVec(1.0F).z * dashStrength);
//            player.velocityModified = true;
//            MovementUtils.syncMovement(player);
//        }
//    }
//}
