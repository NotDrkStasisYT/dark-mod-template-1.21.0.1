package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class FilterplayerFeedbackCommand {

    private static final int MESSAGES_PER_PAGE = 5; // Number of messages per page

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.literal("admin")
                                .requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.literal("supportcenter")
                                .then(CommandManager.literal("feedback")
                                        .then(CommandManager.literal("filter")
                                                .then(CommandManager.literal("player")

                                                .then(CommandManager.argument("player", StringArgumentType.word())
                                                .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                        .executes(FilterplayerFeedbackCommand::executeFilterFeedbackByPlayer)
                                                )
                                                .executes(context -> executeFilterFeedbackByPlayer(context, StringArgumentType.getString(context, "player"), 1))
                                        )
                                )
                        )
        ))));
    }

    private static int executeFilterFeedbackByPlayer(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        int page = context.getArgument("page", Integer.class);
        return executeFilterFeedbackByPlayer(context, playerName, page);
    }

    private static int executeFilterFeedbackByPlayer(CommandContext<ServerCommandSource> context, String playerName, int page) {
        ServerCommandSource source = context.getSource();

        List<SupportCenterCommandFeedback.FeedbackEntry> allMessages = SupportCenterCommandFeedback.getFeedbackMessages();
        List<SupportCenterCommandFeedback.FeedbackEntry> filteredMessages = allMessages.stream()
                .filter(entry -> entry.getPlayerName().equalsIgnoreCase(playerName))
                .toList();

        int totalPages = (int) Math.ceil((double) filteredMessages.size() / MESSAGES_PER_PAGE);

        if (page > totalPages) {
            source.sendFeedback(()->Text.literal("§cPage number exceeds total pages.").formatted(Formatting.RED), false);
            return 1;
        }

        source.sendFeedback(()->Text.literal("§6--- Feedback Messages from " + playerName + " (Page " + page + "/" + totalPages + ") ---").formatted(Formatting.GOLD), false);

        int startIndex = (page - 1) * MESSAGES_PER_PAGE;
        int endIndex = Math.min(startIndex + MESSAGES_PER_PAGE, filteredMessages.size());

        for (int i = startIndex; i < endIndex; i++) {
            SupportCenterCommandFeedback.FeedbackEntry entry = filteredMessages.get(i);
            String formattedMessage = entry.formattedMessage();
            source.sendFeedback(()->Text.literal("§7- ").formatted(Formatting.GRAY)
                    .append(Text.literal(formattedMessage).formatted(Formatting.WHITE)), false);
        }

        return 1;
    }
}
