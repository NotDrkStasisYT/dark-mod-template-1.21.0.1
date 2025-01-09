package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class HelpCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("help")
                        .executes(HelpCommand::showAllCommands)
                        .then(CommandManager.argument("command", StringArgumentType.string())
                                .executes(HelpCommand::showSpecificCommand)
                        )
                );

        dispatcher.register(command);
    }

    private static int showAllCommands(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback((Supplier<Text>) Text.literal("Available Shatterpoint Commands:\n" +
                "/Shatterpoint help - Show this help message\n" +
                "/Shatterpoint score - Check your score\n" +
                "/Shatterpoint listfriends - List your friends\n" +
                "/Shatterpoint sell {price} - Sell the item in your hand for {price} Lumin\n" +
                "/Shatterpoint checkbalance - Check your balance\n" +
                "/Shatterpoint trade - Trade with another player\n" +
                "... and more!"), false);
        return 1;
    }

    private static int showSpecificCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String command = StringArgumentType.getString(context, "command");

        switch (command.toLowerCase()) {
            case "score":
                source.sendFeedback(()-> Text.literal("/Shatterpoint score - Check your score in the game."), false);
                break;
            case "listfriends":
                source.sendFeedback(()-> Text.literal("/Shatterpoint listfriends - List all your friends in the game."), false);
                break;
            case "sell":
                source.sendFeedback(()-> Text.literal("/Shatterpoint sell {price} - Sell the item in your main hand for {price} Lumin."), false);
                break;
            case "checkbalance":
                source.sendFeedback(()-> Text.literal("/Shatterpoint checkbalance - Check your current Lumin balance."), false);
                break;
            case "trade":
                source.sendFeedback(()-> Text.literal("/Shatterpoint trade - Initiate or manage trades with other players."), false);
                break;
            default:
                source.sendFeedback(()-> Text.literal("Unknown command: " + command), false);
                break;
        }
        return 1;
    }
}
