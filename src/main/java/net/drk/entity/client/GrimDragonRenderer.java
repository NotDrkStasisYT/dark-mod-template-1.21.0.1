package net.drk.entity.client;

import net.drk.DarkMod;
import net.drk.entity.custom.GrimDragonEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class GrimDragonRenderer extends MobEntityRenderer<GrimDragonEntity, GrimDragonModel> {
    public GrimDragonRenderer(EntityRendererFactory.Context context) {
        super(context, new GrimDragonModel(context.getPart(ModEntityModelLayers.GRIM_DRAGON)), 0.5f);
    }


    @Override
    public Identifier getTexture(GrimDragonEntity entity) {
        return Identifier.of(DarkMod.MOD_ID, "textures/entity/npc/grim_dragon.png");

    }

    @Override
    public void render(GrimDragonEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
