package net.drk.entity.vfx.client;

import net.drk.DarkMod;
import net.drk.entity.client.ModEntityModelLayers;
import net.drk.entity.custom.DodoEntity;
import net.drk.entity.vfx.custom.GroundFlame;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static com.ibm.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class GroundFlameRenderer extends MobEntityRenderer<GroundFlame, GroundFlameModel> {
    public GroundFlameRenderer(EntityRendererFactory.Context context) {
        super(context, new GroundFlameModel(context.getPart(ModEntityModelLayers.GROUND_FLAME)), 0f);
    }

    @Override
    public Identifier getTexture(GroundFlame entity) {
        return Identifier.of(DarkMod.MOD_ID, "textures/entity/vfx/ground_flame.png");

    }

    @Override
    public void render(GroundFlame livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f,0.5f);
        }


        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    protected int getBlockLight(GroundFlame groundFlame, BlockPos blockPos) {
        return 15;
    }
}
