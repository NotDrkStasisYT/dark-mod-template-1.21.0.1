package net.drk;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PlayerdataManager {
    private static final File DATA_FILE = new File("player_data.txt");
    private static final Map<String, PlayerData> playerDataMap = new HashMap<>();
    public static void loadPlayerData() {
        if (DATA_FILE.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String playerName = parts[0];
                        BigInteger luminAmount = new BigInteger(parts[1]);
                        PlayerData playerData = new PlayerData(luminAmount);
                        playerDataMap.put(playerName, playerData);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void savePlayerData(String playerName, PlayerData playerData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(playerName + ":" + playerData.getLuminCurrency().toString() + "\n");  // Fix: Access lumin currency from playerData, not player
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData getPlayerData(String playerName) {
        return playerDataMap.getOrDefault(playerName, new PlayerData(BigInteger.ZERO));
    }

    public static void setPlayerData(String playerName, PlayerData playerData) {
        playerDataMap.put(playerName, playerData);
        savePlayerData(playerName, playerData);
    }
}
