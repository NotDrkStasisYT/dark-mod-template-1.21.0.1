package net.drk.Z28;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChatMessageListener {

    public static void register() {
        // Register the ALLOW_CHAT_MESSAGE event listener
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, player, typeKey) -> {
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

                // Get the player's custom styled nickname
                String styledNickname = EffectManager.getStyledNickname(serverPlayer);

                // Create the new formatted message with the nickname
                Text formattedMessage = Text.literal(styledNickname + ": ").append(message.getContent().getString());

                // Broadcast the modified message (this won't cancel the original message, just allows modification)
                serverPlayer.getServer().getPlayerManager().broadcast(formattedMessage, false);

                return false;  // Cancel the original message, as we are broadcasting a new one
            }
            return true;  // Allow other messages
        });
    }
}
