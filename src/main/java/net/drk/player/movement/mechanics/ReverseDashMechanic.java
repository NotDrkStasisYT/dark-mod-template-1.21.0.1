//package net.drk.player.movement.mechanics;
//
//import net.drk.player.movement.MovementUtils;
//import net.minecraft.client.network.ClientPlayerEntity;
//
//public class ReverseDashMechanic {
//    public static void execute() {
//        ClientPlayerEntity player = MovementUtils.getPlayer();
//        if (player != null) {
//            player.setVelocity(player.getVelocity().multiply(-1));
//            player.velocityModified = true;
//            MovementUtils.syncMovement(player);
//        }
//    }
//}
