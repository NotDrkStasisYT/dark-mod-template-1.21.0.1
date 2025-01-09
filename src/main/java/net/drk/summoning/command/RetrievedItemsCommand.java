package net.drk.summoning.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.drk.item.ModItems;
import net.drk.summoning.CurrencyManager;
import net.drk.summoning.config.WeaponConfig;
import net.drk.summoning.storage.ItemFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.item.ItemStack;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.*;

public class RetrievedItemsCommand {

    private static final String RETRIEVED_ITEMS_FILE = "retrieved_items.txt"; // File to store retrieved items

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .then(CommandManager.literal("retrieved_items")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    String playerName = source.getPlayer().getName().getString();

                                    List<String> retrievedItems = getRetrievedItemsForPlayer(playerName);

                                    if (retrievedItems.isEmpty()) {
                                        source.sendFeedback(
                                                () -> Text.literal("§cYou have not retrieved any items.").formatted(Formatting.RED),
                                                false
                                        );
                                    } else {
                                        source.sendFeedback(
                                                () -> Text.literal("§6--- Retrieved Items ---").formatted(Formatting.GOLD),
                                                false
                                        );

                                        for (int i = 0; i < retrievedItems.size(); i++) {
                                            String itemName = retrievedItems.get(i);
                                            int finalI = i;
                                            source.sendFeedback(
                                                    () -> Text.literal(String.format("§7%d. %s", finalI + 1, itemName)).formatted(Formatting.GRAY),
                                                    false
                                            );
                                        }
                                    }

                                    return 1;
                                })
                        )
        );
    }

    // Method to get the list of retrieved items for a given player
    private static List<String> getRetrievedItemsForPlayer(String playerName) {
        List<String> retrievedItems = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(RETRIEVED_ITEMS_FILE));
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(playerName)) {
                    int retrievedIndex = Integer.parseInt(parts[1]);
                    String itemName = getStoredItemsForPlayer(playerName).get(retrievedIndex);
                    retrievedItems.add(itemName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retrievedItems;
    }

    // Method to read the stored items from a file for a given player
    private static List<String> getStoredItemsForPlayer(String playerName) {
        List<String> itemNames = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("loot_items.txt"));
            for (String line : lines) {
                if (line.startsWith(playerName)) {
                    String itemName = line.split(":")[2];
                    itemNames.add(itemName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemNames;
    }
}
