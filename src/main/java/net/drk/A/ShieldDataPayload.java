package net.drk.A;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.math.BigInteger;

public record ShieldDataPayload(String playerName, BigInteger shield, BigInteger maxShield) implements CustomPayload {

    // Unique ID for this payload type
    public static final CustomPayload.Id<ShieldDataPayload> ID = new CustomPayload.Id<>(Identifier.of("drkmod", "shield_data"));

    // Codec to handle serialization/deserialization
    public static final PacketCodec<RegistryByteBuf, ShieldDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ShieldDataPayload::playerName,
            new BigIntegerCodec(), ShieldDataPayload::shield,  // Correct BigInteger codec usage
            new BigIntegerCodec(), ShieldDataPayload::maxShield,
            ShieldDataPayload::new
    );

    // Constructor (automatically generated for record classes)
    public ShieldDataPayload(String playerName, BigInteger shield, BigInteger maxShield) {
        this.playerName = playerName;
        this.shield = shield;
        this.maxShield = maxShield;
    }

    // Getter methods (automatically generated for record classes)
    public String playerName() {
        return playerName;
    }

    public BigInteger shield() {
        return shield;
    }

    public BigInteger maxShield() {
        return maxShield;
    }

    // Encode method to serialize the payload into a PacketByteBuf
    public static void encode(ShieldDataPayload packet, PacketByteBuf buf) {
        // Write the player name as a string
        buf.writeString(packet.playerName());

        // Write the shield value (BigInteger) using the custom codec
        new BigIntegerCodec().encode(buf, packet.shield());

        // Write the maxShield value (BigInteger) using the custom codec
        new BigIntegerCodec().encode(buf, packet.maxShield());
    }

    // Decode method to deserialize the payload from a PacketByteBuf
    public static ShieldDataPayload decode(PacketByteBuf buf) {
        // Read the player name (String)
        String playerName = buf.readString();

        // Read the shield value (BigInteger)
        BigInteger shield = new BigIntegerCodec().decode(buf);

        // Read the maxShield value (BigInteger)
        BigInteger maxShield = new BigIntegerCodec().decode(buf);

        // Return a new ShieldDataPayload object
        return new ShieldDataPayload(playerName, shield, maxShield);
    }

    // Override the getId method to return the unique ID for this payload type
    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
