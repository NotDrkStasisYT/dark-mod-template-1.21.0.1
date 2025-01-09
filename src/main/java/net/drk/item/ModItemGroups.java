package net.drk.item;

import net.drk.DarkMod;
import net.drk.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;



public class ModItemGroups {
    public static final ItemGroup DARKMOD_ITEMGROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.tryParse(DarkMod.MOD_ID + ":" + "drkmod_itemgroup"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.drkmod"))
                    .icon(() -> new ItemStack(Items.DIAMOND))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.BLOODFIRE_SPEAR);
                        entries.add(ModBlocks.CONDENSED_DIRT);
                        entries.add(ModBlocks.WHITE_BLOCK);
                        entries.add(ModBlocks.RED_BLOCK1);
                        entries.add(ModBlocks.RED_BLOCK2);
                        entries.add(ModBlocks.RED_BLOCK3);
                        entries.add(ModBlocks.RED_BLOCK4);
                        entries.add(ModBlocks.RED_BLOCK5);
                        entries.add(ModBlocks.RED_BLOCK6);
                        entries.add(ModBlocks.RED_BLOCK7);
                        entries.add(ModBlocks.RED_BLOCK);
                        entries.add(ModBlocks.RED_BLOCK8);
                        entries.add(ModBlocks.BARRICADE);
                        entries.add(ModBlocks.SPIKED_PALISADES);
                        entries.add(ModBlocks.CRAFTING_TABLE);
                        entries.add(ModBlocks.BARREL);
                        entries.add(ModBlocks.TAVERN_CABINENT);
                        entries.add(ModBlocks.TAVERN_KEG);
                        entries.add(ModBlocks.TAVERN_KEG_STATION);
                        entries.add(ModBlocks.TAVERN_LONG_TABLE);
                        entries.add(ModBlocks.TAVERN_PINT_GLASS);
                        entries.add(ModBlocks.TAVERN_SELF);
                        entries.add(ModBlocks.TAVERN_STOOL);
                        entries.add(ModBlocks.TAVERN_BAR_COUNTER);
                        entries.add(ModBlocks.TAVERN_BENCH);
                        entries.add(ModBlocks.TAVERN_BOTTLE_STACK);
                        entries.add(ModBlocks.TAVERN_BOTTLES);
                        entries.add(ModBlocks.OAK_LOG);
                        entries.add(ModBlocks.SPRUCE_LOG);
                        entries.add(ModBlocks.JUNGLE_LOG);
                        entries.add(ModBlocks.MANGROVE_LOG);
                        entries.add(ModBlocks.DARK_OAK_LOG);
                        entries.add(ModBlocks.CHERRY_LOG);
                        entries.add(ModBlocks.ACACIA_LOG);
                        entries.add(ModBlocks.BIRCH_LOG);
                        entries.add(ModBlocks.AUTUMN_LOG);
                        entries.add(ModBlocks.MYSTICAL_LOG);
                        entries.add(ModBlocks.SHADOW_LOG);
                        entries.add(ModBlocks.SPIRIT_LOG);






















                    })
                    .build());

    public static void registerItemGroups() {
        DarkMod.LOGGER.info("Registering Item Groups for " + DarkMod.MOD_ID);
    }
}