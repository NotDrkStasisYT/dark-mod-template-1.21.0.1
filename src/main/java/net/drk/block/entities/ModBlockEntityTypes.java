package net.drk.block.entities;

import net.drk.block.ModBlocks;
import net.drk.block.exclusive.tavern.entities.TavernStoolBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntityTypes {
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("drkmod", path), blockEntityType);
    }

    public static final BlockEntityType<TavernStoolBlockEntity> TAVERN_STOOL = register(
            "tavern_stool",
            // For versions 1.21.2 and above,
            // replace `BlockEntityType.Builder` with `FabricBlockEntityTypeBuilder`.
            FabricBlockEntityTypeBuilder.create(TavernStoolBlockEntity::new, ModBlocks.TAVERN_STOOL).build()
    );

    public static void initialize() {
    }
}