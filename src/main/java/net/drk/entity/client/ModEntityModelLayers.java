package net.drk.entity.client;

import net.drk.DarkMod;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModEntityModelLayers {
    public static final EntityModelLayer DODO =
            new EntityModelLayer(Identifier.of(DarkMod.MOD_ID, "dodo"),"main");

    public static final EntityModelLayer PLAYER =
            new EntityModelLayer(Identifier.of(DarkMod.MOD_ID, "player"),"main");

    public static final EntityModelLayer GRIM_DRAGON =
            new EntityModelLayer(Identifier.of(DarkMod.MOD_ID, "grim_dragon"),"main");

    public static final EntityModelLayer GROUND_FLAME =
            new EntityModelLayer(Identifier.of(DarkMod.MOD_ID, "ground_flame"),"main");
}
