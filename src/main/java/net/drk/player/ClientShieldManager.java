package net.drk.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ClientShieldManager {
    private static final String SHIELD_FILE = "client_player_shields.dat";
    private static final ClientShieldManager INSTANCE = new ClientShieldManager();

    // Store player shields and max shields
    private Map<String, BigInteger> playerShields = new HashMap<>();
    private Map<String, BigInteger> playerMaxShields = new HashMap<>();
    private Map<String, BigInteger> shieldRegenRates = new HashMap<>();

    private ClientShieldManager() {
        // Load shields from file
        loadShields();
    }

    public static ClientShieldManager getInstance() {
        return INSTANCE;
    }

    public BigInteger getShield(ClientPlayerEntity player) {
        return playerShields.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }

    public void setShield(ClientPlayerEntity player, BigInteger shieldAmount) {
        String playerName = player.getName().getString();
        BigInteger maxShield = getMaxShield(player);
        if (shieldAmount.compareTo(maxShield) > 0) {
            shieldAmount = maxShield;
        }
        playerShields.put(playerName, shieldAmount);
        saveShields();
    }

    public void setMaxShield(ClientPlayerEntity player, BigInteger maxShield) {
        String playerName = player.getName().getString();
        playerMaxShields.put(playerName, maxShield);
        // Update current shield if it exceeds the new max
        BigInteger currentShield = playerShields.getOrDefault(playerName, BigInteger.ZERO);
        if (currentShield.compareTo(maxShield) > 0) {
            playerShields.put(playerName, maxShield);
        }
        saveShields();
    }

    public BigInteger getMaxShield(ClientPlayerEntity player) {
        return playerMaxShields.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }

    public void setShieldRegenerationRate(ClientPlayerEntity player, BigInteger rate) {
        String playerName = player.getName().getString();
        shieldRegenRates.put(playerName, rate);
        saveShields();
    }

    public BigInteger getShieldRegenerationRate(ClientPlayerEntity player) {
        return shieldRegenRates.getOrDefault(player.getName().getString(), BigInteger.ZERO);
    }

    public void regenerateShield(ClientPlayerEntity player, BigInteger amount) {
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
}
