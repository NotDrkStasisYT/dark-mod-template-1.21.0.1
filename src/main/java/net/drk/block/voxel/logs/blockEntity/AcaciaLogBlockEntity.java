package net.drk.block.voxel.logs.blockEntity;

import net.drk.block.entities.ModBlockEntities;
import net.drk.block.voxel.logs.block.AcaciaLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AcaciaLogBlockEntity extends BlockEntity {
    private int maxHealth = 12; // Example starting health
    private int health = 12; // Example starting health
    private int rotation;  // Store the rotation value
    private float scale = 1f;
    private int regenTimer = 0; // Timer to track regeneration intervals
    private static final int REGEN_INTERVAL = 40; // Regenerate 20 ticks/second
    public static final IntProperty ROTATION = AcaciaLog.ROTATION;


    public AcaciaLogBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACACIA_LOG, pos, state);  // You will need to register the entity type elsewhere
        this.rotation = 0;  // Default to rotation 0
    }

    public float getRotationDegrees(BlockState state) {
        return RotationPropertyHelper.toDegrees((Integer)state.get(ROTATION));
    }


    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        markDirty();  // Mark the entity as dirty to save changes
        if (world != null && !world.isClient) {
            // Only send update packet on the server side
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }


    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.rotation = nbt.getInt("rotation");
        this.health = nbt.getInt("health");
        super.readNbt(nbt, registryLookup);

    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("rotation", this.rotation);
        nbt.putInt("health", this.health);

        super.writeNbt(nbt, registryLookup);

    }
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
    public void damageBlock(int damage) {
        health -= damage;
        if (health < 0) health = 0;
        markDirty();  // This ensures the entity is marked for re-syncing

        if (world != null && !world.isClient) {
            // Trigger an update to the block for the client
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }


    public boolean isDestroyed() {
        return health <= 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, AcaciaLogBlockEntity entity) {
        if (!world.isClient) { // Server-side logic
            entity.regenTimer++;

            // Regenerate health every REGEN_INTERVAL ticks
            if (entity.regenTimer >= REGEN_INTERVAL) {
                entity.regenTimer = 0; // Reset the timer
                if (entity.health < entity.maxHealth) {
                    entity.health++;
                    entity.markDirty();

                    // Notify clients of the updated health
                    world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                }
            }
        }
    }


    public int getHealth() {
        markDirty();
        return health;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

}