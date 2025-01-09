package net.drk.network.packet;

import io.netty.buffer.Unpooled;
import net.drk.A.ShieldDataPayload;
import net.drk.player.ShieldManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.math.BigInteger;
import java.util.Optional;

public class SummonEntityNetworking {


    public static void registerClientNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(SummonEntityPacket.IDENTIFIER, (payload, context) -> {
            context.client().execute(() -> {
                // Handle receiving the summon entity packet and perform actions on the client side
                SummonEntityPacket packet = (SummonEntityPacket) payload;

                packet.entityTypeId().ifPresent(id -> {
                    // Logic to handle the summoned entity on the client side
                    System.out.println("Summoning entity: " + id);
                    // You can perform more client-side actions here, e.g., visual effects
                });
            });
        });
    }
    public static void sendSummonEntityPacketToServer(Optional<Identifier> entityTypeId) {
        SummonEntityPacket packet = new SummonEntityPacket(entityTypeId);

        // Create a new PacketByteBuf to serialize the packet
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        // Encode the packet into the buffer
        packet.encode(buf);

        // Send the packet with the buffer
        ClientPlayNetworking.send(packet);
    }
    public static void sendSummonProjectileEntityPacketToServer(Optional<Identifier> entityTypeId, Vec3d position, Vec3d velocity) {
        // Create the SummonEntityPacket with position and velocity
        SummonProjectileEntityPacket packet = new SummonProjectileEntityPacket(entityTypeId, position, velocity);

        // Create a new PacketByteBuf to serialize the packet
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        // Encode the packet into the buffer (use encode method instead of write)
        packet.encode(buf);

        // Send the packet with the buffer
        ClientPlayNetworking.send(packet);
    }


    private static void handleSummonEntityPacket(SummonEntityPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        ServerWorld world = (ServerWorld) player.getWorld();

        packet.entityTypeId().ifPresent(id -> {
            // Get the entity type and spawn the entity above the player
            EntityType<?> entityType = EntityType.get(String.valueOf(id)).orElse(null);
            if (entityType != null) {
                BlockPos spawnPos = player.getBlockPos().up();
                Entity entity = entityType.create(world);
                if (entity != null) {
                    entity.refreshPositionAndAngles(spawnPos, 0, 0);
                    world.spawnEntity(entity);
                    System.out.println("Entity summoned: " + id);
                }
            }
        });
    }
}