package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.drk.LuminCurrency;
import net.drk.PlayerData;
import net.drk.PlayerdataManager;
import net.drk.network.lumin.LuminNetworking;
import net.drk.number.NumberFormatter;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.print.attribute.standard.Severity;
import java.math.BigInteger;

public class AddLuminCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("lumin")
                                .requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.literal("add")
                                        .then(CommandManager.argument("player", StringArgumentType.string())
                                                .then(CommandManager.argument("amount", StringArgumentType.string())
                                                        .executes(AddLuminCommand::giveLumin)
                                                )
                                        )
                                )
                        )
        );
    }

    private static int giveLumin(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerCommandSource source = context.getSource();
        String playerName = StringArgumentType.getString(context, "player");
        String amountStr = StringArgumentType.getString(context, "amount");

        // Find player by name
        ServerPlayerEntity targetPlayer = source.getServer().getPlayerManager().getPlayer(playerName);
        if (targetPlayer == null) {
            source.sendError(Text.literal("Player not found."));
            return Command.SINGLE_SUCCESS;
        }

        // Convert amount to BigInteger
        BigInteger bigAmount;
        try {
            bigAmount = new BigInteger(amountStr);
        } catch (NumberFormatException e) {
            source.sendError(Text.literal("Invalid amount specified."));
            return Command.SINGLE_SUCCESS;
        }

        // Retrieve or create player data for the target player
        PlayerData targetPlayerData = PlayerdataManager.getPlayerData(targetPlayer.getName().getString());
        LuminCurrency targetPlayerLumin = targetPlayerData.getLuminCurrency();

        // Ensure the target player has a valid balance
        if (bigAmount.signum() <= 0) {
            source.sendError(Text.literal("Amount must be positive."));
            return Command.SINGLE_SUCCESS;
        }

        // Add Lumin to the target player's currency
        targetPlayerLumin.addLumin(player, bigAmount);

        // Update target player data
        PlayerdataManager.setPlayerData(targetPlayer.getName().getString(), targetPlayerData);

        // Format the balance
        String formattedBalance = targetPlayerLumin.getFormattedAmount();

        // Create a Text object with green formatting for the balance
        Text balanceText = Text.literal(formattedBalance).formatted(Formatting.GREEN);
        LuminNetworking.sendLuminDataToPlayer(targetPlayer);

        // Send feedback to the command source
        Text message = Text.literal("You have given ")
                .append(Text.literal(NumberFormatter.formatPrice(bigAmount)).formatted(Formatting.GREEN))
                .append(Text.literal(" Lumin to "))
                .append(Text.literal(targetPlayer.getName().getString()))
                .append(Text.literal(". Their current balance is: "))
                .append(balanceText); // Append formatted balance in green
        source.sendFeedback(() -> message, false);

        // Optionally, send feedback to the target player as well
        targetPlayer.sendMessage(Text.literal("You have been given ")
                .append(Text.literal(NumberFormatter.formatPrice(bigAmount)).formatted(Formatting.GREEN))
                .append(Text.literal(" Lumin by "))
                .append(Text.literal(source.getName()).formatted(Formatting.GREEN))
                .append(Text.literal(". Your current balance is: "))
                .append(balanceText), false);

        return Command.SINGLE_SUCCESS;
    }
}
