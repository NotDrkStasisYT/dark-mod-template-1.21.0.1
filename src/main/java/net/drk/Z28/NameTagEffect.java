package net.drk.Z28;

public abstract class NameTagEffect {
    protected String name;

    public NameTagEffect(String name) {
        this.name = name;
    }

    // Abstract method for applying the effect to a nickname
    public abstract String applyEffect(String nickname);

    // Method to get the effect's name
    public String getName() {
        return name;
    }
}
