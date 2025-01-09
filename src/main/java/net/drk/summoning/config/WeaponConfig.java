package net.drk.summoning.config;

import java.util.HashMap;
import java.util.Map;

public class WeaponConfig {
    private static final Map<String, Integer> retrieveCosts = new HashMap<>();
    private static final Map<String, String> weaponRarities = new HashMap<>();
    private static final Map<String, String> weaponTypes = new HashMap<>(); // New map for weapon types

    static {
        // Example configuration, you can load this from a file or database
        retrieveCosts.put("gravebane", 10);
        retrieveCosts.put("bloodfire_spear", 20);
        retrieveCosts.put("nature_sword", 10);
        retrieveCosts.put("fire_sword", 10);
        retrieveCosts.put("eclipse_spear", 10);


        weaponRarities.put("gravebane", "COMMON");
        weaponRarities.put("bloodfire_spear", "RARE");
        weaponRarities.put("nature_sword", "UNCOMMON");
        weaponRarities.put("fire_sword", "RARE");
        weaponRarities.put("eclipse_spear", "LEGENDARY");



        // Example weapon types
        weaponTypes.put("gravebane", "SPEAR");
        weaponTypes.put("bloodfire_spear", "SPEAR");
        weaponTypes.put("nature_sword", "SWORD");
        weaponTypes.put("fire_sword", "SWORD");
        weaponTypes.put("eclipse_spear", "SWORD");

    }

    public static int getRetrieveCost(String weaponName) {
        return retrieveCosts.getOrDefault(weaponName, 0);
    }

    public static String getWeaponRarity(String weaponName) {
        return weaponRarities.getOrDefault(weaponName, "UNKNOWN");
    }

    // New method to get weapon type
    public static String getWeaponType(String weaponName) {
        return weaponTypes.getOrDefault(weaponName, "UNKNOWN");
    }
}
