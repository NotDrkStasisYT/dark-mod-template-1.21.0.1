package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.util.Formatting;

import java.util.List;

public class FilterFeedbackCommand {

    private static final int MESSAGES_PER_PAGE = 5; // Number of messages per page
    private static final List<String> ALLOWED_RANGES = List.of("day", "last 2 days", "week");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.literal("admin")
                                .requires(source -> source.hasPermissionLevel(2))

                                .then(CommandManager.literal("supportcenter")
                                        .then(CommandManager.literal("feedback")
                                                .then(CommandManager.literal("filter")
                                                        .then(CommandManager.literal("time")
                                                        .then(CommandManager.argument("range", StringArgumentType.word())
                                                        .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                                .executes(context -> {
                                                                    String timeRange = StringArgumentType.getString(context, "range");
                                                                    int page = IntegerArgumentType.getInteger(context, "page");

                                                                    // Check if the range is valid
                                                                    if (!ALLOWED_RANGES.contains(timeRange)) {
                                                                        context.getSource().sendFeedback(
                                                                                ()->Text.literal("§cInvalid range. Allowed ranges are: day, last 2 days, week.").formatted(Formatting.RED),
                                                                                false
                                                                        );
                                                                        return 1;
                                                                    }

                                                                    return executeFilterFeedback(context, timeRange, page);
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
        )));
    }

    private static int executeFilterFeedback(CommandContext<ServerCommandSource> context, String timeRange, int page) {
        ServerCommandSource source = context.getSource();

        List<SupportCenterCommandFeedback.FeedbackEntry> allMessages = SupportCenterCommandFeedback.getFeedbackMessages();
        List<SupportCenterCommandFeedback.FeedbackEntry> filteredMessages = SupportCenterCommandFeedback.FeedbackFilter.filterFeedbackByTimeRange(allMessages, timeRange);
        int totalPages = (int) Math.ceil((double) filteredMessages.size() / MESSAGES_PER_PAGE);

        if (page > totalPages) {
            source.sendFeedback(
                    ()->Text.literal("§cPage number exceeds total pages.").formatted(Formatting.RED),
                    false
            );
            return 1;
        }

        source.sendFeedback(
                ()->Text.literal("§6--- Feedback Messages (Page " + page + "/" + totalPages + ") for " + timeRange + " ---").formatted(Formatting.GOLD),
                false
        );

        int startIndex = (page - 1) * MESSAGES_PER_PAGE;
        int endIndex = Math.min(startIndex + MESSAGES_PER_PAGE, filteredMessages.size());

        for (int i = startIndex; i < endIndex; i++) {
            SupportCenterCommandFeedback.FeedbackEntry entry = filteredMessages.get(i);
            String formattedMessage = entry.formattedMessage();
            source.sendFeedback(
                    ()->Text.literal("§7- ").formatted(Formatting.GRAY)
                            .append(Text.literal(formattedMessage).formatted(Formatting.WHITE)),
                    false
            );
        }

        return 1;
    }
}
