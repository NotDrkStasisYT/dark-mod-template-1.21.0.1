package net.drk.vfx;

import net.drk.entity.vfx.custom.GroundFlame;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VFXManager {

    /**
     * Spawns a VFX entity in the world at the specified position.
     *
     * @param world The world where the entity should spawn. Must be a ServerWorld.
     * @param position The position to spawn the entity.
     * @param entityClass The VFX entity class to spawn.
     * @param <T> The type of the entity (must extend Entity).
     * @return The spawned entity instance or null if it failed.
     */
    public static <T extends Entity> T spawnVFX(ServerWorld world, Vec3d position, Class<T> entityClass) {
        try {
            // Use reflection to create the entity
            T entity = entityClass.getDeclaredConstructor(World.class).newInstance(world);
            entity.refreshPositionAndAngles(position.x, position.y, position.z, 0.0f, 0.0f);

            // Make sure the entity is spawned properly
            if (world.spawnEntity(entity)) {
                return entity;
            } else {
                System.err.println("Entity could not be spawned.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Spawns a VFX entity with a specific owner.
     *
     * @param world The server world.
     * @param position The position to spawn the entity.
     * @param entityClass The VFX entity class.
     * @param owner The owner of the entity (can be null).
     * @param <T> The type of the entity (must extend Entity).
     * @return The spawned entity or null if it failed.
     */
    public static <T extends Entity> T spawnVFXWithOwner(ServerWorld world, Vec3d position, Class<T> entityClass, @Nullable Entity owner) {
        T entity = spawnVFX(world, position, entityClass);
        if (entity != null && owner != null) {
            entity.setCustomName(owner.getDisplayName());  // Set the custom name as owner
        }
        return entity;
    }
}
