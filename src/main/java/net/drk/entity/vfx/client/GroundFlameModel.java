package net.drk.entity.vfx.client;

import net.drk.entity.vfx.client.animations.GroundFlameAnimations;
import net.drk.entity.vfx.custom.GroundFlame;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class GroundFlameModel extends SinglePartEntityModel<GroundFlame> {
    private final ModelPart ground_flame;

    public GroundFlameModel(ModelPart root) {
        this.ground_flame = root.getChild("ground_flame");

    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData ground_flame = modelPartData.addChild("ground_flame", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData vfx = ground_flame.addChild("vfx", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_flame_rot1 = vfx.addChild("cube_flame_rot1", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_flame1 = cube_flame_rot1.addChild("cube_flame1", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData cube_flame_rot2 = vfx.addChild("cube_flame_rot2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        ModelPartData cube_flame2 = cube_flame_rot2.addChild("cube_flame2", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData cube_flame_rot3 = vfx.addChild("cube_flame_rot3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        ModelPartData cube_flame3 = cube_flame_rot3.addChild("cube_flame3", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData cube_flame_rot4 = vfx.addChild("cube_flame_rot4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_flame4 = cube_flame_rot4.addChild("cube_flame4", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData cube_flame_rot5 = vfx.addChild("cube_flame_rot5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_flame5 = cube_flame_rot5.addChild("cube_flame5", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData cube_flame_rot6 = vfx.addChild("cube_flame_rot6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_flame6 = cube_flame_rot6.addChild("cube_flame6", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData cube_flame_rot7 = vfx.addChild("cube_flame_rot7", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_flame7 = cube_flame_rot7.addChild("cube_flame7", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData cube_flame_rot8 = vfx.addChild("cube_flame_rot8", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_flame8 = cube_flame_rot8.addChild("cube_flame8", ModelPartBuilder.create().uv(15, 29).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(38, 27).cuboid(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -5.0F));

        ModelPartData big_particle = vfx.addChild("big_particle", ModelPartBuilder.create().uv(21, 12).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -10.0F, 0.0F));

        ModelPartData center_flame1 = vfx.addChild("center_flame1", ModelPartBuilder.create().uv(0, 48).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData center_flame2 = vfx.addChild("center_flame2", ModelPartBuilder.create().uv(0, 48).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData center_flame3 = vfx.addChild("center_flame3", ModelPartBuilder.create().uv(0, 48).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(GroundFlame entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.updateAnimation(entity.idleAnimationState, GroundFlameAnimations.IDLE, ageInTicks, 1f);
        if (entity.isDying) {
            this.updateAnimation(entity.deathAnimationState, GroundFlameAnimations.DEATH, ageInTicks, 1f); // Add death animation here
        }
    }



    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        ground_flame.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return ground_flame;
    }
}
