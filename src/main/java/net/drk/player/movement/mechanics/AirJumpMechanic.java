//package net.drk.player.movement.mechanics;
//
//import net.drk.player.movement.MovementUtils;
//import net.minecraft.client.network.ClientPlayerEntity;
//
//public class AirJumpMechanic {
//    private static final int MAX_AIR_JUMPS = 2;
//    private static int airJumpsRemaining = MAX_AIR_JUMPS;
//
//    public static void execute() {
//        ClientPlayerEntity player = MovementUtils.getPlayer();
//        if (player != null && airJumpsRemaining > 0 && !player.isOnGround()) {
//            airJumpsRemaining--;
//            // Apply velocity for air jump
//            player.addVelocity(player.getRotationVec(1.0F).x, 0.6, player.getRotationVec(1.0F).z);
//            player.velocityModified = true;
//            MovementUtils.syncMovement(player);
//        }
//    }
//
//    public static void onLand() {
//        airJumpsRemaining = MAX_AIR_JUMPS;
//    }
//}
