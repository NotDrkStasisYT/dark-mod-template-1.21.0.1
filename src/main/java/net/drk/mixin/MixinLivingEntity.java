package net.drk.mixin;

import net.drk.Z28.d.DamageNumbers;
import net.drk.Z28.d.ShieldEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.math.BigInteger;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Unique
    private float previousHealth = -1.0f;  // Initialize with an invalid value
    @Unique
    private BigInteger previousShield = BigInteger.valueOf(-1);  // Initialize with an invalid value

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void injected$tick(CallbackInfo info) {
        var entity = (LivingEntity) (Object) this;

        // Only run on client-side
        var world = entity.getWorld();
        if (world == null || !world.isClient()) return;

        float currentHealth = entity.getHealth();
        if (previousHealth == -1.0F) {
            previousHealth = currentHealth; // First tick initialization
        }

        // Health change detection
        if (currentHealth != previousHealth) {
            DamageNumbers.getHandler().onEntityHealthChange(entity, previousHealth, currentHealth);
            previousHealth = currentHealth;
        }

        // Shield change detection (if entity has shields)
        if (entity instanceof ShieldEntity shieldEntity) {
            BigInteger currentShield = shieldEntity.getShieldStrength();
            if (previousShield == BigInteger.ONE) {
                previousShield = currentShield; // First tick initialization
            }

            if (currentShield != previousShield) {
                DamageNumbers.getHandler().onShieldChange(entity, previousShield, currentShield);
                previousShield = currentShield;
            }
        }
    }
}
