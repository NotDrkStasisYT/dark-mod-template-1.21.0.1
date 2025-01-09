package net.drk.item.other.stats;

public class Gravebane {
    private float alpha; // Track alpha

    public Gravebane() {
        this.alpha = 1.0f; // Default alpha
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void increaseAlpha(float value) {
        this.alpha += value;
    }

    public float getAlpha() {
        return alpha;
    }

    public float calculateDamage(float baseDamage, float playerPower) {
        // Damage = Base Damage + (alpha * Player Power)
        return baseDamage + (alpha * playerPower);
    }
}
