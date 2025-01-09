package net.drk.player;

import net.drk.A.ShieldNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ShieldManager {
    private static final String SHIELD_FILE = "player_shields.dat";
    private static final ShieldManager INSTANCE = new ShieldManager();

    // Store player shields and max shields
    private Map<String, BigInteger> playerShields = new HashMap<>();
    private Map<String, BigInteger> playerMaxShields = new HashMap<>();
    private Map<String, BigInteger> shieldRegenRates = new HashMap<>();

    private ShieldManager() {
        // Private constructor to enforce singleton pattern
        loadShields();
    }

    public static ShieldManager getInstance() {
        return INSTANCE;
    }

    public BigInteger getShield(ServerPlayerEntity player) {
        return playerShields.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }
    public BigInteger getClientShield(ClientPlayerEntity player) {
        return playerShields.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }

    public void setShield(ServerPlayerEntity player, BigInteger shieldAmount) {
        String playerName = player.getName().getString();
        BigInteger maxShield = getMaxShield(player);
        if (shieldAmount.compareTo(maxShield) > 0) {
            shieldAmount = maxShield;
        }
        playerShields.put(playerName, shieldAmount);
        saveShields();

        // Send updated shield data to the client
        ShieldNetworking.sendShieldDataToPlayer(player);
    }

    public void setMaxShield(ServerPlayerEntity player, BigInteger maxShield) {
        String playerName = player.getName().getString();
        playerMaxShields.put(playerName, maxShield);

        // Update current shield if it exceeds the new max
        BigInteger currentShield = playerShields.getOrDefault(playerName, BigInteger.ZERO);
        if (currentShield.compareTo(maxShield) > 0) {
            playerShields.put(playerName, maxShield);
        }
        saveShields();

        // Send updated shield data to the client
        ShieldNetworking.sendShieldDataToPlayer(player);
    }

    public BigInteger getMaxShield(ServerPlayerEntity player) {
        return playerMaxShields.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }
    public BigInteger getMaxClientShield(ClientPlayerEntity player) {
        return playerMaxShields.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }

    public void setShieldRegenerationRate(ServerPlayerEntity player, BigInteger rate) {
        String playerName = player.getName().getString();
        shieldRegenRates.put(playerName, rate);
        saveShields();
    }

    public BigInteger getShieldRegenerationRate(ServerPlayerEntity player) {
        return shieldRegenRates.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }

    public void regenerateShield(ServerPlayerEntity player, BigInteger amount) {
        BigInteger currentShield = getShield(player);
        BigInteger newShield = currentShield.add(amount);

        // Ensure shield does not exceed max shield
        BigInteger maxShield = getMaxShield(player);
        if (newShield.compareTo(maxShield) > 0) {
            newShield = maxShield;
        }

        setShield(player, newShield);
    }

    private void saveShields() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SHIELD_FILE))) {
            oos.writeObject(playerShields);
            oos.writeObject(playerMaxShields);
            oos.writeObject(shieldRegenRates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadShields() {
        File file = new File(SHIELD_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                playerShields = (Map<String, BigInteger>) ois.readObject();
                playerMaxShields = (Map<String, BigInteger>) ois.readObject();
                shieldRegenRates = (Map<String, BigInteger>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }









    public void updateClientShield(String playerName, BigInteger shield, BigInteger maxShield) {
        playerShields.put(playerName, shield);
        playerMaxShields.put(playerName, maxShield);



        System.out.println("Updated shield data for " + playerName + ": " + shield + "/" + maxShield);

    }


    public void onPlayerLogin(ServerPlayerEntity player) {
        ShieldNetworking.sendShieldDataToPlayer(player);
    }
}
