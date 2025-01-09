package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerFeedbackCommand {

    private static final int MESSAGES_PER_PAGE = 5; // Number of messages per page

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .then(CommandManager.literal("supportcenter")
                                .then(CommandManager.literal("adminrfeedback")
                                        .requires(source -> source.hasPermissionLevel(2))

                                        .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                .executes(context -> {
                                                    int page = IntegerArgumentType.getInteger(context, "page");
                                                    ServerCommandSource source = context.getSource();
                                                    String playerName = source.getName(); // Get the player's name

                                                    // Retrieve feedback messages
                                                    List<SupportCenterCommandFeedback.FeedbackEntry> allMessages = SupportCenterCommandFeedback.getFeedbackMessages();
                                                    List<SupportCenterCommandFeedback.FeedbackEntry> playerMessages = allMessages.stream()
                                                            .filter(entry -> entry.getPlayerName().equalsIgnoreCase(playerName))
                                                            .collect(Collectors.toList());

                                                    int totalPages = (int) Math.ceil((double) playerMessages.size() / MESSAGES_PER_PAGE);

                                                    if (page > totalPages) {
                                                        source.sendFeedback(
                                                                ()->Text.literal("§cPage number exceeds total pages.").formatted(Formatting.RED),
                                                                false
                                                        );
                                                        return 1;
                                                    }

                                                    source.sendFeedback(
                                                            ()->Text.literal("§6--- Your Feedback Messages (Page " + page + "/" + totalPages + ") ---").formatted(Formatting.GOLD),
                                                            false
                                                    );

                                                    int startIndex = (page - 1) * MESSAGES_PER_PAGE;
                                                    int endIndex = Math.min(startIndex + MESSAGES_PER_PAGE, playerMessages.size());

                                                    for (int i = startIndex; i < endIndex; i++) {
                                                        SupportCenterCommandFeedback.FeedbackEntry entry = playerMessages.get(i);
                                                        String playerMessage = entry.getPlayerMessage();
                                                        String adminResponse = entry.getAdminResponse() != null ? entry.getAdminResponse() : "No response yet";

                                                        int finalI = i;
                                                        source.sendFeedback(
                                                                ()->Text.literal("§7" + (finalI + 1) + ". ").formatted(Formatting.GRAY)
                                                                        .append(Text.literal("Your message: " + playerMessage).formatted(Formatting.WHITE))
                                                                        .append(Text.literal("\nAdmin response: " + adminResponse).formatted(Formatting.YELLOW)),
                                                                false
                                                        );
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }
}
