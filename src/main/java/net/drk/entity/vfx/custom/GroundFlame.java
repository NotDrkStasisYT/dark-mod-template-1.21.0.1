package net.drk.entity.vfx.custom;

import net.drk.entity.ModEntities;
import net.drk.entity.custom.PlayerEntity;
import net.drk.item.ModItems;
import net.drk.player.ShieldManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Predicate;

public class GroundFlame extends AnimalEntity {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();  // Death animation state

    private int idleAnimationTimeout = 0;
    public boolean isDying = false;
    private int deathAnimationDuration = 18; // Adjust this duration as needed
    private int deathAnimationTimer = 0;

    private int damageTickTimer = 0; // Timer for dealing damage
    private static final int DAMAGE_INTERVAL = 20; // Damage interval in ticks (1 second)
    private static final double RADIUS = 0.2; // Radius for damaging nearby entities

    private static final int SOUND_INTERVAL = 40; // Number of ticks between each fire sound (2 seconds)
    private int soundTickTimer = 0; // Timer to track when the fire sound should play

    private static final int LIFETIME = 200; // Lifetime of the fire (10 seconds)
    private int lifetimeTickTimer = 0; // Timer to track entity lifetime


    public GroundFlame(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AttackGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, net.minecraft.entity.player.PlayerEntity.class, true));


    }
    @Override
    public boolean damage(DamageSource source, float amount) {
        // Only trigger death if the source is a living entity and the entity is not already dying
        if (source.getAttacker() instanceof LivingEntity && !isDying) {
            // Stop other animations like idle
            this.idleAnimationState.stop();

            // Set the entity into a "dying" state
            this.isDying = true;

            // Start the death animation
            this.deathAnimationState.start(this.age);

            // Play the smoke particles
            for (int i = 0; i < 20; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
                double offsetY = this.random.nextDouble() * 0.5;
                double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;
                this.getWorld().addParticle(ParticleTypes.SMOKE,
                        this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                        0.0, 0.1, 0.0);
            }
            this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0f, 1.0f);

            return true;
        }

        return false;
    }
    private void startDeathSequence() {
        this.idleAnimationState.stop(); // Stop other animations like idle
        this.isDying = true; // Mark the entity as dying
        this.deathAnimationState.start(this.age); // Start the death animation

        // Play smoke particles
        for (int i = 0; i < 20; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
            double offsetY = this.random.nextDouble() * 0.5;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;
            this.getWorld().addParticle(ParticleTypes.SMOKE,
                    this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                    0.0, 0.1, 0.0);
        }
        this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0f, 1.0f);
    }


    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        // Immune to everything except direct player hits
        return !(source.getAttacker() instanceof LivingEntity);
    }

    public void damageNearbyPlayers() {
        // Cast the world to ServerWorld and get the position of the entity
        World world = this.getWorld();
        Vec3d position = this.getPos();

        // Create a bounding box with a radius of 0.2 blocks around the entity
        Box boundingBox = new Box(position.subtract(0.8, 0.8, 0.8), position.add(0.8, 0.8, 0.8));

        // Use getEntitiesByClass to find nearby players
        List<ServerPlayerEntity> nearbyPlayers = world.getEntitiesByClass(ServerPlayerEntity.class, boundingBox, player -> true);

        // Reference to the ShieldManager instance
        ShieldManager shieldManager = ShieldManager.getInstance();

        // Apply shield damage to each nearby player
        for (ServerPlayerEntity player : nearbyPlayers) {
            // Get the player's current and maximum shield
            BigInteger shield = shieldManager.getShield(player);
            BigInteger maxShield = shieldManager.getMaxShield(player);

            // Calculate 5% of the player's maximum shield
            BigInteger damage = maxShield.multiply(BigInteger.valueOf(5)).divide(BigInteger.valueOf(100));

            if (shield.compareTo(damage) >= 0) {
                // Shield can absorb the full 5% damage
                shieldManager.setShield(player, shield.subtract(damage));
            } else {
                // Shield absorbs some damage, but not all
                BigInteger remainingDamage = damage.subtract(shield);
                shieldManager.setShield(player, BigInteger.ZERO);

                // Here, you can handle the remaining damage as needed, e.g., apply to health
            }
        }
    }



    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = 160;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();
        damageTickTimer++;
        soundTickTimer++; // Increment sound timer
        lifetimeTickTimer++; // Increment lifetime timer

        if (damageTickTimer >= DAMAGE_INTERVAL) {
            damageNearbyPlayers(); // Call to damage nearby players
            damageTickTimer = 0; // Reset the timer
        }

        // Keep playing flame particles during idle
        if (soundTickTimer >= SOUND_INTERVAL && !isDying) {
            this.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 1.0f, 1.0f);  // Play fire sound
            soundTickTimer = 0; // Reset sound timer after playing
        }
        if (!isDying) {
            this.setupAnimationStates();
            this.getWorld().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }

        igniteNearbyEntities();

        if (lifetimeTickTimer >= LIFETIME && !isDying) {
            // Start the death process
            this.startDeathSequence();
        }
        // Check if the entity is dying
        if (isDying) {
            deathAnimationTimer++;
            if (deathAnimationTimer >= deathAnimationDuration) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    private void igniteNearbyEntities() {
        BlockPos pos = this.getBlockPos();
        for (Entity entity : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2D, 0.2D, 0.2D))) {
            if (entity instanceof LivingEntity && !(entity instanceof GroundFlame)) {
                entity.setOnFireFor(5); // Set them on fire for 5 seconds
            }
    }

        // Check if the entity is dying
        if (isDying) {
            // Increment the timer
            deathAnimationTimer++;

            // Check if the death animation has reached its duration
            if (deathAnimationTimer >= deathAnimationDuration) {
                // Remove the entity after the animation is done
                this.remove(RemovalReason.KILLED);
            }
        }
    }
    public boolean isPushable() {
        return false;
    }

    protected void pushAway(Entity entity) {
    }


    public boolean canCollide() {
        return false;  // Disable collision for this entity
    }

    public static DefaultAttributeContainer.Builder createGroundFlameAttributes () {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(ModItems.BLOODFIRE_SPEAR);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.GRIM_DRAGON.create(world);
    }

}