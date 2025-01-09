package net.drk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FaeManager {
    private final Map<UUID, Fae> faeMap = new HashMap<>();

    // Add a new Fae
    public void addFae(UUID playerId, Fae fae) {
        faeMap.put(playerId, fae);
    }

    // Get a Fae by player ID
    public Fae getFae(UUID playerId) {
        return faeMap.get(playerId);
    }

    // Remove a Fae
    public void removeFae(UUID playerId) {
        faeMap.remove(playerId);
    }
}
