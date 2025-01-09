package net.drk.entity.custom;

import net.drk.Z28.d.DamageNumbers;
import net.drk.Z28.d.ShieldEntity;
import net.drk.entity.ModEntities;
import net.drk.item.ModItems;
import net.drk.player.ShieldManager;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PlayerEntity extends AnimalEntity implements ShieldEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private BigInteger shieldStrength; // Change to BigInteger

    public PlayerEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.shieldStrength = new BigInteger("50000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"); // Example initial value

    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AttackGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, net.minecraft.entity.player.PlayerEntity.class, true));

        this.goalSelector.add(3, new TemptGoal(this, 1.25, stack -> stack.isOf(ModItems.BLOODFIRE_SPEAR), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.add(5, new WanderAroundGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.setupAnimationStates();
        }
    }

    public static DefaultAttributeContainer.Builder createPlayerAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 2) // Adjust as needed
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(ModItems.BLOODFIRE_SPEAR);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.PLAYER.create(world);
    }


    public boolean damage(DamageSource source, BigInteger amount) {
        // Prevent client-side damage processing
        if (this.getWorld().isClient) {
            System.out.println("Client-side damage processing prevented.");
            return false;
        }

        // Log the incoming damage amount
        System.out.println("Incoming damage: " + amount);
        System.out.println("Current shield strength: " + this.shieldStrength);

        // Validate incoming damage
        if (amount.compareTo(BigInteger.ZERO) < 0) {
            System.out.println("Error: Negative damage amount received. Ignoring damage.");
            return false; // Ignore negative damage
        }

        // Check if the entity is invulnerable to the damage source
        if (this.isInvulnerableTo(source)) {
            System.out.println("Entity is invulnerable to this damage source.");
            return false;
        }

        // Check if the shield is active
        if (this.shieldStrength.compareTo(BigInteger.ZERO) > 0) {
            // Log that the entity has a shield
            System.out.println("Entity has an active shield. Shield strength: " + this.shieldStrength);

            // Calculate damage absorbed by the shield
            BigInteger damageToShield = this.shieldStrength.min(amount);
            System.out.println("Calculated damage to shield: " + damageToShield);

            // Check if damageToShield is zero
            if (damageToShield.equals(BigInteger.ZERO)) {
                System.out.println("No damage was absorbed by the shield.");
                return false; // No damage absorbed, early exit
            }

            // Subtract the absorbed damage from the shield strength
            this.shieldStrength = this.shieldStrength.subtract(damageToShield);
            System.out.println("Shield strength after damage: " + this.shieldStrength);

            // Call the onShieldChange method for the DamageNumbers handler
            DamageNumbers.getHandler().onShieldChange(this, this.shieldStrength.add(damageToShield), this.shieldStrength);

            // Log updated shield strength
            System.out.println("Updated shield strength after damage processing: " + this.shieldStrength);

            // If the shield is depleted, apply remaining damage to health
            if (this.shieldStrength.compareTo(BigInteger.ZERO) <= 0) {
                BigInteger remainingDamage = amount.subtract(damageToShield);
                System.out.println("Shield depleted. Remaining damage to be applied to health: " + remainingDamage);

                // Validate remaining damage before applying to health
                if (remainingDamage.compareTo(BigInteger.ZERO) > 0) {
                    System.out.println("Applying remaining damage to health.");
                    return super.damage(source, remainingDamage.floatValue());
                } else {
                    System.out.println("No remaining damage to apply to health.");
                    return false;
                }
            }

            // Shield absorbed all the damage, no health damage is applied
            System.out.println("Shield absorbed all the damage, no health damage applied.");
            return false;
        } else {
            // No shield active
            System.out.println("No shield active. Full damage applied to health.");
            return super.damage(source, amount.floatValue());
        }
    }





    // Update methods to use ShieldStrength
    @Override
    public BigInteger getShieldStrength() { // Return BigInteger
        return this.shieldStrength;
    }

    @Override
    public void setHealth(float health) {
        // Cap the health to the max value you've defined, if needed
        float maxHealth = this.getMaxHealth();
        if (health > maxHealth) {
            health = maxHealth;
        }
        super.setHealth(health);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("ShieldStrength", this.shieldStrength.toString()); // Store as string for NBT
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("ShieldStrength")) {
            this.shieldStrength = new BigInteger(nbt.getString("ShieldStrength"));
        }
    }



    @Override
    public void kill() {
        // Custom death logic here
        super.kill();
    }

    @Override
    public boolean tryAttack(Entity target) {
        // Custom attack damage logic (modify this value as needed)
        BigDecimal attackDamage = BigDecimal.valueOf(this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));

        // Example: Increase attack damage by a factor (modify as needed)
        attackDamage = attackDamage.multiply(BigDecimal.valueOf(5000000000.0));  // Set to the desired damage

        // Create the damage source from the mob
        DamageSource damageSource = this.getDamageSources().mobAttack(this);

        // Apply enchantment-based damage scaling if in ServerWorld
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            // Convert BigDecimal to float for enchantment calculation
            float scaledDamage = attackDamage.floatValue();
            scaledDamage = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), target, damageSource, scaledDamage);
            attackDamage = BigDecimal.valueOf(scaledDamage);
        }

        // Apply damage to the shield and additional damage
        if (target instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) target;
            ShieldManager shieldManager = ShieldManager.getInstance();
            BigInteger shield = shieldManager.getShield(player);
            BigInteger maxShield = shieldManager.getMaxShield(player);

            if (shield.compareTo(BigInteger.ZERO) > 0) {
                BigInteger damage = attackDamage.toBigInteger();

                // Calculate additional damage (5% of max shield)
                BigInteger additionalDamage = maxShield.multiply(BigInteger.valueOf(5)).divide(BigInteger.valueOf(100));

                // Total damage (shield damage + additional damage)
                BigInteger totalDamage = damage.add(additionalDamage);

                if (shield.compareTo(totalDamage) >= 0) {
                    // Shield absorbs the full damage including additional damage
                    shieldManager.setShield(player, shield.subtract(totalDamage));
                    // Cancel the attack effect (health not reduced)
                    return false;
                } else {
                    // Shield absorbs some damage, additional damage is dealt
                    BigInteger remainingDamage = totalDamage.subtract(shield);
                    shieldManager.setShield(player, BigInteger.ZERO);
                    // Apply remaining damage to health or another mechanic if needed
                    // For now, we will only handle shield damage
                    // Example: log remaining damage or apply it elsewhere
                    // System.out.println("Remaining damage: " + remainingDamage);
                    return true;
                }
            }
        }

        // Proceed with normal attack logic if not targeting a player or shield is not active
        return super.tryAttack(target);
    }

    @Override
    public <A> @Nullable A getAttached(AttachmentType<A> type) {
        return super.getAttached(type);
    }
}
