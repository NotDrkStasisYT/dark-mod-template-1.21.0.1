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

public class AcceptFriendCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("friends")
                        .then(CommandManager.literal("accept")
                                .then(CommandManager.argument("friend", EntityArgumentType.player())
                                        .executes(context -> {
                                            ServerPlayerEntity player = context.getSource().getPlayer();

                                            try {
                                                ServerPlayerEntity requesterPlayer = EntityArgumentType.getPlayer(context, "friend");

                                                UUID playerUUID = player.getUuid();
                                                UUID requesterUUID = requesterPlayer.getUuid();


                                                if (playerUUID.equals(requesterUUID)) {
                                                    context.getSource().sendError(Text.translatable("shatterpoint.friends.cannot_accept_self"));
                                                } else if (FriendListManager.acceptFriendRequest(playerUUID, requesterUUID)) {
                                                    context.getSource().sendFeedback(() -> Text.translatable("shatterpoint.friends.added_to_list", requesterPlayer.getName()), false);
                                                    requesterPlayer.sendMessage(Text.translatable("shatterpoint.friends.request_accepted", requesterPlayer.getName()), false);
                                                } else {
                                                    context.getSource().sendFeedback(() -> Text.translatable("shatterpoint.friends.no_request_found", requesterPlayer.getName()), false);
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
