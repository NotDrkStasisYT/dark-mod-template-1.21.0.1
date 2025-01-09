package net.drk.item;

import net.drk.DarkMod;
import net.drk.entity.ModEntities;
import net.drk.item.other.weapons.BloodfireSpear;
import net.drk.item.other.weapons.test;
import net.drk.item.trinket.backpack.SchoolBackpack;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.mixin.item.ItemSettingsMixin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.drk.item.ModFoodComponents.SUSPICIOUS_FOOD_COMPONENT;
import static net.drk.item.ModFoodComponents.OVERSHIELD_FOOD_COMPONENT;


public class ModItems {


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(DarkMod.MOD_ID, name), item);
    }

    // School Backpack
    public static final Item SCHOOL_BACKPACK = register(
            new SchoolBackpack(new Item.Settings().maxCount(1)), "school_backpack");
    // Book1
    public static final Item BOOK1 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "book1");
    // Acorn 1
    public static final Item ACORN1 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "acorn1");
    // Acorn 2
    public static final Item ACORN2 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "acorn2");
    // Acorn 3
    public static final Item ACORN3 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "acorn3");


    // Rock5
    public static final Item ROCK5 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "rock5");
    // Rock4
    public static final Item ROCK4 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "rock4");
    // Rock3
    public static final Item ROCK3 = register(
                new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "rock3");
    // Rock2
    public static final Item ROCK2 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "rock2");

    // Void
    public static final Item VOID = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "void");
    // Void1
    public static final Item VOID1 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "void1");
    // Black Chest
    public static final Item BLACK_CHEST = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "black_chest");
    // Blue Chest
    public static final Item BLUE_CHEST = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "blue_chest");
    // Wooden Chest
    public static final Item WOODEN_CHEST = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "wooden_chest");
    // Umbrella
    public static final Item UMBRELLA = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "umbrella");
    // Opal Seashell
    public static final Item OPAL_SEASHELL = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "opal_seashell");
    // Tangerine Seashell
    public static final Item TANGERINE_SEASHELL = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "tangerine_seashell");

    // Seashell
    public static final Item SEASHELL = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "seashell");

    //Roseate Seashell
    public static final Item ROSEATE_SEASHELL = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "roseate_seashell");

    //Rock
    public static final Item ROCK1 = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "rock1");


    // Overshield Potion
    public static final Item OVERSHIELD_POTION = register(
            new Item(new Item.Settings().food(OVERSHIELD_FOOD_COMPONENT)),
            "overshield_potion");




// Suspicious Substance
    public static final Item SUSPICIOUS_SUBSTANCE = register(
            new Item(new Item.Settings().food(SUSPICIOUS_FOOD_COMPONENT)),
            "suspicious_substance");

// Bloodfire spear
public static final Item BLOODFIRE_SPEAR = register(
        new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "bloodfire_spear");

// Gravebane
    public static final Item GRAVEBANE = registerItem("gravebane",
        new test(ModToolMaterial.FLUORITE,
                new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterial.FLUORITE, 3, -2.4f)), StatusEffects.LEVITATION));


    // Twitch Sword
    public static final Item TWITCH_SWORD = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "twitch_sword");

// Dark Sword
    public static final Item DARK_SWORD = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "dark_sword");

// Dark Sword
    public static final Item ENERGY_SWORD = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "energy_sword");

// Nature Sword
    public static final Item NATURE_SWORD = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "nature_sword");

// Fire Sword
    public static final Item FIRE_SWORD = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "fire_sword");

// Cleaver
    public static final Item CLEAVER = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "cleaver");

// Eclipse Spear
    public static final Item ECLIPSE_SPEAR = register(
            new SwordItem(ModToolMaterial.FLUORITE, new Item.Settings()), "eclipse_spear");






    public static Item register(Item item, String id) {

        Identifier itemID = Identifier.of(DarkMod.MOD_ID, id);

        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

        return registeredItem;

        }
        public static void initialize() {




        // Suspicious Substance
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ModItems.SUSPICIOUS_SUBSTANCE));


        //Dark Staff
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.BLOODFIRE_SPEAR));

        // Gravebane
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.GRAVEBANE));

        // Twitch Sword
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.TWITCH_SWORD));

        // Dark Sword
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.DARK_SWORD));

        // Energy Sword
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.ENERGY_SWORD));

        // Nature Sword
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.NATURE_SWORD));

        // Fire Sword
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.FIRE_SWORD));

        // Cleaver
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.CLEAVER));

        // Eclipse Spear
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                    .register((itemGroup) -> itemGroup.add(ModItems.ECLIPSE_SPEAR));

        // Overshield Potion
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.OVERSHIELD_POTION));

            // Rock1
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ROCK1));

            // Roseate Seashell
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ROSEATE_SEASHELL));

            // Seashell
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.SEASHELL));
            // Tangerine Seashell

            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.TANGERINE_SEASHELL));

            // Opal Seashell

            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.OPAL_SEASHELL));

            // Umbrella
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.UMBRELLA));

            // Wooden Chest
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.WOODEN_CHEST));
            // Blue Chest
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.BLUE_CHEST));
            // Black Chest
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.BLACK_CHEST));
            // Void
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.VOID));
            // Void1
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.VOID1));

            // Rock2
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ROCK2));
            // Rock3
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ROCK3));
            // Rock4
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ROCK4));
            // Rock5
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ROCK5));


            // Acorn 1
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ACORN1));
            // Acorn 2
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ACORN2));
            // Acorn 3
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.ACORN3));

            // Book 1
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                    .register((itemGroup) -> itemGroup.add(ModItems.BOOK1));




        }

}
