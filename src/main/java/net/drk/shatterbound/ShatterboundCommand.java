package net.drk.shatterbound;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.shatterbound.ShatterboundManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Supplier;

public class ShatterboundCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2)) // Admin permission level
                        .then(CommandManager.literal("admin")
                                .then(CommandManager.literal("shatterbind")
                                        .then(CommandManager.argument("player1", StringArgumentType.string())
                                                .then(CommandManager.argument("player2", StringArgumentType.string())
                                                        .executes(context -> {
                                                            String player1Name = StringArgumentType.getString(context, "player1");
                                                            String player2Name = StringArgumentType.getString(context, "player2");
                                                            ServerCommandSource source = context.getSource();

                                                            // Retrieve the players by name
                                                            ServerPlayerEntity player1 = source.getServer().getPlayerManager().getPlayer(player1Name);
                                                            ServerPlayerEntity player2 = source.getServer().getPlayerManager().getPlayer(player2Name);

                                                            if (player1 == null || player2 == null) {
                                                                source.sendFeedback((Supplier<Text>) Text.literal("§cOne or both players are not online or don't exist."), false);
                                                                return 1;
                                                            }

                                                            // Attempt to add a Shatterbound relationship between the two players
                                                            if (ShatterboundManager.addShatterbound(player1Name, player2Name)) {
                                                                source.sendFeedback((Supplier<Text>) Text.literal("§aSuccessfully Shatterbound " + player1Name + " and " + player2Name).formatted(Formatting.GREEN), false);
                                                                player1.sendMessage(Text.literal("You are now Shatterbound with " + player2Name + "!").formatted(Formatting.AQUA), false);
                                                                player2.sendMessage(Text.literal("You are now Shatterbound with " + player1Name + "!").formatted(Formatting.AQUA), false);
                                                            } else {
                                                                source.sendFeedback((Supplier<Text>) Text.literal("§cFailed to Shatterbind the players. Either they are already Shatterbound or group rules are not met.").formatted(Formatting.RED), false);
                                                            }

                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
        );
    }
}
