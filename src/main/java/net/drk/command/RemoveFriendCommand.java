package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;

import java.util.UUID;

public class RemoveFriendCommand {

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("friends")
                        .then(CommandManager.literal("unfriend")

                                .then(CommandManager.argument("friend", EntityArgumentType.player()).executes(RemoveFriendCommand::execute)))));

    }

    private static int execute(CommandContext<ServerCommandSource> context) {

        ServerPlayerEntity player = context.getSource().getPlayer();

        try {
            ServerPlayerEntity friend = EntityArgumentType.getPlayer(context, "friend");

            UUID playerUUID = player.getUuid();
            UUID friendUUID = friend.getUuid();

            if (!FriendListManager.isFriend(playerUUID, friendUUID)) {
                context.getSource().sendFeedback(() -> Text.translatable("shatterpoint.friends.not_on_list", friend.getName()), false);

            } else {
                FriendListManager.removeFriend(playerUUID, friendUUID);
                context.getSource().sendFeedback(() -> Text.translatable("shatterpoint.friends.removed_from_list", friend.getName()), false);

            }
        } catch (CommandSyntaxException e) {
            context.getSource().sendError(Text.of("Could not find the specified player."));
        }

        return Command.SINGLE_SUCCESS;
    }
}






