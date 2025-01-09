// Assuming you have a command class where you handle player input
package net.drk.AI;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.AI.AIChatbot;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.text.Text;

public class ChatbotCommand {
    private static final AIChatbot chatbot = new AIChatbot();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("Chat")
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String playerName = context.getSource().getName();
                                    String message = StringArgumentType.getString(context, "message");

                                    // Process the message and get a response
                                    chatbot.onPlayerMessage(playerName, message);

                                    // You could also send the response back to the player
                                    context.getSource().sendFeedback(()->
                                            Text.literal(chatbot.getResponse(message)), false
                                    );

                                    return 1;
                                })
                        )
                ));
    }
}