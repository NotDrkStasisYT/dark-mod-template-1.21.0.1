package net.drk.zodiac;

import net.drk.util.IEntityDataSaver;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ZodiacManager {
    private static final String ZODIAC_TAG = "ZodiacSign";

    // List of all available zodiac signs
    public enum ZodiacSign {
        ARIES, TAURUS, GEMINI, CANCER, LEO, VIRGO, LIBRA, SCORPIO, SAGITTARIUS, CAPRICORN, AQUARIUS, PISCES;
    }

    // Method to set a player's zodiac sign and store it in NBT data
    public static void setPlayerZodiac(ServerPlayerEntity player, ZodiacSign sign) {
        // Save zodiac sign to the player's persistent NBT data
        NbtCompound nbt = ((IEntityDataSaver) player).getPersistentData();
        nbt.putString(ZODIAC_TAG, sign.name());
        player.sendMessage(Text.literal("Your zodiac sign has been set to " + sign.name() + "!").formatted(Formatting.GOLD), false);
    }

    // Method to get a player's zodiac sign from NBT data
    public static ZodiacSign getPlayerZodiac(ServerPlayerEntity player) {
        NbtCompound nbt = ((IEntityDataSaver) player).getPersistentData();
        if (nbt.contains(ZODIAC_TAG)) {
            try {
                return ZodiacSign.valueOf(nbt.getString(ZODIAC_TAG));
            } catch (IllegalArgumentException e) {
                // Handle invalid sign data
                return null;
            }
        }
        return null;
    }

    // Method to check if a player already has a zodiac sign
    public static boolean hasZodiac(ServerPlayerEntity player) {
        NbtCompound nbt = ((IEntityDataSaver) player).getPersistentData();
        return nbt.contains(ZODIAC_TAG);
    }

    // Method to remove a player's zodiac sign and clear it from NBT
    public static void removePlayerZodiac(ServerPlayerEntity player) {
        if (hasZodiac(player)) {
            NbtCompound nbt = ((IEntityDataSaver) player).getPersistentData();
            nbt.remove(ZODIAC_TAG); // Remove the zodiac sign from the player's data
            player.sendMessage(Text.literal("Your zodiac sign has been removed.").formatted(Formatting.RED), false);
        } else {
            player.sendMessage(Text.literal("You do not have a zodiac sign set.").formatted(Formatting.RED), false);
        }
    }
}
