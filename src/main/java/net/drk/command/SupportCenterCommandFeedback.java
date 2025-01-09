package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.util.Formatting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SupportCenterCommandFeedback {

    public static void saveFeedbackMessages(List<FeedbackEntry> messages) {
    }

    // Class to store feedback with timestamp
    public static class FeedbackEntry {
        private final String playerName;
        private final String message;
        private final LocalDateTime timestamp;

        public FeedbackEntry(String playerName, String message, LocalDateTime timestamp) {
            this.playerName = playerName;
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String formattedMessage() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return String.format("[%s] %s: %s", timestamp.format(formatter), playerName, message);
        }

        public void addReply(Text fullReply) {
        }

        public String getPlayerMessage() {
            return "";
        }

        public void setAdminResponse(String adminMessage) {
        }

        public String getAdminResponse() {
            return "";
        }
    }

    // List to store feedback entries
    private static final List<FeedbackEntry> feedbackMessages = new ArrayList<>();

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("supportcenter")
                        .then(CommandManager.literal("feedback")
                                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String feedbackMessage = StringArgumentType.getString(context, "message");
                                            ServerCommandSource source = context.getSource();
                                            String playerName = source.getName();
                                            LocalDateTime now = LocalDateTime.now();

                                            // Store the feedback entry
                                            feedbackMessages.add(new FeedbackEntry(playerName, feedbackMessage, now));

                                            // Confirm to the player with formatted text
                                            source.sendFeedback(() -> Text.literal("§aThank you for your feedback!").formatted(Formatting.GREEN)
                                                    .append(Text.literal("\n§7Your message: ").formatted(Formatting.GRAY)
                                                            .append(Text.literal(feedbackMessage).formatted(Formatting.WHITE))), false);

                                            return 1;
                                        })))));
    }

    // Method to get all feedback messages
    public static List<FeedbackEntry> getFeedbackMessages() {
        return feedbackMessages;
    }

    public class FeedbackFilter {

        public static List<SupportCenterCommandFeedback.FeedbackEntry> filterFeedbackByTimeRange(
                List<SupportCenterCommandFeedback.FeedbackEntry> allFeedback, String timeRange) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate = null;

            switch (timeRange.toLowerCase()) {

                case "today":
                    startDate = now.toLocalDate().atStartOfDay();
                    break;
                case "last 3 days":
                    startDate = now.minusDays(3);
                    break;
                case "last week":
                    startDate = now.minusWeeks(1);
                    break;
                case "last month":
                    startDate = now.minusMonths(1);
                    break;
                default:
                    return allFeedback; // No filter applied if the range is unknown
            }

            LocalDateTime finalStartDate = startDate;
            return allFeedback.stream()
                    .filter(entry -> entry.getTimestamp().isAfter(finalStartDate))
                    .collect(Collectors.toList());
        }
    }
}
