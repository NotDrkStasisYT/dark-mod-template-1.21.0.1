package net.drk.event;

import net.drk.PowerManager;
import net.drk.item.ModItems;
import net.drk.item.other.stats.BloodfireSpear;
import net.drk.item.other.stats.Gravebane;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PowerBasedDamageEventListener {

    private final PowerManager powerManager;
    private final Map<Item, Object> itemStatsMap = new HashMap<>(); // Store instances of item-specific classes

    public PowerBasedDamageEventListener(PowerManager powerManager) {
        this.powerManager = powerManager;
        initializeItemStats();
        AttackEntityCallback.EVENT.register(this::onPlayerAttackEntity);
    }

    private void initializeItemStats() {
        // Initialize item-specific classes for each weapon
        itemStatsMap.put(ModItems.BLOODFIRE_SPEAR, new BloodfireSpear());
        itemStatsMap.put(ModItems.GRAVEBANE, new Gravebane());
        // Add more items as needed
    }

    private ActionResult onPlayerAttackEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (player instanceof ServerPlayerEntity && entity instanceof LivingEntity) {
            ItemStack weapon = player.getMainHandStack();

            if (weapon.isEmpty()) {
                // Fallback in case the player has no weapon
                ((LivingEntity) entity).damage(player.getDamageSources().playerAttack(player), 1.0f);
                return ActionResult.SUCCESS;
            }

            Item weaponItem = weapon.getItem();

            // Check if the weapon has an associated stats class
            Object stats = itemStatsMap.get(weaponItem);
            if (stats == null) {
                System.out.println("No stats found for weapon: " + weaponItem.toString());
                return ActionResult.PASS;
            }

            // Retrieve the player power
            BigInteger playerPower = powerManager.getPower(player.getName().getString());
            float playerPowerFloat = new BigDecimal(playerPower).floatValue();

            // Use the specific item stats class to calculate damage
            float baseDamage = 1.0f; // You can set this base damage as needed for the item
            float damage = calculateItemDamage(stats, baseDamage, playerPowerFloat);

            // Apply a slight random variation to the damage
            float finalDamage = applyDamageVariation(damage, 0.1f);

            // Apply the calculated damage to the entity
            ((LivingEntity) entity).damage(player.getDamageSources().playerAttack(player), finalDamage);

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    /**
     * Dynamically calculate damage using the specific item's stats class.
     * Formula: Damage = Base Damage + (alpha * player power)
     */
    private float calculateItemDamage(Object stats, float baseDamage, float playerPower) {
        if (stats instanceof BloodfireSpear) {
            return ((BloodfireSpear) stats).calculateDamage(baseDamage, playerPower);
        } else if (stats instanceof Gravebane) {
            return ((Gravebane) stats).calculateDamage(baseDamage, playerPower);
        }
        // Add more item types as needed
        return baseDamage; // Default fallback
    }

    private float applyDamageVariation(float baseDamage, float variation) {
        // Apply a random variation to the damage
        float minDamage = baseDamage * (1.0f - variation);
        float maxDamage = baseDamage * (1.0f + variation);
        return minDamage + (float) Math.random() * (maxDamage - minDamage);
    }
}
