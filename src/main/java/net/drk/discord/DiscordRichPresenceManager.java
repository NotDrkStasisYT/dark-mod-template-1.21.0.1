package net.drk.discord;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public class DiscordRichPresenceManager {

    private static final long CLIENT_ID = 1297340811226255392L;  // Your Discord client ID
    private static final Map<String, DiscordRichPresenceManager> playerPresenceMap = new HashMap<>(); // Map to track players' presence
    private IPCClient client;
    private String lastState = null;
    private String lastDetails = null;
    private String lastLargeImageKey = null; // Track last large image key
    private String lastSmallImageKey = null; // Track last small image key
    private String currentLargeImageKey = RichPresenceImage.IMAGE_ONE.getKey(); // Default large image
    private String currentSmallImageKey = "menu"; // Default small image

    public DiscordRichPresenceManager(String playerId) {
        client = new IPCClient(CLIENT_ID);
        playerPresenceMap.put(playerId, this);

        try {
            client.connect();
            System.out.println("Discord RPC connected for player: " + playerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePresence(String state, String details, String partyId, int currentSize, int maxSize) {
        // Check if the state, details, or image keys have changed
        if (state.equals(lastState) && details.equals(lastDetails) &&
                currentLargeImageKey.equals(lastLargeImageKey) && currentSmallImageKey.equals(lastSmallImageKey)) {
            System.out.println("No changes in presence, skipping update.");
            return; // Exit if no changes
        }

        // Update last state, details, and image keys
        lastState = state;
        lastDetails = details;
        lastLargeImageKey = currentLargeImageKey;
        lastSmallImageKey = currentSmallImageKey;

        System.out.println("Updating Rich Presence: " + state + " - " + details);

        RichPresence.Builder builder = new RichPresence.Builder()
                .setDetails(details)
                .setState(state)
                .setStartTimestamp(OffsetDateTime.now())
                .setLargeImage(currentLargeImageKey, "Shatterpoint")  // Use the current large image key
                .setSmallImage(currentSmallImageKey, "Status")     // Use the current small image key
                .setJoinSecret("sd")
                .setParty(partyId, PlayerStatus.getStreak(), 10);


        try {
            client.sendRichPresence(builder.build());
            System.out.println("Rich Presence updated: " + state + " - " + details);
        } catch (Exception e) {
            System.out.println("Failed to update Rich Presence: " + e.getMessage());
        }
    }

    public void setLargeImageKey(String key) {
        currentLargeImageKey = key;
        System.out.println("Large image changed to: " + key);
    }

    public void setSmallImageKey(String key) {
        currentSmallImageKey = key;
        System.out.println("Small image changed to: " + key);
    }

    public void clearPresence() {
        lastState = null;
        lastDetails = null;
        lastLargeImageKey = null;
        lastSmallImageKey = null;

        System.out.println("Clearing Rich Presence...");
        client.sendRichPresence(null);
        System.out.println("Rich Presence cleared.");
    }

    public void shutdown() {
        try {
            if (client != null) {
                client.close();
                System.out.println("Discord RPC closed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DiscordRichPresenceManager getManagerForPlayer(String playerId) {
        return playerPresenceMap.get(playerId);
    }
}
