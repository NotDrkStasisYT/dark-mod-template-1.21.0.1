package net.drk.summoning;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CurrencyManager {

    private static final File CURRENCY_FILE = new File("currency_data.txt");
    private static final String SEPARATOR = ":";

    private static Map<String, Integer> currencyMap = new HashMap<>();

    static {
        loadCurrencyData();
    }

    public static int getCurrency(String playerName) {
        return currencyMap.getOrDefault(playerName, 0);
    }

    public static boolean setCurrency(String playerName, int amount) {
        currencyMap.put(playerName, amount);
        saveCurrencyData();
        return false;
    }

    public static boolean canAfford(String playerName, int cost) {
        return getCurrency(playerName) >= cost;
    }

    public static void deductCurrency(String playerName, int amount) {
        int current = getCurrency(playerName);
        if (current >= amount) {
            setCurrency(playerName, current - amount);
        }
    }

    private static void loadCurrencyData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CURRENCY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length == 2) {
                    String playerName = parts[0];
                    int amount = Integer.parseInt(parts[1]);
                    currencyMap.put(playerName, amount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveCurrencyData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CURRENCY_FILE))) {
            for (Map.Entry<String, Integer> entry : currencyMap.entrySet()) {
                writer.write(entry.getKey() + SEPARATOR + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
