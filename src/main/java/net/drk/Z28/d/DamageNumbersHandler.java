package net.drk.Z28.d;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public interface DamageNumbersHandler {

    void onEntityHealthChange(@NotNull LivingEntity entity, float oldHealth, float newHealth);

    void onShieldChange(@NotNull LivingEntity entity, BigInteger oldShield, BigInteger newShield);
}
