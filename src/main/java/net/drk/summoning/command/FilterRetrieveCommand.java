package net.drk.summoning.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FilterRetrieveCommand {

    private static final Map<String, Set<Integer>> retrievedItemsMap = new HashMap<>(); // Player -> Retrieved Indexes Set
    private static final String RETRIEVED_ITEMS_FILE = "retrieved_items.txt"; // File to store retrieved items
    private static final int ITEMS_PER_PAGE = 5; // Number of items per page

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .then(CommandManager.literal("summon")
                                .then(CommandManager.literal("storage")
                                .then(CommandManager.literal("filter")
                                        .then(CommandManager.argument("rarity", StringArgumentType.string())
                                                .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            String rarity = StringArgumentType.getString(context, "rarity");
                                                            int page = IntegerArgumentType.getInteger(context, "page");
                                                            ServerCommandSource source = context.getSource();
                                                            String playerName = source.getPlayer().getName().getString();

                                                            List<ItemEntry> itemEntries = getStoredItemsForPlayer(playerName, rarity, null);
                                                            displayFilteredItems(source, itemEntries, page, playerName);

                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(CommandManager.argument("type", StringArgumentType.string())
                                                .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            String type = StringArgumentType.getString(context, "type");
                                                            int page = IntegerArgumentType.getInteger(context, "page");
                                                            ServerCommandSource source = context.getSource();
                                                            String playerName = source.getPlayer().getName().getString();

                                                            List<ItemEntry> itemEntries = getStoredItemsForPlayer(playerName, null, type);
                                                            displayFilteredItems(source, itemEntries, page, playerName);

                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("retrieve")
                                .then(CommandManager.argument("index", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            int index = IntegerArgumentType.getInteger(context, "index") - 1; // Adjust for 0-based index
                                            ServerCommandSource source = context.getSource();
                                            String playerName = source.getPlayer().getName().getString();

                                            List<ItemEntry> itemEntries = getStoredItemsForPlayer(playerName, null, null);

                                            // Check if the index is valid
                                            if (index >= 0 && index < itemEntries.size()) {
                                                ItemEntry entry = itemEntries.get(index);
                                                String itemName = entry.name;
                                                int retrieveCost = WeaponConfig.getRetrieveCost(itemName);

                                                if (CurrencyManager.canAfford(playerName, retrieveCost)) {
                                                    if (canRetrieveIndex(playerName, entry.index)) {
                                                        CurrencyManager.deductCurrency(playerName, retrieveCost);
                                                        ItemStack itemStack = createItemFromName(itemName);

                                                        if (!itemStack.isEmpty()) {
                                                            // Give the item to the player
                                                            boolean success = source.getPlayer().giveItemStack(itemStack);
                                                            if (success) {
                                                                addRetrievedIndexForPlayer(playerName, entry.index); // Mark item as retrieved
                                                                source.sendFeedback(
                                                                        () -> Text.literal("§aItem retrieved from storage: ").formatted(Formatting.GREEN)
                                                                                .append(Text.literal(itemName).formatted(Formatting.WHITE)),
                                                                        false
                                                                );
                                                            } else {
                                                                source.sendError(Text.literal("§cFailed to give item to the player."));
                                                            }
                                                        } else {
                                                            source.sendError(Text.literal("§cError creating item from name: " + itemName));
                                                        }
                                                    } else {
                                                        source.sendError(Text.literal("§cCannot retrieve this item more than once."));
                                                    }
                                                } else {
                                                    source.sendError(Text.literal("§cYou do not have enough currency."));
                                                }
                                            } else {
                                                source.sendError(Text.literal("§cInvalid item index."));
                                            }

                                            return 1;
                                        })
                                )
                        )
        ));
    }

    // Method to read the stored items from a file for a given player with optional filtering
    private static List<ItemEntry> getStoredItemsForPlayer(String playerName, String rarityFilter, String typeFilter) {
        List<ItemEntry> itemEntries = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("loot_items.txt"));
            for (String line : lines) {
                if (line.startsWith(playerName)) {
                    String[] parts = line.split(":");
                    String itemName = parts[2];
                    int index = Integer.parseInt(parts[3]); // Read the index
                    String itemRarity = WeaponConfig.getWeaponRarity(itemName);
                    String itemType = WeaponConfig.getWeaponType(itemName);

                    // Apply filters
                    boolean matchesRarity = rarityFilter == null || rarityFilter.equalsIgnoreCase(itemRarity);
                    boolean matchesType = typeFilter == null || typeFilter.equalsIgnoreCase(itemType);

                    if (matchesRarity && matchesType) {
                        itemEntries.add(new ItemEntry(itemName, index));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemEntries;
    }

    // Method to create an ItemStack from the item name
    private static ItemStack createItemFromName(String itemName) {
        return  ItemFactory.createItemFromName(itemName);

    }

    // Method to check if an index can be retrieved
    private static boolean canRetrieveIndex(String playerName, int index) {
        Set<Integer> playerRetrievedIndexes = retrievedItemsMap.computeIfAbsent(playerName, k -> new HashSet<>());
        return !playerRetrievedIndexes.contains(index);
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

    // Method to display filtered items with pagination and gray out already retrieved items
    private static void displayFilteredItems(ServerCommandSource source, List<ItemEntry> itemEntries, int page, String playerName) {
        Set<Integer> retrievedIndexes = getRetrievedIndexesForPlayer(playerName);
        int totalPages = (int) Math.ceil((double) itemEntries.size() / ITEMS_PER_PAGE);

        // If the requested page exceeds the total pages, show an error
        if (page > totalPages) {
            source.sendFeedback(
                    () -> Text.literal("§cPage number exceeds total pages.").formatted(Formatting.RED),
                    false
            );
            return;
        }

        // Display header
        source.sendFeedback(
                () -> Text.literal("§6--- Filtered Items (Page " + page + "/" + totalPages + ") ---").formatted(Formatting.GOLD),
                false
        );

        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, itemEntries.size());

        for (int i = startIndex; i < endIndex; i++) {
            ItemEntry entry = itemEntries.get(i);
            String itemRarity = WeaponConfig.getWeaponRarity(entry.name);
            Formatting numberColor = getRarityColor(itemRarity);
            Formatting textColor = retrievedIndexes.contains(entry.index) ? Formatting.GRAY : Formatting.WHITE;

            int finalI = i;
            source.sendFeedback(
                    () -> Text.literal(String.format("%d. ", finalI + 1)).formatted(numberColor) // Set number color to rarity color
                            .append(Text.literal(entry.name).formatted(textColor)) // Set item name color (white or gray if retrieved)
                            .append(Text.literal(" [" + (entry.index) + "]").formatted(retrievedIndexes.contains(entry.index - 1) ? Formatting.GRAY : Formatting.YELLOW)), // Gray if retrieved, otherwise yellow
                    false
            );

            System.out.println("Checking retrieved status for index " + entry.index + ": " + retrievedIndexes.contains(entry.index));


        }
    }

    // Method to read the retrieved indexes from a file for a given player
    private static Set<Integer> getRetrievedIndexesForPlayer(String playerName) {
        Set<Integer> retrievedIndexes = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(RETRIEVED_ITEMS_FILE));
            for (String line : lines) {
                if (line.startsWith(playerName)) {
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
    private static void addRetrievedIndexForPlayer(String playerName, int index) {
        Set<Integer> playerRetrievedIndexes = retrievedItemsMap.computeIfAbsent(playerName, k -> new HashSet<>());
        playerRetrievedIndexes.add(index);
        try {
            Files.write(Paths.get(RETRIEVED_ITEMS_FILE),
                    (playerName + ":" + index + "\n").getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Helper class to store item name and index
    private static class ItemEntry {
        String name;
        int index;

        ItemEntry(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }
}
