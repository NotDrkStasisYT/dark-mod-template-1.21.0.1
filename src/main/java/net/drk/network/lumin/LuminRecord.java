package net.drk.network.lumin;

import net.drk.A.BigIntegerCodec;
import net.drk.DarkMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.math.BigInteger;
import java.util.UUID;

public record LuminRecord(UUID slapped) implements CustomPayload {
    public static final CustomPayload.Id<LuminRecord> PACKET_ID = new CustomPayload.Id<>(Identifier.of(DarkMod.MOD_ID));
    public static final PacketCodec<RegistryByteBuf, LuminRecord> PACKET_CODEC = Uuids.PACKET_CODEC.xmap(LuminRecord::new, LuminRecord::slapped).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
