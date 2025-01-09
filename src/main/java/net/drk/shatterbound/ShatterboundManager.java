package net.drk.shatterbound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShatterboundManager {
    private static final int MAX_SHATTERBOUND_PARTNERS = 4; // Maximum number of players in the bond group
    private static final Map<String, Set<String>> shatterboundPairs = new HashMap<>();

    // Add a new Shatterbound relationship
    public static boolean addShatterbound(String newPlayer, String bondedPlayer) {
        if (canJoinShatterboundGroup(newPlayer, bondedPlayer)) {
            // Bond newPlayer with everyone in bondedPlayer's group
            for (String partner : shatterboundPairs.get(bondedPlayer)) {
                shatterboundPairs.computeIfAbsent(partner, k -> new HashSet<>()).add(newPlayer);
                shatterboundPairs.computeIfAbsent(newPlayer, k -> new HashSet<>()).add(partner);
            }
            // Finally, bond newPlayer and bondedPlayer themselves
            shatterboundPairs.computeIfAbsent(bondedPlayer, k -> new HashSet<>()).add(newPlayer);
            shatterboundPairs.computeIfAbsent(newPlayer, k -> new HashSet<>()).add(bondedPlayer);

            return true;
        }
        return false; // Either max group size exceeded or bonding conditions not met
    }

    // Remove a Shatterbound relationship
    public static boolean removeShatterbound(String player1, String player2) {
        if (areShatterbound(player1, player2)) {
            shatterboundPairs.getOrDefault(player1, new HashSet<>()).remove(player2);
            shatterboundPairs.getOrDefault(player2, new HashSet<>()).remove(player1);
            return true;
        }
        return false; // They were not Shatterbound
    }

    // Check if two players are Shatterbound
    public static boolean areShatterbound(String player1, String player2) {
        return shatterboundPairs.getOrDefault(player1, new HashSet<>()).contains(player2);
    }

    // Get a player's Shatterbound partners
    public static Set<String> getShatterboundPartners(String player) {
        return shatterboundPairs.getOrDefault(player, new HashSet<>());
    }

    // Check if a player can join a group based on the Shatterbound rules
    public static boolean canJoinShatterboundGroup(String newPlayer, String bondedPlayer) {
        Set<String> bondedGroup = getShatterboundPartners(bondedPlayer);

        // Check if the group size is already at the limit
        if (bondedGroup.size() >= MAX_SHATTERBOUND_PARTNERS - 1) {
            return false;
        }

        // Ensure the new player can bond with every member of the bonded player's group
        for (String partner : bondedGroup) {
            if (!areShatterbound(newPlayer, partner) && !canShatterbind(newPlayer)) {
                return false;
            }
        }

        return true;
    }

    // Check if a player can bond with others (i.e., has less than 4 bonds)
    public static boolean canShatterbind(String player) {
        return getShatterboundPartners(player).size() < MAX_SHATTERBOUND_PARTNERS;
    }

    // Remove all bonds when a player leaves or resets
    public static void removeAllShatterbonds(String player) {
        Set<String> partners = shatterboundPairs.remove(player);
        if (partners != null) {
            for (String partner : partners) {
                shatterboundPairs.getOrDefault(partner, new HashSet<>()).remove(player);
            }
        }
    }
}
