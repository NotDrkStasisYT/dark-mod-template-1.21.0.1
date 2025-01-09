package net.drk.other.stun;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class StunState {
    private boolean stunned;
    private long stunEndTime; // Time when stun ends in milliseconds

    // Constructor
    public StunState() {
        this.stunned = false;
        this.stunEndTime = 0;
    }

    // Check if the entity is currently stunned
    public boolean isStunned() {
        return stunned;
    }

    // Stun the entity for a given duration (in ticks)
    public void stun(long durationTicks) {
        this.stunned = true;
        this.stunEndTime = System.currentTimeMillis() + (durationTicks * 50); // Convert ticks to milliseconds
    }

    // Check if the stun duration has ended
    public void updateStunState() {
        if (stunned && System.currentTimeMillis() >= stunEndTime) {
            stunned = false;
        }
    }

    // Serialize stun state to NBT
    public void writeNbt(NbtCompound nbt) {
        NbtCompound stunNbt = new NbtCompound();
        stunNbt.putBoolean("stunned", this.stunned);
        stunNbt.putLong("stunEndTime", this.stunEndTime);
        nbt.put("StunState", stunNbt);
    }

    // Deserialize stun state from NBT
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("StunState", 10)) {
            NbtCompound stunNbt = nbt.getCompound("StunState");
            this.stunned = stunNbt.getBoolean("stunned");
            this.stunEndTime = stunNbt.getLong("stunEndTime");
        }
    }

    // Disable actions (movement, jumping, and attacking) while stunned
    public void disableEntityActions(LivingEntity entity) {
        if (stunned) {
            entity.setVelocity(0, 0, 0); // Prevent movement
            entity.velocityModified = true; // Stop entity from being knocked back

            if (entity instanceof PlayerEntity player) {
                player.getAbilities().setWalkSpeed(0);
                player.getAbilities().setFlySpeed(0);
                player.sendAbilitiesUpdate();
            }
        }
    }

    // Re-enable actions after stun ends
    public void enableEntityActions(LivingEntity entity) {
        if (!stunned) {
            if (entity instanceof PlayerEntity player) {
                player.getAbilities().setWalkSpeed(0);
                player.getAbilities().setFlySpeed(0);
                player.sendAbilitiesUpdate();
            }
        }
    }
}
