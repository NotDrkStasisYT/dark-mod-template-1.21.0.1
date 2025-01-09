package net.drk.network.lumin;

import net.drk.LuminCurrency;
import net.drk.network.lumin.LuminDataPayload;
import net.drk.player.ShieldManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.math.BigInteger;

public class LuminNetworking {

    public static void registerNetworking() {
        // Register server-to-client payload
        PayloadTypeRegistry.playS2C().register(LuminDataPayload.ID, LuminDataPayload.CODEC);

        // Register client-side receiver

    }
    public static void registerClientNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(LuminDataPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                LuminCurrency luminCurrency = LuminCurrency.getInstance();
                luminCurrency.updateClientLumin(payload.playerName(), payload.lumin());
            });
        });
    }
    public static void sendLuminDataToPlayer(ServerPlayerEntity player) {
        LuminCurrency luminCurrency = LuminCurrency.getInstance();
        BigInteger lumin = luminCurrency.getAmount(player);

        LuminDataPayload payload = new LuminDataPayload(player.getName().getString(), lumin);
        ServerPlayNetworking.send(player, payload);
    }

}