package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class AddFriendCommand {

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("friends")
                        .then(CommandManager.literal("forcefriend").requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.argument("friend", EntityArgumentType.player()).executes(AddFriendCommand::execute)))));

    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerPlayerEntity friend = EntityArgumentType.getPlayer(context, "friend");

        if (player == null || friend == null) {
            return 0; // Command can only be run by a player and a friend must be specified
        }

        UUID playerUUID = player.getUuid();
        UUID friendUUID = friend.getUuid();

        if (FriendListManager.isFriend(playerUUID, friendUUID)) {
            context.getSource().sendFeedback(() -> Text.literal(friend.getName() + " is already on your friends list!"), false);


        } else {
            FriendListManager.addFriend(playerUUID, friendUUID);
            context.getSource().sendFeedback(() -> Text.literal(friend.getName() + "  has been added to your friends list!"), false);
        }

        return Command.SINGLE_SUCCESS;
    }
}

