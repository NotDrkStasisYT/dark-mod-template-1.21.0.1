package net.drk.network.codec;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;

public class IdentifierCodec implements PacketCodec<PacketByteBuf, Identifier> {
    @Override
    public Identifier decode(PacketByteBuf buf) {
        return Identifier.of(buf.readString(32767)); // Read as string and construct Identifier
    }

    @Override
    public void encode(PacketByteBuf buf, Identifier value) {
        buf.writeString(value.toString()); // Write Identifier as string
    }
}
