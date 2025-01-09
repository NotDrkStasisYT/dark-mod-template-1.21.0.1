/*package net.drk.mixin;

import net.drk.block.sitting.TavernStool;
import net.drk.util.ILivingEntityMixin;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class SittingMixin extends Entity implements ILivingEntityMixin {

    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Shadow public abstract void stopRiding();
    @Shadow public abstract EntityDimensions getDimensions(EntityPose pose);

    protected SittingMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void trackCustomData(CallbackInfo ci) {
        // Only set custom data after the dataTracker is initialized
        if (this.dataTracker != null) {
            this.dataTracker.set(SITTING, false);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        // Make sure the dataTracker is initialized before accessing it
        if (this.dataTracker != null && nbt.contains("Sitting")) {
            this.getDataTracker().set(SITTING, nbt.getBoolean("Sitting"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        // Make sure the dataTracker is initialized before accessing it
        if (this.dataTracker != null) {
            nbt.putBoolean("Sitting", this.getDataTracker().get(SITTING));
        }
    }

    @Override
    public void sit(BlockPos pos) {
        BlockState blockState;
        if (this.hasVehicle()) {
            this.stopRiding();
        }
        if ((blockState = this.getWorld().getBlockState(pos)).getBlock() instanceof TavernStool) {
            this.setPose(EntityPose.SITTING);
            this.setPos(pos.getX(), pos.getY(), pos.getZ());

            this.setBoundingBox(this.calculateBoundingBox());
            this.setVelocity(Vec3d.ZERO);
            this.velocityDirty = true;
        }
    }

    @Override
    public boolean inSittingPose() {
        return this.getDataTracker().get(SITTING);
    }

    @Override
    public void setSittingPose(boolean sitting) {
        this.getDataTracker().set(SITTING, sitting);
    }
}
*/