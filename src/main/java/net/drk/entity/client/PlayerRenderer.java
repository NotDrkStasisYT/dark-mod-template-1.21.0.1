package net.drk.entity.client;

import net.drk.DarkMod;
import net.drk.entity.custom.PlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PlayerRenderer extends MobEntityRenderer<PlayerEntity, PlayerModel> {
    public PlayerRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerModel(context.getPart(ModEntityModelLayers.PLAYER)), 0.5f);
    }


    @Override
    public Identifier getTexture(PlayerEntity entity) {
        return Identifier.of(DarkMod.MOD_ID, "textures/entity/npc/npc1.png");

    }

    @Override
    public void render(PlayerEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
