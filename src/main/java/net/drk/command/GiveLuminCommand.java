package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.LuminCurrency;
import net.drk.PlayerData;
import net.drk.PlayerdataManager;
import net.drk.number.NumberFormatter;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.math.BigInteger;

public class GiveLuminCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.literal("lumin")
                                .requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.literal("give")
                                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                                .then(CommandManager.argument("amount", StringArgumentType.string())
                                                        .executes(GiveLuminCommand::giveLumin)
                                                )
                                        )
                                )
                        ));
    }

    private static int giveLumin(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity giver = source.getPlayer();
        ServerPlayerEntity recipient;

        try {
            recipient = EntityArgumentType.getPlayer(context, "player");
        } catch (Exception e) {
            source.sendFeedback(() -> Text.literal("Error retrieving recipient player."), false);
            return Command.SINGLE_SUCCESS;
        }

        String amountStr = StringArgumentType.getString(context, "amount");
        BigInteger bigAmount;

        try {
            bigAmount = new BigInteger(amountStr);
        } catch (NumberFormatException e) {
            source.sendFeedback(() -> Text.literal("Invalid amount specified."), false);
            return Command.SINGLE_SUCCESS;
        }

        // Retrieve or create player data
        PlayerData giverData = PlayerdataManager.getPlayerData(giver.getName().getString());
        PlayerData recipientData = PlayerdataManager.getPlayerData(recipient.getName().getString());

        if (giverData == null || recipientData == null) {
            source.sendFeedback(() -> Text.literal("Error retrieving player data."), false);
            return Command.SINGLE_SUCCESS;
        }

        LuminCurrency giverLumin = giverData.getLuminCurrency();
        LuminCurrency recipientLumin = recipientData.getLuminCurrency();

        // Check if the giver has enough Lumin
        if (giverLumin.getAmount(player).compareTo(bigAmount) < 0) {
            source.sendFeedback(() -> Text.literal("Insufficient funds."), false);
            return Command.SINGLE_SUCCESS;
        }

        // Get the number abbreviations
        String giverAbbreviation = getNumberAbbreviation(giverLumin.getAmount(player));
        String recipientAbbreviation = getNumberAbbreviation(recipientLumin.getAmount(player));

        if (giverAbbreviation.equals(recipientAbbreviation)) {
            // Direct transfer if both have the same abbreviation
            giverLumin.subtractLumin(bigAmount);
            recipientLumin.addLumin(player, bigAmount);

            // Update player data
            PlayerdataManager.setPlayerData(giver.getName().getString(), giverData);
            PlayerdataManager.setPlayerData(recipient.getName().getString(), recipientData);

            // Send feedback to both players
            Text message = Text.literal("You have given ")
                    .append(Text.literal(NumberFormatter.formatPrice(bigAmount)).formatted(Formatting.GREEN))
                    .append(Text.literal(" Lumin to " + recipient.getName().getString() + ". Your current balance is: " + giverLumin.getFormattedAmount()));
            source.sendFeedback(() -> message, false);

            Text recipientMessage = Text.literal("You have received ")
                    .append(Text.literal(NumberFormatter.formatPrice(bigAmount)).formatted(Formatting.GREEN))
                    .append(Text.literal(" Lumin from " + giver.getName().getString() + ". Your new balance is: " + recipientLumin.getFormattedAmount()));
            recipient.sendMessage(recipientMessage, false);

        } else {
            // Calculate percentage-based transfer if abbreviations differ
            BigInteger amountToGive = bigAmount;
            BigInteger recipientCurrentAmount = recipientLumin.getAmount(player);
            BigInteger amountToReceive = amountToGive.multiply(BigInteger.valueOf(100))
                    .divide(BigInteger.valueOf(100)).multiply(recipientCurrentAmount)
                    .divide(BigInteger.valueOf(100));

            // Ensure the giver has enough Lumin
            if (giverLumin.getAmount(player).compareTo(amountToGive) < 0) {
                source.sendFeedback(() -> Text.literal("Insufficient funds."), false);
                return Command.SINGLE_SUCCESS;
            }

            giverLumin.subtractLumin(amountToGive);
            recipientLumin.addLumin(player, amountToReceive);

            // Update player data
            PlayerdataManager.setPlayerData(giver.getName().getString(), giverData);
            PlayerdataManager.setPlayerData(recipient.getName().getString(), recipientData);

            // Send feedback to both players
            Text message = Text.literal("You have given ")
                    .append(Text.literal(NumberFormatter.formatPrice(amountToGive)).formatted(Formatting.GREEN))
                    .append(Text.literal(" Lumin to " + recipient.getName().getString() + ". Your current balance is: " + giverLumin.getFormattedAmount()));
            source.sendFeedback(() -> message, false);

            Text recipientMessage = Text.literal("You have received ")
                    .append(Text.literal(NumberFormatter.formatPrice(amountToReceive)).formatted(Formatting.GREEN))
                    .append(Text.literal(" Lumin from " + giver.getName().getString() + ". Your new balance is: " + recipientLumin.getFormattedAmount()));
            recipient.sendMessage(recipientMessage, false);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static String getNumberAbbreviation(BigInteger amount) {
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000_000_000_000L)) >= 0) return "N"; // Nonillion and beyond
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000_000_000L)) >= 0) return "Dc"; // Decillion
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000_000L)) >= 0) return "Oc"; // Octillion
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000L)) >= 0) return "Sp"; // Septillion
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000L)) >= 0) return "Sx"; // Sextillion
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000L)) >= 0) return "Qn"; // Quintillion
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000L)) >= 0) return "Qd"; // Quadrillion
        if (amount.compareTo(BigInteger.valueOf(1_000_000_000L)) >= 0) return "T"; // Trillion
        if (amount.compareTo(BigInteger.valueOf(1_000_000L)) >= 0) return "B"; // Billion
        if (amount.compareTo(BigInteger.valueOf(1_000L)) >= 0) return "M"; // Million
        if (amount.compareTo(BigInteger.valueOf(1_000)) >= 0) return "K"; // Thousand
        return ""; // Less than thousand
    }
}
