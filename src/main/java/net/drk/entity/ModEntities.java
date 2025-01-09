package net.drk.entity;

import com.google.common.hash.BloomFilter;
import net.drk.DarkMod;
import net.drk.entity.custom.DodoEntity;
import net.drk.entity.custom.GrimDragonEntity;
import net.drk.entity.custom.PlayerEntity;
import net.drk.entity.vfx.custom.GroundFlame;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<DodoEntity> DODO = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(DarkMod.MOD_ID, "dodo"),
            EntityType.Builder.create(DodoEntity::new, SpawnGroup.CREATURE).dimensions(1f, 2.5f).build());

    public static final EntityType<PlayerEntity> PLAYER = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(DarkMod.MOD_ID, "player"),
            EntityType.Builder.create(PlayerEntity::new, SpawnGroup.CREATURE).dimensions(1f, 2f).build());

    public static final EntityType<GrimDragonEntity> GRIM_DRAGON = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(DarkMod.MOD_ID, "grim_dragon"),
            EntityType.Builder.create(GrimDragonEntity::new, SpawnGroup.CREATURE).dimensions(1f, 2f).build());

    public static final EntityType<GroundFlame> GROUND_FLAME = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(DarkMod.MOD_ID, "ground_flame"),
            EntityType.Builder.create(GroundFlame::new, SpawnGroup.MISC).dimensions(1f, 1.4f).build());



    public static void registerModEntities() {
        DarkMod.LOGGER.info("Registering Mod Entities for " + DarkMod.MOD_ID);

    }

}
