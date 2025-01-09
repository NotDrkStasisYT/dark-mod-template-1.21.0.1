package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AdminFeedbackReplyCommand {

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
                                                .then(CommandManager.literal("reply")
                                                        .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                                .then(CommandManager.argument("messageNumber", IntegerArgumentType.integer(1))
                                                                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                                                                .requires(source -> source.hasPermissionLevel(2))
                                                                                .executes(context -> {
                                                                                    int page = IntegerArgumentType.getInteger(context, "page");
                                                                                    int messageNumber = IntegerArgumentType.getInteger(context, "messageNumber");
                                                                                    String adminMessage = StringArgumentType.getString(context, "message");
                                                                                    ServerCommandSource source = context.getSource();

                                                                                    // Retrieve all messages
                                                                                    List<SupportCenterCommandFeedback.FeedbackEntry> messages = SupportCenterCommandFeedback.getFeedbackMessages();
                                                                                    if (messageNumber < 1 || messageNumber > messages.size()) {
                                                                                        source.sendFeedback(
                                                                                                ()->Text.literal("§cInvalid message number.").formatted(Formatting.RED),
                                                                                                false
                                                                                        );
                                                                                        return 1;
                                                                                    }

                                                                                    SupportCenterCommandFeedback.FeedbackEntry entry = messages.get(messageNumber - 1);
                                                                                    entry.setAdminResponse(adminMessage);

                                                                                    source.sendFeedback(
                                                                                            ()->Text.literal("§aSuccessfully replied to feedback:").formatted(Formatting.GREEN)
                                                                                                    .append(Text.literal("\n§7Your message: " + entry.getPlayerMessage()).formatted(Formatting.GRAY))
                                                                                                    .append(Text.literal("\n§aAdmin response: " + adminMessage).formatted(Formatting.GREEN)),
                                                                                            false
                                                                                    );

                                                                                    return 1;
                                                                                })
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }
}
