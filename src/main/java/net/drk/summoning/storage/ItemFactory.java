package net.drk.summoning.storage;

import net.drk.item.ModItems;
import net.minecraft.item.ItemStack;

public class ItemFactory {

    // Method to create an ItemStack based on the item name
    public static ItemStack createItemFromName(String itemName) {
        try {
            // Use the ModItems class to retrieve the item instance based on the itemName
            switch (itemName) {
                case "gravebane":
                    return new ItemStack(ModItems.GRAVEBANE); // Example reference to a custom item
                case "bloodfire_spear":
                    return new ItemStack(ModItems.BLOODFIRE_SPEAR);
                case "nature_sword":
                    return new ItemStack(ModItems.NATURE_SWORD);
                case "fire_sword":
                    return new ItemStack(ModItems.FIRE_SWORD);
                case "eclipse_spear":
                    return new ItemStack(ModItems.ECLIPSE_SPEAR);
                default:
                    // Default case for when the item is not found
                    return ItemStack.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }
}
