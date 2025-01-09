package net.drk.entity.client;

import net.drk.entity.client.animation.GrimDragonAnimations;
import net.drk.entity.client.animation.PlayerAnimations;
import net.drk.entity.custom.GrimDragonEntity;
import net.drk.entity.custom.PlayerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class GrimDragonModel extends SinglePartEntityModel<GrimDragonEntity> {
    private final ModelPart bone2;
    private final ModelPart Head;

    public GrimDragonModel(ModelPart root) {
        this.bone2 = root.getChild("bone2");
        this.Head = bone2.getChild("Head");

    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone2 = modelPartData.addChild("bone2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData LeftLeg = bone2.addChild("LeftLeg", ModelPartBuilder.create().uv(0, 29).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, -12.0F, 0.0F));

        ModelPartData RightLeg = bone2.addChild("RightLeg", ModelPartBuilder.create().uv(32, 9).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

        ModelPartData LeftArm = bone2.addChild("LeftArm", ModelPartBuilder.create().uv(32, 41).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 7.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, -22.0F, 0.0F));

        ModelPartData LeftArm2 = LeftArm.addChild("LeftArm2", ModelPartBuilder.create().uv(27, 0).cuboid(-2.0F, -2.5F, -2.0F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 7.5F, 0.0F));

        ModelPartData RightArm = bone2.addChild("RightArm", ModelPartBuilder.create().uv(16, 37).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 7.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, -22.0F, 0.0F));

        ModelPartData RightArm2 = RightArm.addChild("RightArm2", ModelPartBuilder.create().uv(43, 0).cuboid(-7.0F, -17.0F, -2.0F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 22.0F, 0.0F));

        ModelPartData Body = bone2.addChild("Body", ModelPartBuilder.create().uv(28, 25).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(24, 74).cuboid(-4.0F, 1.0F, 2.0F, 8.0F, 9.0F, 2.0F, new Dilation(0.0F))
                .uv(50, 76).cuboid(-3.0F, 5.0F, 4.0F, 6.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(58, 54).cuboid(-2.0F, 0.0F, -1.3F, 1.0F, 5.0F, -1.0F, new Dilation(0.0F))
                .uv(55, 54).cuboid(1.0F, 0.0F, -1.3F, 1.0F, 4.0F, -1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData Head = bone2.addChild("Head", ModelPartBuilder.create().uv(0, 13).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-4.5F, -9.0F, -4.2F, 9.0F, 4.0F, 9.0F, new Dilation(0.0F))
                .uv(-6, 53).cuboid(-4.0F, -5.0F, 3.0F, 8.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData Face = Head.addChild("Face", ModelPartBuilder.create().uv(2, 2).cuboid(1.0F, -1.0F, 0.0F, 2.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -4.01F));

        ModelPartData cube_r1 = Face.addChild("cube_r1", ModelPartBuilder.create().uv(2, 2).cuboid(-1.0F, -2.0F, 0.99F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -1.0F, -0.99F, 0.0F, 0.0F, -3.1416F));

        ModelPartData eyebrows = Head.addChild("eyebrows", ModelPartBuilder.create().uv(3, 1).cuboid(1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(3, 1).cuboid(-3.0F, -2.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -4.01F));
        return TexturedModelData.of(modelData, 100, 100);
    }
    @Override
    public void setAngles(GrimDragonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        this.animateMovement(GrimDragonAnimations.WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleAnimationState, GrimDragonAnimations.IDLE, ageInTicks, 1f);


    }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0f);
        headPitch = MathHelper.clamp(headPitch, -25F, 45f);

        this.Head.yaw = headYaw * 0.017453292F;
        this.Head.pitch = headPitch * 0.017453292F;

    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        bone2.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return bone2;
    }
}
