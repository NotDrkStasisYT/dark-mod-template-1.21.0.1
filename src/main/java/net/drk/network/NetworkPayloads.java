package net.drk.network;

import net.drk.network.packet.SummonEntityPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

@SuppressWarnings("SameParameterValue")

public class NetworkPayloads {
    static {
        registerC2S(SummonEntityPacket.IDENTIFIER, SummonEntityPacket.CODEC);

    }
    private static <T extends CustomPayload> void registerS2C(CustomPayload.Id<T> packetIdentifier, PacketCodec<RegistryByteBuf, T> codec) {
        PayloadTypeRegistry.playS2C().register(packetIdentifier, codec);
    }

    private static <T extends CustomPayload> void registerC2S(CustomPayload.Id<T> packetIdentifier, PacketCodec<RegistryByteBuf, T> codec) {
        PayloadTypeRegistry.playC2S().register(packetIdentifier, codec);
    }
    public static void initialize() {
    }
}
