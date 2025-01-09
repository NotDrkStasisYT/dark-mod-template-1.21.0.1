package net.drk;

import java.math.BigInteger;
import java.util.UUID;

public class Fae {
    private final UUID id; // Unique identifier for the Fae (e.g., player UUID)
    private String playerName; // Player's name
    private BigInteger shield; // Player's current shield value
    private BigInteger maxShield; // Player's maximum shield value
    private int health; // Player's health
    private int maxHealth; // Player's maximum health
    private int level; // Player's level
    private int experience; // Player's experience

    // Constructor to initialize the Fae object
    public Fae(UUID id, String playerName) {
        this.id = id;
        this.playerName = playerName;
        this.shield = BigInteger.ZERO; // Default to zero shield
        this.maxShield = BigInteger.ZERO; // Default to zero max shield
        this.health = 100; // Default health value
        this.maxHealth = 100; // Default max health
        this.level = 1; // Default level
        this.experience = 0; // Default experience
    }

    // Getter and setter methods
    public UUID getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public BigInteger getShield() {
        return shield;
    }

    public void setShield(BigInteger shield) {
        this.shield = shield;
    }

    public BigInteger getMaxShield() {
        return maxShield;
    }

    public void setMaxShield(BigInteger maxShield) {
        this.maxShield = maxShield;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Example method to level up
    public void levelUp() {
        this.level++;
        this.experience = 0; // Reset experience
        this.maxHealth += 10; // Increase max health on level-up
        this.health = this.maxHealth; // Restore health to max
    }

    // Example method to take damage
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // Example method to heal the Fae
    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    // Other methods like updating shield, power, or special abilities can be added here
}
