package net.drk.command.lumin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.drk.LuminCurrency;
import net.drk.PlayerData;
import net.drk.PlayerdataManager;
import net.drk.command.shield.ShieldStyle;
import net.drk.command.shield.PlayerShieldStyleManager;
import net.drk.command.shield.ShowShieldCommand;
import net.drk.number.NumberFormatter;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;

import java.math.BigInteger;

public class ShowLuminCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("display")
                        .then(CommandManager.literal("lumin").requires(source -> source.hasPermissionLevel(2))
                                .executes(ShowLuminCommand::showLumin))));
    }

    private static int showLumin(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        // Retrieve or create player data for the player
        PlayerData playerData = PlayerdataManager.getPlayerData(player.getName().getString());
        LuminCurrency playerLumin = playerData.getLuminCurrency();

        if (playerLumin == null) {
            playerLumin = new LuminCurrency(BigInteger.ZERO); // Default to zero if not present
            playerData.setLuminCurrency(playerLumin);
        }

        BigInteger currentLumin = playerLumin.getAmount(player);

        // Get player's selected shield style
        ShieldStyle selectedStyle = PlayerShieldStyleManager.getStyle(player);

        // Format the text with colors and styling using shield styles
        Text luminText = Text.literal(NumberFormatter.formatPrice(currentLumin))
                .formatted(selectedStyle.getShieldColor(1.0f)); // Assuming 1.0f for full value

        // Color for the left "<" and right ">" symbols
        Text leftArrow = Text.literal("<").formatted(selectedStyle.getShieldColor(1.0f));
        Text rightArrow = Text.literal(" >").formatted(selectedStyle.getShieldColor(1.0f));

        // Create the label with formatting
        Text label = Text.literal(" Lumin: ").formatted(selectedStyle.getShieldColor(1.0f));

        // Assemble the action bar message
        Text message = ((MutableText) leftArrow)
                .append(label)
                .append(luminText)
                .append(rightArrow);

        // Send the message to the action bar
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(message));

        return Command.SINGLE_SUCCESS;
    }
}
