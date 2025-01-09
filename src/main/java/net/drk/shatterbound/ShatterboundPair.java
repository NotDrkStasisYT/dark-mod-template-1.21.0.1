package net.drk.shatterbound;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ShatterboundPair {
    private final ServerPlayerEntity player1;
    private final ServerPlayerEntity player2;
    private float synergyLevel;
    private long lastSynergyIncrease;

    public ShatterboundPair(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.synergyLevel = 0;
        this.lastSynergyIncrease = System.currentTimeMillis();
    }

    public boolean contains(ServerPlayerEntity player) {
        return player.equals(player1) || player.equals(player2);
    }

    public void applyProximityBuffs() {
        if (areClose()) {
            // Apply buffs, increase synergy level
            increaseSynergy();
            float distance = (float) player1.getPos().distanceTo(player2.getPos());
            applyBuffs(distance);
        } else {
            // Decay synergy if too far apart
            decaySynergy();
        }
    }

    private boolean areClose() {
        Vec3d pos1 = player1.getPos();
        Vec3d pos2 = player2.getPos();
        return pos1.isInRange(pos2, 15); // Set range for buffs
    }

    private void increaseSynergy() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSynergyIncrease >= 60000) { // Increase every 1 minute together
            synergyLevel = Math.min(synergyLevel + 1, 3); // Cap synergy at level 3
            lastSynergyIncrease = currentTime;
        }
    }

    private void decaySynergy() {
        if (synergyLevel > 0) {
            synergyLevel -= 0.01f; // Slow decay if apart
        }
    }

    private void applyBuffs(float distance) {
        if (distance <= 5) {
            // High-level buffs
            player1.addExperience(1);  // Example buffs
            player2.addExperience(1);
        } else if (distance <= 10) {
            // Medium-level buffs
            player1.heal(1.0F);
            player2.heal(1.0F);
        } else if (distance <= 15) {
            // Low-level buffs
            player1.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0));
            player2.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0));
        }
    }

    public void transferDamage(float damage) {
        // Share damage based on synergy level
        float sharedDamage = damage * (0.1f * synergyLevel);
       // player1.damage(DamageSource.MAGIC, sharedDamage);
        //player2.damage(DamageSource.MAGIC, sharedDamage);
    }

    public float getSynergyLevel() {
        return synergyLevel;
    }
}
