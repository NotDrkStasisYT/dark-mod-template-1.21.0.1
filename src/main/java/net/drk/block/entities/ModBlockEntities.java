package net.drk.block.entities;

import net.drk.block.ModBlocks;
import net.drk.block.advanced.*;
import net.drk.block.voxel.logs.blockEntity.*;
import net.drk.block.voxel.tavern.blockEntity.TavernKegBlockEntity;
import net.drk.block.voxel.tavern.blockEntity.TavernKegStationBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<BarrelBlockEntity> BARREL;
    public static BlockEntityType<OakLogBlockEntity> OAK_LOG;
    public static BlockEntityType<SpruceLogBlockEntity> SPRUCE_LOG;
    public static BlockEntityType<JungleLogBlockEntity> JUNGLE_LOG;
    public static BlockEntityType<MangroveLogBlockEntity> MANGROVE_LOG;
    public static BlockEntityType<DarkOakLogBlockEntity> DARK_OAK_LOG;
    public static BlockEntityType<CherryLogBlockEntity> CHERRY_LOG;
    public static BlockEntityType<AcaciaLogBlockEntity> ACACIA_LOG;
    public static BlockEntityType<BirchLogBlockEntity> BIRCH_LOG;
    public static BlockEntityType<TavernKegBlockEntity> TAVERN_KEG;
    public static BlockEntityType<AutumnLogBlockEntity> AUTUMN_LOG;
    public static BlockEntityType<MysticalLogBlockEntity> MYSTICAL_LOG;
    public static BlockEntityType<TavernKegStationBlockEntity> TAVERN_KEG_STATION;
    public static BlockEntityType<ShadowLogBlockEntity> SHADOW_LOG;
    public static BlockEntityType<SpiritLogBlockEntity> SPIRIT_LOG;













    public static void registerBlockEntities() {
        BARREL = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "barrel"), BlockEntityType.Builder.create(BarrelBlockEntity::new, ModBlocks.BARREL).build(null));
        OAK_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "oak_log"), BlockEntityType.Builder.create(OakLogBlockEntity::new, ModBlocks.OAK_LOG).build(null));
        SPRUCE_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "spruce_log"), BlockEntityType.Builder.create(SpruceLogBlockEntity::new, ModBlocks.SPRUCE_LOG).build(null));
        JUNGLE_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "jungle_log"), BlockEntityType.Builder.create(JungleLogBlockEntity::new, ModBlocks.JUNGLE_LOG).build(null));
        MANGROVE_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "mangrove_log"), BlockEntityType.Builder.create(MangroveLogBlockEntity::new, ModBlocks.MANGROVE_LOG).build(null));
        DARK_OAK_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "dark_oak_log"), BlockEntityType.Builder.create(DarkOakLogBlockEntity::new, ModBlocks.DARK_OAK_LOG).build(null));
        CHERRY_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "cherry_log"), BlockEntityType.Builder.create(CherryLogBlockEntity::new, ModBlocks.CHERRY_LOG).build(null));
        ACACIA_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "acacia_log"), BlockEntityType.Builder.create(AcaciaLogBlockEntity::new, ModBlocks.ACACIA_LOG).build(null));
        BIRCH_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "birch_log"), BlockEntityType.Builder.create(BirchLogBlockEntity::new, ModBlocks.BIRCH_LOG).build(null));
        TAVERN_KEG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "tavern_keg"), BlockEntityType.Builder.create(TavernKegBlockEntity::new, ModBlocks.TAVERN_KEG).build(null));
        AUTUMN_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "autumn_log"), BlockEntityType.Builder.create(AutumnLogBlockEntity::new, ModBlocks.AUTUMN_LOG).build(null));
        MYSTICAL_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "mystical_log"), BlockEntityType.Builder.create(MysticalLogBlockEntity::new, ModBlocks.MYSTICAL_LOG).build(null));
        TAVERN_KEG_STATION = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "tavern_keg_station"), BlockEntityType.Builder.create(TavernKegStationBlockEntity::new, ModBlocks.TAVERN_KEG_STATION).build(null));
        SHADOW_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "shadow_log"), BlockEntityType.Builder.create(ShadowLogBlockEntity::new, ModBlocks.SHADOW_LOG).build(null));
        SPIRIT_LOG = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", "spirit_log"), BlockEntityType.Builder.create(SpiritLogBlockEntity::new, ModBlocks.SPIRIT_LOG).build(null));

    }
}
