package net.drk.command.shield;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.number.NumberFormatter;
import net.drk.player.ShieldManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.math.BigInteger;

public class ShieldCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("setshield")
                .requires(source -> source.hasPermissionLevel(2))

                .then(CommandManager.argument("amount", StringArgumentType.string())
                        .executes(context -> {
                            String amountStr = StringArgumentType.getString(context, "amount");
                            try {
                                BigInteger amount = new BigInteger(amountStr);
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player != null) {
                                    ShieldManager shieldManager = ShieldManager.getInstance();
                                    shieldManager.setMaxShield(player, amount);
                                    player.sendMessage(Text.literal("Max shield set to " + NumberFormatter.formatPrice(amount)), false);
                                }
                            } catch (NumberFormatException e) {
                                context.getSource().sendFeedback(() -> Text.literal("Invalid number format for shield."), false);
                            }
                            return Command.SINGLE_SUCCESS;
                        })));

        dispatcher.register(CommandManager.literal("setshieldregen")
                .requires(source -> source.hasPermissionLevel(2))

                .then(CommandManager.argument("amount", StringArgumentType.string())
                        .executes(context -> {
                            String amountStr = StringArgumentType.getString(context, "amount");
                            try {
                                BigInteger amount = new BigInteger(amountStr);
                                ShieldManager shieldManager = ShieldManager.getInstance();
                                shieldManager.setShieldRegenerationRate(context.getSource().getPlayer(), amount);
                                context.getSource().sendFeedback(() -> Text.literal("Shield regeneration rate set to " + NumberFormatter.formatPrice(amount)), false);
                            } catch (NumberFormatException e) {
                                context.getSource().sendFeedback(() -> Text.literal("Invalid number format for regeneration rate."), false);
                            }
                            return Command.SINGLE_SUCCESS;
                        })));

        dispatcher.register(CommandManager.literal("checkshield")
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    if (player != null) {
                        ShieldManager shieldManager = ShieldManager.getInstance();
                        BigInteger currentShield = shieldManager.getShield(player);
                        BigInteger maxShield = shieldManager.getMaxShield(player);
                        context.getSource().sendFeedback(() -> Text.literal("Current shield: " + NumberFormatter.formatPrice(currentShield) + " / Max shield: " + NumberFormatter.formatPrice(maxShield)), false);
                    }
                    return Command.SINGLE_SUCCESS;
                }));
    }
}
