package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.drk.TradeManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TradeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("trade")
                        .then(CommandManager.argument("player", StringArgumentType.word())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity sender = source.getPlayer();
                                    ServerPlayerEntity receiver = source.getServer().getPlayerManager().getPlayer(playerName);

                                    if (receiver == null) {
                                        sender.sendMessage(Text.literal("Player not found."), false);
                                        return 0;
                                    }



                                    TradeManager.initiateTrade(sender, receiver);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(CommandManager.literal("accept")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();
                                    TradeManager.acceptTrade(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(CommandManager.literal("cancel")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();
                                    TradeManager.cancelTrade(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(CommandManager.literal("offer")
                                .then(CommandManager.argument("item", StringArgumentType.word())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(context -> {
                                                    ServerCommandSource source = context.getSource();
                                                    ServerPlayerEntity player = source.getPlayer();
                                                    String itemName = StringArgumentType.getString(context, "item");
                                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                                    Identifier itemId = Identifier.of(itemName);  // Use the correct constructor
                                                    Item item = Registries.ITEM.get(itemId);

                                                    if (item == null) {  // Check for AIR instead of null
                                                        player.sendMessage(Text.literal("Item not found."), false);
                                                        return 0;
                                                    }

                                                    ItemStack itemStack = new ItemStack(item, amount);
                                                    TradeManager.offerItem(player, itemStack, amount);
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                        .then(CommandManager.literal("history")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();
                                    String history = TradeManager.getTradeHistory(player);
                                    player.sendMessage(Text.literal(history), false);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
