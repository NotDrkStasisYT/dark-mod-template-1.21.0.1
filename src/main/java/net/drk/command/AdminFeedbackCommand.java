package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.util.Formatting;

import java.util.List;

public class AdminFeedbackCommand {

    static final int MESSAGES_PER_PAGE = 5; // Number of messages per page

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
                                                .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            int page = IntegerArgumentType.getInteger(context, "page");
                                                            ServerCommandSource source = context.getSource();

                                                            List<SupportCenterCommandFeedback.FeedbackEntry> messages = SupportCenterCommandFeedback.getFeedbackMessages();
                                                            int totalPages = (int) Math.ceil((double) messages.size() / MESSAGES_PER_PAGE);

                                                            if (page > totalPages) {
                                                                source.sendFeedback(
                                                                        ()->Text.literal("§cPage number exceeds total pages.").formatted(Formatting.RED),
                                                                        false
                                                                );
                                                                return 1;
                                                            }

                                                            source.sendFeedback(
                                                                    ()->Text.literal("§6--- Feedback Messages (Page " + page + "/" + totalPages + ") ---").formatted(Formatting.GOLD),
                                                                    false
                                                            );

                                                            int startIndex = (page - 1) * MESSAGES_PER_PAGE;
                                                            int endIndex = Math.min(startIndex + MESSAGES_PER_PAGE, messages.size());

                                                            for (int i = startIndex; i < endIndex; i++) {
                                                                SupportCenterCommandFeedback.FeedbackEntry entry = messages.get(i);
                                                                String formattedMessage = entry.formattedMessage();
                                                                int messageNumber = i - startIndex + 1; // Calculate message number for this page
                                                                source.sendFeedback(
                                                                        ()->Text.literal("§7" + messageNumber + ". ").formatted(Formatting.GRAY)
                                                                                .append(Text.literal(formattedMessage).formatted(Formatting.WHITE)),
                                                                        false
                                                                );
                                                            }

                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
        );
    }
}
