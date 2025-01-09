package net.drk.A;

import net.drk.player.ShieldManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.math.BigInteger;

public class ShieldNetworking {

    public static void registerNetworking() {
        // Register server-to-client payload
        PayloadTypeRegistry.playS2C().register(ShieldDataPayload.ID, ShieldDataPayload.CODEC);

        // Register client-side receiver

    }
    public static void registerClientNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(ShieldDataPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ShieldManager shieldManager = ShieldManager.getInstance();
                shieldManager.updateClientShield(payload.playerName(), payload.shield(), payload.maxShield());
            });
        });
    }
    public static void sendShieldDataToPlayer(ServerPlayerEntity player) {
        ShieldManager shieldManager = ShieldManager.getInstance();
        BigInteger shield = shieldManager.getShield(player);
        BigInteger maxShield = shieldManager.getMaxShield(player);

        ShieldDataPayload payload = new ShieldDataPayload(player.getName().getString(), shield, maxShield);
        ServerPlayNetworking.send(player, payload);
    }

    public static void broadcastShieldData(ServerWorld world, ServerPlayerEntity targetPlayer) {
        ShieldManager shieldManager = ShieldManager.getInstance();
        BigInteger shield = shieldManager.getShield(targetPlayer);
        BigInteger maxShield = shieldManager.getMaxShield(targetPlayer);

        ShieldDataPayload payload = new ShieldDataPayload(targetPlayer.getName().getString(), shield, maxShield);
        for (ServerPlayerEntity player : PlayerLookup.tracking(targetPlayer)) {
            ServerPlayNetworking.send(player, payload);
        }
    }


}