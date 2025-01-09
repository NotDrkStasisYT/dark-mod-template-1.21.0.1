package net.drk.network.packet;

import net.drk.DarkMod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public record SummonProjectileEntityPacket(Optional<Identifier> entityTypeId, Vec3d position, Vec3d velocity) implements CustomPayload {

    public static final CustomPayload.Id<SummonEntityPacket> IDENTIFIER =
            new CustomPayload.Id<>(DarkMod.identifierOf("summon_entity"));
    // Encode method
    public void encode(PacketByteBuf buf) {
        // Write the entityTypeId to the buffer
        buf.writeOptional(this.entityTypeId, PacketByteBuf::writeIdentifier);

        // Write the position and velocity to the buffer
        buf.writeDouble(this.position.x);
        buf.writeDouble(this.position.y);
        buf.writeDouble(this.position.z);

        buf.writeDouble(this.velocity.x);
        buf.writeDouble(this.velocity.y);
        buf.writeDouble(this.velocity.z);
    }

    // Decode method
    public static SummonProjectileEntityPacket decode(PacketByteBuf buf) {
        Optional<Identifier> entityTypeId = buf.readOptional(PacketByteBuf::readIdentifier);

        // Read position and velocity from the buffer
        Vec3d position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3d velocity = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());

        return new SummonProjectileEntityPacket(entityTypeId, position, velocity);
    }

    // Handle the packet on the server side
    public void handlePacket(ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        ServerWorld world = (ServerWorld) player.getWorld();

        entityTypeId.ifPresent(id -> {
            // Get the EntityType using the Identifier
            EntityType<?> type = EntityType.get(id.toString()).orElse(null);
            if (type != null) {
                // Create the entity (here we assume it’s a projectile, for example an arrow)
                Entity entity = type.create(world);
                if (entity != null) {
                    // Set position
                    entity.setPosition(position.x, position.y, position.z);

                    // If it’s a projectile (like an arrow), set the velocity
                    if (entity instanceof ProjectileEntity projectile) {
                        projectile.setVelocity(velocity.x, velocity.y, velocity.z);
                    }

                    // Spawn the entity in the world
                    world.spawnEntity(entity);
                }
            }
        });
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return IDENTIFIER;
    }

}
