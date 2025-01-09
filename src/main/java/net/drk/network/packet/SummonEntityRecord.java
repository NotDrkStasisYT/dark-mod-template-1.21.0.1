package net.drk.network.packet;

import net.drk.DarkMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.math.BigInteger;
import java.util.UUID;

public record SummonEntityRecord(UUID slapped) implements CustomPayload {
    public static final CustomPayload.Id<SummonEntityRecord> PACKET_ID = new CustomPayload.Id<>(Identifier.of(DarkMod.MOD_ID));
    public static final PacketCodec<RegistryByteBuf, SummonEntityRecord> PACKET_CODEC = Uuids.PACKET_CODEC.xmap(SummonEntityRecord::new, SummonEntityRecord::slapped).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
