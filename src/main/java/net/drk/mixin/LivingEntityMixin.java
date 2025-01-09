package net.drk.mixin;

import net.drk.number.NumberFormatter;
import net.drk.player.ShieldManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.math.BigInteger;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ShieldManager shieldManager = ShieldManager.getInstance();
            BigInteger shield = shieldManager.getShield(player);

            if (shield.compareTo(BigInteger.ZERO) > 0) {
                // Shield is active, convert the damage to BigInteger
                BigInteger damage = BigInteger.valueOf(Math.round(amount));

                if (shield.compareTo(damage) >= 0) {
                    // Shield can fully absorb the damage
                    shieldManager.setShield(player, shield.subtract(damage));
                    cir.setReturnValue(false); // Cancel damage to health
                    Text message = Text.literal("Your shield absorbed " + NumberFormatter.formatPrice(damage) + " damage!");
                    player.sendMessage(message, false);
                } else {
                    // Shield partially absorbs the damage, the rest goes to health
                    BigInteger remainingDamage = damage.subtract(shield);
                    shieldManager.setShield(player, BigInteger.ZERO); // Deplete the shield

                    // Update the remaining damage for health
                    amount = remainingDamage.floatValue();

                    Text message = Text.literal("Your shield absorbed " + NumberFormatter.formatPrice(shield) + " damage! You have " + NumberFormatter.formatPrice(BigInteger.ZERO) + " shield left.");
                    player.sendMessage(message, false);
                }
            }
        }
    }
}
