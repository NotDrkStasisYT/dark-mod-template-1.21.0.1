package net.drk.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {
    public static final FoodComponent SUSPICIOUS_FOOD_COMPONENT = new FoodComponent.Builder()
            .alwaysEdible()
            .snack()
            // The duration is in ticks, 20 ticks = 1 second
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 6 * 20, 1), 1.0f)

            .build();

    public static final FoodComponent OVERSHIELD_FOOD_COMPONENT = new FoodComponent.Builder()
            .alwaysEdible()
            .snack()
            // The duration is in ticks, 20 ticks = 1 second
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 6 * 20, 1), 1.0f)
            .build();
}
