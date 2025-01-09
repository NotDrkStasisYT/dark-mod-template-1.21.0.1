package net.drk.summoning.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.drk.summoning.CurrencyManager;
import net.drk.summoning.FileStorageManager;
import net.drk.summoning.config.LootTable;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GetLootCommand {
    private static final LootTable lootTable = new LootTable();
    private static final int SUMMON_COST = 1; // Cost in currency to summon an item
    private static final FileStorageManager storageManager = new FileStorageManager("loot_items.txt");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("summon")
                                .executes(GetLootCommand::getLootItem)));
    }

    private static int getLootItem(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        String playerName = player.getName().getString();

        // Check if the player has enough currency
        if (CurrencyManager.canAfford(playerName, SUMMON_COST)) {
            // Deduct the currency
            CurrencyManager.deductCurrency(playerName, SUMMON_COST);

            // Get the random item identifier from the loot table
            Identifier randomItemId = lootTable.getRandomItemId();
            if (randomItemId != null) {
                // Log the item ID for debugging
                System.out.println("Random item ID: " + randomItemId);

                // Add the item to the player's storage with item count tracking
                try {
                    storageManager.addItemToStorage(playerName, randomItemId.toString());
                    source.sendFeedback(() -> Text.literal("Item added to storage: " + randomItemId), false);
                } catch (Exception e) {
                    e.printStackTrace();
                    source.sendError(Text.literal("An error occurred while adding the item to storage."));
                }
            } else {
                source.sendError(Text.literal("Loot table is empty!"));
            }
        } else {
            source.sendError(Text.literal("§cYou do not have enough currency to summon an item."));
        }

        return 1;
    }}
