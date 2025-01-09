package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.drk.LuminCurrency;
import net.drk.PlayerData;
import net.drk.PlayerdataManager;
import net.drk.number.NumberFormatter;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.math.BigInteger;

public class CheckBalanceCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("lumin")
                                .requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.literal("balance")
                                .executes(CheckBalanceCommand::checkBalance)
                        )
        ));
    }

    private static int checkBalance(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.sendError(Text.literal("Player not found."));
            return Command.SINGLE_SUCCESS;
        }

        // Retrieve or create player data
        PlayerData playerData = PlayerdataManager.getPlayerData(player.getName().getString());
        if (playerData == null) {
            source.sendError(Text.literal("Player data not found."));
            return Command.SINGLE_SUCCESS;
        }

        LuminCurrency playerLumin = playerData.getLuminCurrency();

        if (playerLumin == null) {
            source.sendError(Text.literal("Lumin currency data not found."));
            return Command.SINGLE_SUCCESS;
        }

        // Retrieve lumin earned in the last 24 hours
        BigInteger luminEarnedLast24Hours = playerData.getLuminEarnedLast24Hours();

        // Format balances
        String formattedBalance = playerLumin.getFormattedAmount();
        String formattedEarned = NumberFormatter.formatPrice(luminEarnedLast24Hours) + " Lumin";

        // Send feedback to the player
        Text message = Text.literal("Your current balance is: ")
                .append(Text.literal(formattedBalance).formatted(Formatting.GREEN))
                .append(Text.literal(".\nYou have earned: ")
                        .append(Text.literal(formattedEarned).formatted(Formatting.GOLD))
                        .append(Text.literal(" in the last 24 hours.")
                        ));

        source.sendFeedback(() -> message, false);

        return Command.SINGLE_SUCCESS;
    }
}
