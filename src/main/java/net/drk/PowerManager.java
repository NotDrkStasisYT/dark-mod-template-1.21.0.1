package net.drk;

import net.drk.number.NumberFormatter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Box;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PowerManager {
    private static final String POWER_FILE = "player_power.dat";
    private Map<String, BigInteger> playerPowers = new HashMap<>();

    public PowerManager() {
        loadPowers();
    }

    public void addPower(String playerName, BigInteger amount) {
        playerPowers.put(playerName, playerPowers.getOrDefault(playerName, BigInteger.ZERO).add(amount));
        savePowers();
    }

    public void removePower(String playerName, BigInteger amount) {
        playerPowers.put(playerName, playerPowers.getOrDefault(playerName, BigInteger.ZERO).subtract(amount).max(BigInteger.ZERO));
        savePowers();
    }

    public BigInteger getPower(String playerName) {
        return playerPowers.getOrDefault(playerName, BigInteger.ZERO);
    }

    private void savePowers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(POWER_FILE))) {
            oos.writeObject(playerPowers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadPowers() {
        File file = new File(POWER_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                playerPowers = (Map<String, BigInteger>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String formatBigNumber(BigInteger number) {
        if (number.compareTo(BigInteger.valueOf(1_000_000_000)) >= 0) {
            return formatWithSuffix(number, BigInteger.valueOf(1_000_000_000), "B");
        } else if (number.compareTo(BigInteger.valueOf(1_000_000)) >= 0) {
            return formatWithSuffix(number, BigInteger.valueOf(1_000_000), "M");
        } else if (number.compareTo(BigInteger.valueOf(1_000)) >= 0) {
            return formatWithSuffix(number, BigInteger.valueOf(1_000), "K");
        } else {
            return number.toString();
        }
    }

    private String formatWithSuffix(BigInteger number, BigInteger divisor, String suffix) {
        BigDecimal decimal = new BigDecimal(number).divide(new BigDecimal(divisor), 2, BigDecimal.ROUND_HALF_UP);
        return decimal.toPlainString() + suffix;
    }

    public void setPowerForMobsInRadius(ServerWorld world, BlockPos center, double radius, BigInteger power) {
        // Create a box around the center position with the specified radius
        Box box = new Box(center).expand(radius);

        // Get all LivingEntities within the box
        List<LivingEntity> mobs = world.getEntitiesByClass(LivingEntity.class, box, entity -> true);

        for (LivingEntity mob : mobs) {
            if (mob.getPos().isInRange(new Vec3d(center.getX(), center.getY(), center.getZ()), radius)) {
                setMobPower(mob, power);
            }
        }
    }

    private void setMobPower(LivingEntity mob, BigInteger power) {
        // Implement logic to set power for the mob
        // For example, you might store power in a custom attribute or tag
    }


    public void applyDamageBasedOnPowerBloodfireSpear(LivingEntity entity, String playerName) {
        // Get the power for the player who used the item
        BigInteger power = getPower(playerName);

        // Define a scaling factor for converting power to damage
        BigInteger scalingFactor = BigInteger.valueOf(10); // Adjust as needed

        // Calculate the damage value based on power
        BigInteger scaledPower = power.multiply(scalingFactor);

        // Debug statement to verify the scaled power
        System.out.println("Debug: Player power: " + power + ", Scaled power: " + scaledPower);

        // Ensure damage is at least 1 to avoid zero or negative damage
        BigInteger minimumDamage = BigInteger.ONE;
        if (scaledPower.compareTo(minimumDamage) < 0) {
            scaledPower = minimumDamage;
        }

        // Convert BigInteger damage to float for Minecraft's damage system
        float damage = scaledPower.floatValue();

        // Debug statement to verify the final damage
        System.out.println("Debug: Final damage: " + damage);

        // Apply the damage to the entity
        entity.damage(entity.getDamageSources().magic(), damage);

        // Optionally log the damage value for debugging
        System.out.println("Applied damage: " + damage + " based on player power: " + power);
    }
    public void applyDamageBasedOnPowerBloodfireSpearAbility1(LivingEntity entity, String playerName) {
        // Get the power for the player who used the item
        BigInteger power = getPower(playerName);

        // Define a scaling factor for converting power to damage
        BigInteger scalingFactor = BigInteger.valueOf(5); // Adjust as needed for this specific ability

        // Calculate the damage value based on power
        BigInteger scaledPower = power.multiply(scalingFactor);

        // Debug statement to verify the scaled power
        System.out.println("Debug: Player power: " + power + ", Scaled power: " + scaledPower);

        // Ensure damage is at least 1 to avoid zero or negative damage
        BigInteger minimumDamage = BigInteger.ONE;
        if (scaledPower.compareTo(minimumDamage) < 0) {
            scaledPower = minimumDamage;
        }

        // Convert BigInteger damage to float for Minecraft's damage system
        float damage = scaledPower.floatValue();

        // Debug statement to verify the final damage
        System.out.println("Debug: Final damage: " + damage);

        // Apply the damage to the entity
        entity.damage(entity.getDamageSources().magic(), damage);

        // Optionally log the damage value for debugging
        System.out.println("Applied damage: " + damage + " based on player power: " + power);
    }

    public void applyDamageBasedOnPowerGravebane(LivingEntity entity, String playerName) {
        // Get the power for the player who used the item
        BigInteger power = getPower(playerName);

        // Define a scaling factor for converting power to damage
        BigInteger scalingFactor = BigInteger.valueOf(1); // Adjust as needed

        // Calculate the damage value based on power
        BigInteger scaledPower = power.multiply(scalingFactor);

        // Debug statement to verify the scaled power
        System.out.println("Debug: Player power: " + power + ", Scaled power: " + scaledPower);

        // Ensure damage is at least 1 to avoid zero or negative damage
        BigInteger minimumDamage = BigInteger.ONE;
        if (scaledPower.compareTo(minimumDamage) < 0) {
            scaledPower = minimumDamage;
        }

        // Convert BigInteger damage to float for Minecraft's damage system
        float damage = scaledPower.floatValue();

        // Debug statement to verify the final damage
        System.out.println("Debug: Final damage: " + damage);

        // Apply the damage to the entity
        entity.damage(entity.getDamageSources().magic(), damage);

        // Optionally log the damage value for debugging
        System.out.println("Applied damage: " + damage + " based on player power: " + power);
    }
}