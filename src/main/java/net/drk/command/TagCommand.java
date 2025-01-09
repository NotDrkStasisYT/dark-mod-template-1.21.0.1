package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.server.command.ServerCommandSource;

public class TagCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("addtag")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.argument("player", StringArgumentType.word())
                                .then(CommandManager.argument("tag", StringArgumentType.word())
                                        .executes(TagCommand::execute))
                        ));
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String playerName = StringArgumentType.getString(context, "player");
        String tag = StringArgumentType.getString(context, "tag");

        ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);

        if (player != null) {
            player.getCommandTags().add(tag);
            source.sendFeedback(()-> Text.of("Tag added to " + playerName), true);
            return 1;
        } else {
            source.sendError(Text.of("Player not found: " + playerName));
            return 0;
        }
    }
}
