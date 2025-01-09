package net.drk.summoning;

import java.io.*;
import java.util.*;

public class FileStorageManager {

    private final File storageFile;
    private final Map<String, Integer> playerItemCounts; // Track item count for each player

    public FileStorageManager(String fileName) {
        storageFile = new File(fileName);
        playerItemCounts = new HashMap<>();

        try {
            if (!storageFile.exists()) {
                storageFile.createNewFile();
            } else {
                loadPlayerItemCounts(); // Load existing counts from the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the item counts for each player from the file
    private void loadPlayerItemCounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    String playerName = parts[0];
                    int itemCount = Integer.parseInt(parts[3]); // Item count is the 4th element

                    // Update the item count for the player (store the highest count)
                    playerItemCounts.put(playerName, Math.max(playerItemCounts.getOrDefault(playerName, 0), itemCount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save an item name to the player's storage file
    public void addItemToStorage(String playerName, String itemName) {
        // Increment the player's item count or start at 1 if it's the first item
        int itemCount = playerItemCounts.getOrDefault(playerName, 0) + 1;
        playerItemCounts.put(playerName, itemCount); // Update count in map

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageFile, true))) {
            // Write player, mod, item name, and current item count
            writer.write(playerName + ":" + itemName + ":" + itemCount);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all items stored for a player
    public List<String> getItemsFromStorage(String playerName) {
        List<String> items = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(playerName + ":")) {
                    String[] parts = line.split(":");
                    if (parts.length >= 4) {
                        items.add(parts[2]); // Extract item name (3rd part of the line)
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    // Method to get a specific item by index (0-based index)
    public String getItemFromStorage(String playerName, int index) {
        List<String> items = getItemsFromStorage(playerName);
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }
}
