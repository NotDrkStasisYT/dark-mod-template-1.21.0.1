package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.PowerManager;
import net.drk.number.NumberFormatter;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import java.math.BigInteger;

public class PowerCommand {

    private final PowerManager powerManager;

    // Pass the shared PowerManager instance through the constructor
    public PowerCommand(PowerManager powerManager) {
        this.powerManager = powerManager;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, PowerManager powerManager) {
        PowerCommand command = new PowerCommand(powerManager);

        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.literal("power")
                                .requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.literal("addpower").requires(source -> source.hasPermissionLevel(2))
                                        .then(CommandManager.argument("player", StringArgumentType.string())
                                                .then(CommandManager.argument("amount", StringArgumentType.string())
                                                        .executes(command::addPower))))
                                .then(CommandManager.literal("removepower").requires(source -> source.hasPermissionLevel(2))
                                        .then(CommandManager.argument("player", StringArgumentType.string())
                                                .then(CommandManager.argument("amount", StringArgumentType.string())
                                                        .executes(command::removePower))))
                                .then(CommandManager.literal("checkpower")
                                        .then(CommandManager.argument("player", StringArgumentType.string())
                                                .executes(command::checkPower)))
                        )
        );
    }

    private int addPower(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        BigInteger amount = new BigInteger(StringArgumentType.getString(context, "amount"));
        powerManager.addPower(playerName, amount);
        context.getSource().sendFeedback(() -> Text.literal("Added " + NumberFormatter.formatPrice(amount) + " power to " + playerName), true);
        return 1;
    }

    private int removePower(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        BigInteger amount = new BigInteger(StringArgumentType.getString(context, "amount"));
        powerManager.removePower(playerName, amount);
        context.getSource().sendFeedback(() -> Text.literal("Removed " + NumberFormatter.formatPrice(amount) + " power from " + playerName), true);
        return 1;
    }

    private int checkPower(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        BigInteger power = powerManager.getPower(playerName);
        context.getSource().sendFeedback(() -> Text.literal(playerName + " has " + NumberFormatter.formatPrice(power) + " power"), true);
        return 1;
    }
}
