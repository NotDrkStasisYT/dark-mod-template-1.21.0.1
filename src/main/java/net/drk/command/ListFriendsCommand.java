package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;

public class ListFriendsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("friends")
                        .then(CommandManager.literal("list")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();
                                    UUID playerUUID = player.getUuid();

                                    // Get the list of friends' names
                                    List<String> friends = FriendListManager.getFriendsList(playerUUID);

                                    // Format the list into a readable string
                                    if (friends.isEmpty()) {
                                        source.sendFeedback(() -> Text.translatable("shatterpoint.friends.no_friends"), false);
                                    } else {
                                        String friendsList = String.join(", ", friends);
                                        source.sendFeedback(() -> Text.translatable("shatterpoint.friends.list", friendsList), false);
                                    }

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
