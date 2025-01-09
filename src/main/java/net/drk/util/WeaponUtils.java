package net.drk.util;

import java.util.HashMap;
import java.util.Map;

public class WeaponUtils {

    // Maps to store cooldowns and damages for various weapons
    private static final Map<String, Integer> WEAPON_COOLDOWNS = new HashMap<>();
    private static final Map<String, Integer> WEAPON_DAMAGES = new HashMap<>();

    private static final Map<String, Integer> WEAPON_LOG_COOLDOWNS = new HashMap<>();
    private static final Map<String, Integer> WEAPON_LOG_DAMAGES = new HashMap<>();


    static {
        // Set cooldowns in milliseconds for different weapons
        WEAPON_COOLDOWNS.put("item.minecraft.netherite_sword", 1000); // 1 second
        WEAPON_COOLDOWNS.put("item.minecraft.wooden_sword", 2500);  // 2.5 seconds
        WEAPON_COOLDOWNS.put("item.drkmod.eclipse_spear", 800);  // 0.8 seconds

        WEAPON_LOG_COOLDOWNS.put("item.minecraft.netherite_sword", 800);
        WEAPON_LOG_COOLDOWNS.put("item.minecraft.diamond_sword", 1500);
        WEAPON_LOG_COOLDOWNS.put("item.drkmod.gravebane", 2500);



        // Set damage for different weapons
        WEAPON_DAMAGES.put("item.minecraft.netherite_sword", 5);
        WEAPON_DAMAGES.put("item.minecraft.wooden_sword", 1);
        WEAPON_DAMAGES.put("item.drkmod.eclipse_spear", 9);

        WEAPON_LOG_DAMAGES.put("item.minecraft.netherite_sword", 5);
        WEAPON_LOG_DAMAGES.put("item.minecraft.diamond_sword", 200);
        WEAPON_LOG_DAMAGES.put("item.drkmod.gravebane", 7);



    }

    /**
     * Gets the cooldown in milliseconds for a weapon.
     * @param itemKey The translation key of the item.
     * @return The cooldown in milliseconds.
     */
    public static int getCooldownForWeapon(String itemKey) {
        return WEAPON_COOLDOWNS.getOrDefault(itemKey, 0); // Default 0 if not found
    }
    public static int getCooldownForLogWeapon(String itemKey) {
        return WEAPON_LOG_COOLDOWNS.getOrDefault(itemKey, 0); // Default 0 if not found
    }

    /**
     * Gets the damage value for a weapon.
     * @param itemKey The translation key of the item.
     * @return The damage value.
     */
    public static int getDamageForLogWeapon(String itemKey) {
        return WEAPON_LOG_DAMAGES.getOrDefault(itemKey, 0); // Default 0 if not found
    }

    public static int getDamageForWeapon(String itemKey) {
        return WEAPON_DAMAGES.getOrDefault(itemKey, 0); // Default 0 if not found
    }

}
