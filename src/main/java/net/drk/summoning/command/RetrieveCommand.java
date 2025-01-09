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

public class RetrieveCommand {

    static final int ITEMS_PER_PAGE = 5; // Number of items per page
    private static final Map<String, Set<Integer>> retrievedItemsMap = new HashMap<>(); // Player -> Retrieved Indexes Set
    private static final String RETRIEVED_ITEMS_FILE = "retrieved_items.txt"; // File to store retrieved items

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .then(CommandManager.literal("summon")
                                .then(CommandManager.literal("storage")
                                        .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                .executes(context -> {
                                                    int page = IntegerArgumentType.getInteger(context, "page");
                                                    ServerCommandSource source = context.getSource();
                                                    String playerName = source.getPlayer().getName().getString();

                                                    System.out.println("[RetrieveCommand] Requested page: " + page + " for player: " + playerName);


                                                    List<String> itemNames = getStoredItemsForPlayer(playerName);
                                                    Set<Integer> retrievedIndexes = getRetrievedIndexesForPlayer(playerName);
                                                    int totalPages = (int) Math.ceil((double) itemNames.size() / ITEMS_PER_PAGE);

                                                    System.out.println("[RetrieveCommand] Total pages: " + totalPages);


                                                    // If the requested page exceeds the total pages, show an error
                                                    if (page > totalPages) {
                                                        source.sendFeedback(
                                                                () -> Text.literal("§cPage number exceeds total pages.").formatted(Formatting.RED),
                                                                false
                                                        );
                                                        return 1;
                                                    }

                                                    // Display header
                                                    source.sendFeedback(
                                                            () -> Text.literal("§6--- Stored Items (Page " + page + "/" + totalPages + ") ---").formatted(Formatting.GOLD),
                                                            false
                                                    );

                                                    int startIndex = (page - 1) * ITEMS_PER_PAGE;
                                                    int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, itemNames.size());

                                                    System.out.println("[RetrieveCommand] Displaying items from index " + startIndex + " to " + endIndex);



                                                    for (int i = startIndex; i < endIndex; i++) {
                                                        String itemName = itemNames.get(i);
                                                        int itemNumber = i + 1; // Adjust item number based on overall index
                                                        String rarity = WeaponConfig.getWeaponRarity(itemName);
                                                        Formatting numberColor = getRarityColor(rarity);
                                                        Formatting itemColor = retrievedIndexes.contains(i + 1  ) ? Formatting.GRAY : Formatting.WHITE;
                                                        if (retrievedIndexes.contains(i + 1  )) {
                                                            System.out.println(retrievedIndexes.contains(i + 1));
                                                            System.out.println(i);

                                                            System.out.println("HELLODSODOOSDOOD");
                                                        }

                                                        source.sendFeedback(
                                                                () -> Text.literal(numberColor + String.format("%d. ", itemNumber)).formatted(Formatting.GRAY)
                                                                        .append(Text.literal(itemName).formatted(itemColor)),
                                                                false
                                                        );
                                                    }

                                                    return 1;
                                                })
                                        )
                                        .then(CommandManager.literal("retrieve")
                                                .then(CommandManager.argument("index", IntegerArgumentType.integer(1))
                                                        .executes(context -> {

                                                            int index = IntegerArgumentType.getInteger(context, "index") - 1; // Adjust for 0-based index
                                                            ServerCommandSource source = context.getSource();
                                                            String playerName = source.getPlayer().getName().getString();

                                                            System.out.println("[RetrieveCommand] Retrieval request index: " + index + " for player: " + playerName);
                                                            System.out.println("HELLOISJDISDFJISDJFIAJHDSISHDSUFHUSHFUSIUHFSHUFU");


                                                            List<String> itemNames = getStoredItemsForPlayer(playerName);

                                                            // Check if the index is valid
                                                            if (index >= 0 && index < itemNames.size()) {
                                                                String itemName = itemNames.get(index);
                                                                int retrieveCost = WeaponConfig.getRetrieveCost(itemName);

                                                                System.out.println("[RetrieveCommand] Item to retrieve: " + itemName + " with cost: " + retrieveCost);
                                                                for (int i = 0; i < itemNames.size(); i++) {
                                                                    if (index == i) {
                                                                        System.out.println("HEOKFODF");
                                                                    }
                                                                }
                                                                if (CurrencyManager.canAfford(playerName, retrieveCost)) {
                                                                    if (hasNotRetrievedItemBefore(playerName, index)) { // Check if item has not been retrieved before
                                                                        CurrencyManager.deductCurrency(playerName, retrieveCost);
                                                                        ItemStack itemStack = createItemFromName(itemName);

                                                                        if (!itemStack.isEmpty()) {
                                                                            // Give the item to the player
                                                                            boolean success = source.getPlayer().giveItemStack(itemStack);
                                                                            if (success) {
                                                                                addRetrievedIndexForPlayer(playerName, index); // Mark item as retrieved
                                                                                source.sendFeedback(
                                                                                        () -> Text.literal("§aItem retrieved from storage: ").formatted(Formatting.GREEN)
                                                                                                .append(Text.literal(itemName).formatted(Formatting.WHITE)),
                                                                                        false
                                                                                );

                                                                                System.out.println("[RetrieveCommand] Successfully retrieved item: " + itemName);

                                                                                List<String> storedItems = getStoredItemsForPlayer(playerName);
                                                                                Set<Integer> retrievedIndexes = getRetrievedIndexesForPlayer(playerName);
                                                                                updateIndexesAfterDeletion(storedItems, retrievedIndexes);
                                                                            } else {
                                                                                source.sendError(Text.literal("§cFailed to give item to the player."));

                                                                                System.err.println("[RetrieveCommand] Failed to give item to player: " + itemName);

                                                                            }
                                                                        } else {
                                                                            source.sendError(Text.literal("§cError creating item from name: " + itemName));
                                                                            System.err.println("[RetrieveCommand] Error creating item from name: " + itemName);

                                                                        }
                                                                    } else {
                                                                        source.sendError(Text.literal("§cCannot retrieve this item more than once."));
                                                                        System.out.println("[RetrieveCommand] Item already retrieved: " + itemName);

                                                                    }
                                                                } else {
                                                                    source.sendError(Text.literal("§cYou do not have enough currency."));

                                                                    System.out.println("[RetrieveCommand] Not enough currency for item: " + itemName);

                                                                }
                                                            } else {
                                                                source.sendError(Text.literal("§cInvalid item index."));
                                                                System.out.println("[RetrieveCommand] Invalid index: " + index);

                                                            }

                                                            return 1;
                                                        })
                                                )
                                        )
                                ))
        );
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
            System.out.println("[RetrieveCommand] Loaded stored items for player: " + playerName + " - " + itemNames);

        } catch (IOException e) {
            e.printStackTrace();

            System.err.println("[RetrieveCommand] Error reading stored items for player: " + playerName);

        }
        return itemNames;
    }

    // Method to create an ItemStack from the item name
    private static ItemStack createItemFromName(String itemName) {
        return ItemFactory.createItemFromName(itemName);

    }

    // Method to check if an index can be retrieved
    private static boolean hasNotRetrievedItemBefore(String playerName, int index) {
        Set<Integer> retrievedIndexes = getRetrievedIndexesForPlayer(playerName);
        return !retrievedIndexes.contains(index); // If not in the set, allow retrieval
    }


    // Method to get the color associated with the weapon rarity
    private static Formatting getRarityColor(String rarity) {
        switch (rarity.toUpperCase()) {
            case "COMMON":
                return Formatting.GRAY;
            case "UNCOMMON":
                return Formatting.GREEN;
            case "RARE":
                return Formatting.BLUE;
            case "EPIC":
                return Formatting.LIGHT_PURPLE;
            case "LEGENDARY":
                return Formatting.GOLD;
            default:
                return Formatting.WHITE; // Default color for unknown rarities
        }
    }

    // Method to read the retrieved indexes from a file for a given player
    private static Set<Integer> getRetrievedIndexesForPlayer(String playerName) {
        Set<Integer> retrievedIndexes = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(RETRIEVED_ITEMS_FILE));
            for (String line : lines) {
                if (line.startsWith(playerName + ":")) {
                    int index = Integer.parseInt(line.split(":")[1]);
                    retrievedIndexes.add(index);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retrievedIndexes;
    }

    // Method to add an index to the list of retrieved items for a given player
// Method to add an index to the list of retrieved items for a given player
    private static void addRetrievedIndexForPlayer(String playerName, int index) {
        // Update the local map
        Set<Integer> playerRetrievedIndexes = retrievedItemsMap.computeIfAbsent(playerName, k -> new HashSet<>());
        playerRetrievedIndexes.add(index);

        // Persist the retrieved index to the file
        try {
            String entry = playerName + ":" + index + "\n";
            Files.write(Paths.get(RETRIEVED_ITEMS_FILE), entry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateIndexesAfterDeletion(List<String> items, Set<Integer> retrievedIndexes) {
        List<String> updatedItems = new ArrayList<>();
        Set<Integer> updatedIndexes = new HashSet<>();
        for (int i = 0; i < items.size(); i++) {
            if (!retrievedIndexes.contains(i)) {
                updatedItems.add(items.get(i));
                if (i < items.size() - 1) {
                    updatedIndexes.add(i);
                }
            }
        }
        try {
            Files.write(Paths.get("retrieved_items.txt"), updatedItems);
            retrievedItemsMap.put("playerName", updatedIndexes);
            // Debug log
            System.out.println("[RetrieveCommand] Updated indexes after deletion: " + updatedIndexes);
        } catch (IOException e) {
            e.printStackTrace();
            // Debug log
            System.err.println("[RetrieveCommand] Error updating indexes after deletion.");
        }
    }
}
