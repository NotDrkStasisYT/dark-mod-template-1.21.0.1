package net.drk.block;


import net.drk.DarkMod;
import net.drk.block.advanced.CraftingTable;
import net.drk.block.voxel.*;
import net.drk.block.voxel.logs.block.*;
import net.drk.block.voxel.tavern.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block WHITE_BLOCK = registerBlock("white_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block RED_BLOCK = registerBlock("red_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block RED_BLOCK1 = registerBlock("red_block1",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block RED_BLOCK2 = registerBlock("red_block2",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block RED_BLOCK3 = registerBlock("red_block3",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RED_BLOCK4 = registerBlock("red_block4",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RED_BLOCK5 = registerBlock("red_block5",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RED_BLOCK6 = registerBlock("red_block6",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RED_BLOCK7 = registerBlock("red_block7",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RED_BLOCK8 = registerBlock("red_block8",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block BARRICADE = registerBlock("barricade",
            new Barricade(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(4.0f).sounds(BlockSoundGroup.WOOD)));
    public static final Block SPIKED_PALISADES = registerBlock("spiked_palisades",
            new Spiked_Palisades(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(4.0f).sounds(BlockSoundGroup.WOOD)));
    public static final Block CRAFTING_TABLE = registerBlock("crafting_table",
            new CraftingTable(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(4.0f).sounds(BlockSoundGroup.WOOD)));

    public static final Block BARREL = registerBlock("barrel",
            new Barrel(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));


    public static final Block OAK_LOG = registerBlock("oak_log",
            new OakLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block SPRUCE_LOG = registerBlock("spruce_log",
            new SpruceLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block JUNGLE_LOG = registerBlock("jungle_log",
            new JungleLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block MANGROVE_LOG = registerBlock("mangrove_log",
            new MangroveLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block DARK_OAK_LOG = registerBlock("dark_oak_log",
            new DarkOakLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block CHERRY_LOG = registerBlock("cherry_log",
            new CherryLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block ACACIA_LOG = registerBlock("acacia_log",
            new AcaciaLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block BIRCH_LOG = registerBlock("birch_log",
            new BirchLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block AUTUMN_LOG = registerBlock("autumn_log",
            new AutumnLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block MYSTICAL_LOG = registerBlock("mystical_log",
            new MysticalLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD).luminance(MysticalLog::getLighting)));
    public static final Block SHADOW_LOG = registerBlock("shadow_log",
            new ShadowLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));
    public static final Block SPIRIT_LOG = registerBlock("spirit_log",
            new SpiritLog(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD).luminance(SpiritLog::getLighting)));

//Tavern Blocks:
public static final Block TAVERN_STOOL = registerBlock("tavern_stool",
        new TavernStoolBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_BAR_COUNTER = registerBlock("tavern_bar_counter",
            new TavernBarCounter(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_BENCH = registerBlock("tavern_bench",
            new TavernBench(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_BOTTLE_STACK = registerBlock("tavern_bottle_stack",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_BOTTLES = registerBlock("tavern_bottles",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_SELF = registerBlock("tavern_shelf",
            new TavernShelfBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_PINT_GLASS = registerBlock("tavern_pint_glass",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_LONG_TABLE = registerBlock("tavern_long_table",
            new TavernLongTable(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_KEG_STATION = registerBlock("tavern_keg_station",
            new TavernKegStation(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_KEG = registerBlock("tavern_keg",
            new TavernKegBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    public static final Block TAVERN_CABINENT = registerBlock("tavern_cabinet",
            new TavernCabinet(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.WOOD)));

    //

    public static final Block CONDENSED_DIRT = registerBlock("condensed_dirt",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.tryParse(DarkMod.MOD_ID + ":" + name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.tryParse(DarkMod.MOD_ID + ":" + name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        DarkMod.LOGGER.info("Registering ModBLocks for " + DarkMod.MOD_ID);
    }
}



