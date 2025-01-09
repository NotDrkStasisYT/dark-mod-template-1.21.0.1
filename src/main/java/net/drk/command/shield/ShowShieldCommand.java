package net.drk.command.shield;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.drk.command.shield.max.MaxShieldColorManager;
import net.drk.number.NumberFormatter;
import net.drk.player.ShieldManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;

import java.math.BigInteger;

public class ShowShieldCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("display")
                        .then(CommandManager.literal("shield").requires(source -> source.hasPermissionLevel(2))
                                .executes(ShowShieldCommand::showShield))));
    }

    private static int showShield(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        // Retrieve the player who executed the command
        ServerPlayerEntity player = context.getSource().getPlayer();

        // Fetch the shield data
        ShieldManager shieldManager = ShieldManager.getInstance();
        BigInteger currentShield = shieldManager.getShield(player);
        BigInteger maxShield = shieldManager.getMaxShield(player);

        // Calculate the shield percentage
        float percentage = currentShield.floatValue() / maxShield.floatValue();

        // Get the player's selected shield style
        ShieldStyle selectedStyle = PlayerShieldStyleManager.getStyle(player);
        Formatting maxShieldColor = MaxShieldColorManager.getMaxShieldColor(player);

        // Format the text for current and max shield values
        Text currentShieldText = Text.literal(NumberFormatter.formatPrice(currentShield))
                .formatted(selectedStyle.getShieldColor(percentage));

        Text maxShieldText = Text.literal(NumberFormatter.formatPrice(maxShield))
                .formatted(maxShieldColor);

        // Format the action bar message
        Text leftArrow = Text.literal("<").formatted(selectedStyle.getShieldColor(percentage));
        Text rightArrow = Text.literal(">").formatted(selectedStyle.getShieldColor(percentage));

        Text message = ((MutableText) leftArrow)
                .append(Text.literal(" "))
                .append(currentShieldText)
                .append(Text.literal(" / ").formatted(Formatting.GRAY))
                .append(maxShieldText)
                .append(Text.literal(" "))
                .append(rightArrow);

        // Send the message to the player's action bar
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(message));

        // Return success
        return Command.SINGLE_SUCCESS;
    }
}
