package net.drk.item.other.stats;

public class BloodfireSpear {
    private float alpha; // Track alpha

    public BloodfireSpear() {
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
        return baseDamage + (alpha * playerPower);
    }
}
