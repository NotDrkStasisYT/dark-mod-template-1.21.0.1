package net.drk.event;

import net.minecraft.entity.EntityType;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class EntityLuminMapper {

    private static final Map<EntityType<?>, BigInteger> entityLuminMap = new HashMap<>();

    static {
        // Define lumin values for each entity type
        entityLuminMap.put(EntityType.ZOMBIE, BigInteger.valueOf(100)); // Example: Zombie gives 100 lumin
        entityLuminMap.put(EntityType.SKELETON, BigInteger.valueOf(150)); // Skeleton gives 150 lumin
        entityLuminMap.put(EntityType.ENDER_DRAGON, BigInteger.valueOf(50000)); // Ender Dragon gives 50000 lumin
        entityLuminMap.put(EntityType.CREEPER, BigInteger.valueOf(200)); // Creeper gives 200 lumin
        // Add more entities as needed
    }

    // Get the lumin for a specific entity type
    public static BigInteger getLuminForEntity(EntityType<?> entityType) {
        return entityLuminMap.getOrDefault(entityType, BigInteger.valueOf(50)); // Default value if entity not in map
    }
}
