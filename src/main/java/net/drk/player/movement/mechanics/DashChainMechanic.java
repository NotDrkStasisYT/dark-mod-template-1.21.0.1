//package net.drk.player.movement.mechanics;
//
//import net.drk.player.movement.MovementUtils;
//import net.minecraft.client.network.ClientPlayerEntity;
//
//public class DashChainMechanic {
//    private static int chainCount = 0;
//    private static final int MAX_CHAIN_COUNT = 5;
//    private static final int DASH_CHAIN_WINDOW_TICKS = 20; // 1 second
//    private static int chainTimer = 0;
//
//    public static void execute() {
//        ClientPlayerEntity player = MovementUtils.getPlayer();
//        if (player != null && chainCount < MAX_CHAIN_COUNT && chainTimer > 0) {
//            chainCount++;
//            chainTimer = DASH_CHAIN_WINDOW_TICKS; // Reset the chain timer
//            // Apply dash velocity
//            player.addVelocity(player.getRotationVec(1.0F).x, 0, player.getRotationVec(1.0F).z);
//            player.velocityModified = true;
//            MovementUtils.syncMovement(player);
//        }
//    }
//
//    public static void onTick() {
//        if (chainTimer > 0) chainTimer--;
//        if (chainTimer == 0) chainCount = 0;
//    }
//}
