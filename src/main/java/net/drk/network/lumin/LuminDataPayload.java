package net.drk.network.lumin;

import net.drk.A.BigIntegerCodec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.math.BigInteger;

public record LuminDataPayload(String playerName, BigInteger lumin) implements CustomPayload {

    // Unique ID for this payload type
    public static final CustomPayload.Id<LuminDataPayload> ID = new CustomPayload.Id<>(Identifier.of("drkmod", "lumin_data"));

    // Codec to handle serialization/deserialization
    public static final PacketCodec<RegistryByteBuf, LuminDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, LuminDataPayload::playerName,
            new BigIntegerCodec(), LuminDataPayload::lumin,  // Correct BigInteger codec usage
            LuminDataPayload::new
    );

    // Constructor (automatically generated for record classes)
    public LuminDataPayload(String playerName, BigInteger lumin) {
        this.playerName = playerName;
        this.lumin = lumin;
    }

    // Getter methods (automatically generated for record classes)
    public String playerName() {
        return playerName;
    }

    public BigInteger lumin() {
        return lumin;
    }
    public static void encode(LuminDataPayload packet, PacketByteBuf buf) {
        // Write the player name as a string
        buf.writeString(packet.playerName());

        // Write the shield value (BigInteger) using the custom codec
        new BigIntegerCodec().encode(buf, packet.lumin);

        // Write the maxShield value (BigInteger) using the custom codec
    }

    // Decode method to deserialize the payload from a PacketByteBuf
    public static LuminDataPayload decode(PacketByteBuf buf) {
        // Read the player name (String)
        String playerName = buf.readString();

        // Read the shield value (BigInteger)
        BigInteger lumin = new BigIntegerCodec().decode(buf);

        // Read the maxShield value (BigInteger)

        // Return a new ShieldDataPayload object
        return new LuminDataPayload(playerName, lumin);
    }
    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    }
