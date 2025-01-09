package net.drk.discord;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.discord.DiscordRichPresenceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PresenceCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("discord")
                        .then(CommandManager.literal("presence")
                                .then(CommandManager.argument("state", StringArgumentType.string())
                                        .then(CommandManager.argument("details", StringArgumentType.string())
                                                .executes(context -> {
                                                    ServerCommandSource source = context.getSource();
                                                    String playerName = source.getPlayer().getName().getString();
                                                    DiscordRichPresenceManager manager = DiscordRichPresenceManager.getManagerForPlayer(playerName);

                                                    if (manager == null) {
                                                        source.sendError(Text.literal("No Discord Rich Presence manager found for player.")
                                                                .formatted(Formatting.RED));
                                                        return 1;
                                                    }

                                                    // Get arguments from the command
                                                    String state = StringArgumentType.getString(context, "state");
                                                    String details = StringArgumentType.getString(context, "details");

                                                    // Update the player's presence
                                                    manager.updatePresence(state, details, "", 0, 0);

                                                    source.sendFeedback(()->Text.literal("Discord Rich Presence updated: ").formatted(Formatting.GREEN)
                                                            .append(Text.literal(state + " - " + details).formatted(Formatting.WHITE)), false);

                                                    return 1;
                                                })
                                        )
                                )
                                // Change Large Image
                                .then(CommandManager.literal("setLargeImage")
                                        .then(CommandManager.argument("imageKey", StringArgumentType.string())
                                                .executes(context -> {
                                                    ServerCommandSource source = context.getSource();
                                                    String playerName = source.getPlayer().getName().getString();
                                                    DiscordRichPresenceManager manager = DiscordRichPresenceManager.getManagerForPlayer(playerName);

                                                    if (manager == null) {
                                                        source.sendError(Text.literal("No Discord Rich Presence manager found for player.")
                                                                .formatted(Formatting.RED));
                                                        return 1;
                                                    }

                                                    // Get the large image key from the command
                                                    String imageKey = StringArgumentType.getString(context, "imageKey");

                                                    // Set the large image key
                                                    manager.setLargeImageKey(imageKey);

                                                    source.sendFeedback(()->Text.literal("Large image changed to: ").formatted(Formatting.GREEN)
                                                            .append(Text.literal(imageKey).formatted(Formatting.WHITE)), false);

                                                    return 1;
                                                })
                                        )
                                )
                                // Change Small Image
                                .then(CommandManager.literal("setSmallImage")
                                        .then(CommandManager.argument("imageKey", StringArgumentType.string())
                                                .executes(context -> {
                                                    ServerCommandSource source = context.getSource();
                                                    String playerName = source.getPlayer().getName().getString();
                                                    DiscordRichPresenceManager manager = DiscordRichPresenceManager.getManagerForPlayer(playerName);

                                                    if (manager == null) {
                                                        source.sendError(Text.literal("No Discord Rich Presence manager found for player.")
                                                                .formatted(Formatting.RED));
                                                        return 1;
                                                    }

                                                    // Get the small image key from the command
                                                    String imageKey = StringArgumentType.getString(context, "imageKey");

                                                    // Set the small image key
                                                    manager.setSmallImageKey(imageKey);

                                                    source.sendFeedback(()->Text.literal("Small image changed to: ").formatted(Formatting.GREEN)
                                                            .append(Text.literal(imageKey).formatted(Formatting.WHITE)), false);

                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }
}
