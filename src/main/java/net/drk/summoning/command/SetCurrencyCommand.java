package net.drk.summoning.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.drk.summoning.CurrencyManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class SetCurrencyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.literal("setcurrency")
                                .requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.argument("player", StringArgumentType.string())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(SetCurrencyCommand::setCurrency)
                                        )
                                )
                        )
        );
    }

    private static int setCurrency(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String playerName = StringArgumentType.getString(context, "player");
        int amount = IntegerArgumentType.getInteger(context, "amount");

        // Check if the amount is valid
        if (amount < 0) {
            source.sendError(Text.literal("§cCurrency amount cannot be negative."));
            return 1;
        }

        // Apply the command to all players if the selector is @a
        if ("@a".equals(playerName)) {
            Collection<ServerPlayerEntity> players = source.getServer().getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : players) {
                boolean success = CurrencyManager.setCurrency(player.getName().getString(), amount);
                if (success) {
                    source.sendFeedback(() -> Text.literal("§aSuccessfully set currency for " + player.getName().getString() + " to " + amount + ".").formatted(Formatting.GREEN), false);
                } else {
                    source.sendError(Text.literal("§cFailed to set currency for " + player.getName().getString() + "."));
                }
            }
        } else {
            // Set the currency for a specific player
            boolean success = CurrencyManager.setCurrency(playerName, amount);
            if (success) {
                source.sendFeedback(() -> Text.literal("§aSuccessfully set currency for " + playerName + " to " + amount + ".").formatted(Formatting.GREEN), false);
            } else {
                source.sendError(Text.literal("§cFailed to set currency for " + playerName + "."));
            }
        }

        return 1;
    }
}
