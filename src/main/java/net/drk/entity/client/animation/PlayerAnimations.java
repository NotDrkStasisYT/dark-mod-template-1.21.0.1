package net.drk.entity.client.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class PlayerAnimations {
    public static final Animation IDLE = Animation.Builder.create(2f).looping()
            .addBoneAnimation("Head",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, -0.2f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("LeftArm",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.125f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createTranslationalVector(0f, -0.3f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("LeftArm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createRotationalVector(0f, 0f, -2.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("Body",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("RightArm",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.125f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createTranslationalVector(0f, -0.3f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("RightArm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createRotationalVector(0f, 0f, 2.5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation WALK = Animation.Builder.create(0.875f).looping()
            .addBoneAnimation("RightLeg",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-30f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(30f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("LeftLeg",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(30f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(-30f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("LeftArm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-17.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(17.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("RightArm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(17.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(-17.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
}