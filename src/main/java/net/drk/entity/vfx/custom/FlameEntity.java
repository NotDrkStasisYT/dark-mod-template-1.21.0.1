//package net.drk.entity.vfx.custom;
//
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.damage.DamageSource;
//import net.minecraft.entity.projectile.ProjectileEntity;
//import net.minecraft.util.hit.EntityHitResult;
//import net.minecraft.util.hit.HitResult;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//
//public class FlameEntity extends ProjectileEntity {
//    private LivingEntity target;
//
//    public FlameEntity(EntityType<? extends FlameEntity> entityType, World world) {
//        super(entityType, world);
//    }
//
//    public FlameEntity(World world, LivingEntity owner, LivingEntity target) {
//        super(ModEntities.FLAME_ENTITY, world); // Replace with your registered entity type
//        this.setOwner(owner);
//        this.target = target;
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        if (target != null && !target.isRemoved()) {
//            Vec3d direction = target.getPos().subtract(this.getPos()).normalize();
//            this.setVelocity(direction.multiply(0.5)); // Adjust speed here
//        } else {
//            this.discard(); // Remove entity if the target is invalid
//        }
//    }
//
//    @Override
//    protected void onCollision(HitResult hitResult) {
//        super.onCollision(hitResult);
//
//        if (hitResult instanceof EntityHitResult entityHitResult) {
//            if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
//                livingEntity.damage(DamageSource.magic(this, this.getOwner()), 10.0F);
//            }
//        }
//
//        this.discard(); // Remove the projectile after collision
//    }
//
//    @Override
//    public Packet<?> createSpawnPacket() {
//        return EntitySpawnPacket.create(this); // Custom packet for syncing entity on client
//    }
//}
