package net.drk.summoning.config;

import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTable {
    private static class LootEntry {
        private final Identifier itemId; // Full identifier (namespace:item_name)
        private final int rarity;

        public LootEntry(Identifier itemId, int rarity) {
            this.itemId = itemId;
            this.rarity = rarity;
        }

        public Identifier getItemId() {
            return itemId;
        }

        public int getRarity() {
            return rarity;
        }
    }

    private final List<LootEntry> items = new ArrayList<>();
    private final Random random = new Random();
    private int totalRarityWeight = 0;

    // Loot table constructor automatically calls initialization
    public LootTable() {
        initializeLootTable();
    }

    // Method to add an item by full identifier (namespace:item_name) with rarity to the loot table
    public void addItem(Identifier itemId, int rarity) {
        items.add(new LootEntry(itemId, rarity));
        totalRarityWeight += rarity;
        System.out.println("Added item: " + itemId + " with rarity: " + rarity);
    }

    // Method to get a random item identifier from the loot table based on rarity
    public Identifier getRandomItemId() {
        if (items.isEmpty()) {
            System.out.println("Loot table is empty!");
            return null;
        }

        int randomWeight = random.nextInt(totalRarityWeight);
        int currentWeight = 0;

        for (LootEntry entry : items) {
            currentWeight += entry.getRarity();
            if (randomWeight < currentWeight) {
                return entry.getItemId();
            }
        }

        System.out.println("Error in loot table item selection.");
        return null;
    }

    // Initialize the loot table with custom items
    public void initializeLootTable() {
        System.out.println("Initializing loot table with custom items...");
        addItem(Identifier.of("drkmod", "bloodfire_spear"), 5); // Example items with full identifiers
        addItem(Identifier.of("drkmod", "gravebane"), 100);
        addItem(Identifier.of("drkmod", "nature_sword"), 5);
        addItem(Identifier.of("drkmod", "fire_sword"), 30);
        addItem(Identifier.of("drkmod", "eclipse_spear"), 1);


        System.out.println("Loot table initialized. Total items: " + items.size());
        System.out.println("Total rarity weight: " + totalRarityWeight);
    }
}
