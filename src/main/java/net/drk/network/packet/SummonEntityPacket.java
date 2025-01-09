package net.drk.network.packet;

import net.drk.A.BigIntegerCodec;
import net.drk.A.ShieldDataPayload;
import net.drk.DarkMod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.math.BigInteger;
import java.util.Optional;

public record SummonEntityPacket(Optional<Identifier> entityTypeId) implements CustomPayload {

    public static final CustomPayload.Id<SummonEntityPacket> IDENTIFIER =
            new CustomPayload.Id<>(DarkMod.identifierOf("summon_entity"));

    // Codec for serialization and deserialization
    public static final PacketCodec<RegistryByteBuf, SummonEntityPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.optional(PacketCodecs.STRING.xmap(Identifier::tryParse, Identifier::toString)),
            SummonEntityPacket::entityTypeId,
            SummonEntityPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return IDENTIFIER;
    }

    // Encode method
    public void encode(PacketByteBuf buf) {
        // Write the entityTypeId to the buffer
        buf.writeOptional(this.entityTypeId, PacketByteBuf::writeIdentifier);
    }

    public static SummonEntityPacket decode(PacketByteBuf buf) {
        Optional<Identifier> entityTypeId = buf.readOptional(PacketByteBuf::readIdentifier);
        return new SummonEntityPacket(entityTypeId);
    }

    public void handlePacket(ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        ServerWorld world = (ServerWorld) player.getWorld();

        entityTypeId.ifPresent(id -> {
            EntityType<?> type = EntityType.get(String.valueOf(id)).orElse(null); // Get the EntityType using the Identifier
            if (type != null) {
                BlockPos position = player.getBlockPos().up(); // Spawn above the player
                Entity entity = type.create(world);
                if (entity != null) {
                    entity.refreshPositionAndAngles(position, 0, 0);
                    world.spawnEntity(entity);
                }
            }
        });
    }
}
