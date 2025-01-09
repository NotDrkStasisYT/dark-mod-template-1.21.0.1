package net.drk.network;

import net.drk.network.packet.SummonEntityPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.logging.Logger;


public class CustomC2SNetworking {
    static {
        ServerPlayNetworking.registerGlobalReceiver(SummonEntityPacket.IDENTIFIER, SummonEntityPacket::handlePacket);

    }

    public static void initialize() {
    }
}