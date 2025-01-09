package net.drk.summoning.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.drk.summoning.CurrencyManager;
import net.drk.number.NumberFormatter;
import net.drk.command.shield.ShieldStyle;
import net.drk.command.shield.PlayerShieldStyleManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;

import java.math.BigInteger;

public class ShowCurrencyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("display")
                        .then(CommandManager.literal("currency")
                                .executes(ShowCurrencyCommand::showCurrency))));
    }

    private static int showCurrency(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        // Retrieve the player who executed the command
        ServerPlayerEntity player = context.getSource().getPlayer();

        // Fetch the currency data
        BigInteger currentCurrency = BigInteger.valueOf(CurrencyManager.getCurrency(player.getName().getString()));
        if (currentCurrency == null) {
            currentCurrency = BigInteger.ZERO; // Default to zero if not present
        }

        // Get player's selected shield style
        ShieldStyle selectedStyle = PlayerShieldStyleManager.getStyle(player);

        // Format the text for the currency value
        Text currencyText = Text.literal(NumberFormatter.formatPrice(currentCurrency))
                .formatted(selectedStyle.getShieldColor(1.0f)); // Assuming 1.0f for full value

        // Color for the left "<" and right ">" symbols
        Text leftArrow = Text.literal("<").formatted(selectedStyle.getShieldColor(1.0f));
        Text rightArrow = Text.literal(">").formatted(selectedStyle.getShieldColor(1.0f));

        // Create the label with formatting
        Text label = Text.literal(" Currency: ").formatted(selectedStyle.getShieldColor(1.0f));

        // Assemble the action bar message
        Text message = ((MutableText) leftArrow)
                .append(Text.literal(" "))
                .append(label)
                .append(currencyText)
                .append(Text.literal(" "))
                .append(rightArrow);

        // Send the message to the action bar
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(message));

        // Return success
        return Command.SINGLE_SUCCESS;
    }
}
