package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;

import java.util.UUID;

public class RequestFriendCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("friends")
                        .then(CommandManager.literal("request")
                                .then(CommandManager.argument("friend", EntityArgumentType.player())
                                        .executes(context -> {
                                            ServerPlayerEntity player = context.getSource().getPlayer();

                                            try {
                                                ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "friend");

                                                UUID playerUUID = player.getUuid();
                                                UUID targetUUID = targetPlayer.getUuid();

                                                if (playerUUID.equals(targetUUID)) {
                                                    context.getSource().sendError(Text.translatable("shatterpoint.friends.cannot_request_self"));
                                                } else if (FriendListManager.isFriend(playerUUID, targetUUID)) {
                                                    context.getSource().sendFeedback(() -> Text.translatable("shatterpoint.friends.already_friends", targetPlayer.getName()), false);

                                                } else if (FriendListManager.hasPendingRequest(targetUUID, playerUUID)) {
                                                    context.getSource().sendFeedback(() -> Text.translatable("shatterpoint.friends.request_sent", targetPlayer.getName()), false);

                                                } else {
                                                    FriendListManager.sendFriendRequest(playerUUID, targetUUID);
                                                    targetPlayer.sendMessage(Text.translatable("shatterpoint.friends.request_received", player.getName(), player.getName()), false);

                                                }

                                            } catch (CommandSyntaxException e) {
                                                context.getSource().sendError(Text.of("Could not find the specified player."));
                                            }

                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                ));
    }
}