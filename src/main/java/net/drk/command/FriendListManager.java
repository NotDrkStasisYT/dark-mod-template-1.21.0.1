package net.drk.command;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class FriendListManager {

    private static final HashMap<UUID, Set<UUID>> friendsMap = new HashMap<>();
    private static final HashMap<UUID, Set<UUID>> pendingRequests = new HashMap<>();
    public static MinecraftServer server; // Reference to the Minecraft server

    public static void setServer(MinecraftServer server) {
        FriendListManager.server = server;
    }

    // Add a friend
    public static void addFriend(UUID playerUUID, UUID friendUUID) {
        friendsMap.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(friendUUID);
        friendsMap.computeIfAbsent(friendUUID, k -> new HashSet<>()).add(playerUUID);
    }

    // Remove a friend
    public static void removeFriend(UUID playerUUID, UUID friendUUID) {
        Set<UUID> friends = friendsMap.get(playerUUID);
        if (friends != null) {
            friends.remove(friendUUID);
        }
        friends = friendsMap.get(friendUUID);
        if (friends != null) {
            friends.remove(playerUUID);
        }
    }

    // Check if someone is a friend
    public static boolean isFriend(UUID playerUUID, UUID friendUUID) {
        Set<UUID> friends = friendsMap.get(playerUUID);
        return friends != null && friends.contains(friendUUID);
    }

    // Send a friend request
    public static void sendFriendRequest(UUID playerUUID, UUID targetUUID) {
        pendingRequests.computeIfAbsent(targetUUID, k -> new HashSet<>()).add(playerUUID);
    }

    // Accept a friend request
    public static boolean acceptFriendRequest(UUID playerUUID, UUID requesterUUID) {
        Set<UUID> requests = pendingRequests.get(playerUUID);
        if (requests != null && requests.remove(requesterUUID)) {
            addFriend(playerUUID, requesterUUID);
            return true;
        }
        return false;
    }

    // Check if a friend request is pending
    public static boolean hasPendingRequest(UUID playerUUID, UUID requesterUUID) {
        Set<UUID> requests = pendingRequests.get(playerUUID);
        return requests != null && requests.contains(requesterUUID);
    }

    // Get all friends of a player
    public static Set<UUID> getFriends(UUID playerUUID) {
        return friendsMap.getOrDefault(playerUUID, new HashSet<>());
    }

    // Get all friends' names of a player
    public static List<String> getFriendsList(UUID playerUUID) {
        Set<UUID> friends = getFriends(playerUUID);
        return friends.stream()
                .map(uuid -> {
                    ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
                    return player != null ? player.getName().getString() : null;
                })
                .filter(name -> name != null)
                .collect(Collectors.toList());
    }
    public static int countOnlineFriends(UUID playerUUID) {
        Set<UUID> friends = getFriends(playerUUID);
        int onlineFriends = 0;
        for (UUID friendUUID : friends) {
            ServerPlayerEntity friend = server.getPlayerManager().getPlayer(friendUUID);
            if (friend != null) { // If the player object is not null, the friend is online
                onlineFriends++;
            }
        }
        return onlineFriends;
    }
    // FriendListManager.java

    public static BigDecimal getLuminMultiplier(ServerPlayerEntity player) {
        int onlineFriendsCount = countOnlineFriends(player.getUuid());
        BigDecimal multiplier = BigDecimal.ONE; // Start with a base multiplier of 1

        // Example multipliers based on the number of online friends
        if (onlineFriendsCount >= 1) {
            multiplier = multiplier.add(BigDecimal.valueOf(2)); // +0.5 for 1 friend
        }
        if (onlineFriendsCount >= 2) {
            multiplier = multiplier.add(BigDecimal.valueOf(5)); // +0.5 for 2 friends
        }
        // Add more conditions for more friends if needed

        return multiplier; // Returns the calculated multiplier
    }

}
