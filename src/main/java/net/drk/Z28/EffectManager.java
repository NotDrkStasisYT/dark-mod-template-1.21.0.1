package net.drk.Z28;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class EffectManager {
    private static final Map<String, NameTagEffect> effects = new HashMap<>();
    private static final Map<ServerPlayerEntity, NameTagEffect> playerEffects = new HashMap<>();

    // Register an effect
    public static void registerEffect(NameTagEffect effect) {
        effects.put(effect.getName(), effect);
    }

    // Check if an effect exists
    public static boolean hasEffect(String effectName) {
        return effects.containsKey(effectName);
    }

    // Apply an effect to a player's nickname
    public static void applyEffectToPlayer(ServerPlayerEntity player, String effectName) {
        NameTagEffect effect = effects.get(effectName);
        if (effect != null) {
            playerEffects.put(player, effect);
        }
    }

    // Method to get the player's styled nickname
    public static String getStyledNickname(ServerPlayerEntity player) {
        // Retrieve the player's custom nickname from ChangeNicknameCommand
        String baseNickname = ChangeNicknameCommand.getNickname(player);

        // Apply any active effect to the nickname
        NameTagEffect effect = playerEffects.get(player);
        if (effect != null) {
            return effect.applyEffect(baseNickname);
        }
        return baseNickname;
    }
    public static String getStyledNickname(ClientPlayerEntity player) {
        String baseNickname = player.getName().getString();  // ClientPlayerEntity gets its name this way
        NameTagEffect effect = playerEffects.get(player.getUuid());  // Fetch effect using UUID for the client
        if (effect != null) {
            return effect.applyEffect(baseNickname);
        }
        return baseNickname;
    }
}
