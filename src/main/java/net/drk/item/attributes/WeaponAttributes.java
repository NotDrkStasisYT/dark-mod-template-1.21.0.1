package net.drk.item.attributes;

public class WeaponAttributes {
    private final String name;
    private final float baseDamage;
    private final float alpha;

    public WeaponAttributes(String name, float baseDamage, float alpha) {
        this.name = name;
        this.baseDamage = baseDamage;
        this.alpha = alpha;
    }

    public String getName() {
        return name;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public float getAlpha() {
        return alpha;
    }
}
