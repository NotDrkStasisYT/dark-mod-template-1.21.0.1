package net.drk.A;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.math.BigInteger;

public class BigIntegerCodec implements PacketCodec<PacketByteBuf, BigInteger> {

    @Override
    public BigInteger decode(PacketByteBuf buf) {
        // Read the byte array from the buffer and convert it to BigInteger
        byte[] bytes = buf.readByteArray();
        return new BigInteger(bytes);
    }

    @Override
    public void encode(PacketByteBuf buf, BigInteger value) {
        // Convert BigInteger to byte array and write to the buffer
        byte[] bytes = value.toByteArray();
        buf.writeByteArray(bytes);
    }

    public static void encode1(PacketByteBuf buf, BigInteger value) {
        // Convert BigInteger to byte array and write to the buffer
        byte[] bytes = value.toByteArray();
        buf.writeByteArray(bytes);
    }

    public static BigInteger decode1(PacketByteBuf buf) {
        // Read the byte array from the buffer and convert it to BigInteger
        byte[] bytes = buf.readByteArray();
        return new BigInteger(bytes);
    }
}
