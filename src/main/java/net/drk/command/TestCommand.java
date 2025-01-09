package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.drk.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import  net.drk.command.ScoreCommand;

import java.util.function.Supplier;

public class TestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("redeem")

                        .then(CommandManager.argument("code", StringArgumentType.string()) // Argument to capture any string input
                        .executes(context -> {
                            String action = StringArgumentType.getString(context, "code");
                            return run(context.getSource(), action); // Pass the action to your command logic
                        }))));
    }

    // Command logic that handles the dynamic input
    private static int run(ServerCommandSource source, String action) {
        // Determine the action based on the input
        switch (action.toLowerCase()) {
            case "teleport":
                // Example action: send a message about teleportation
                source.sendFeedback(() -> Text.literal("Teleportation initiated!"), false);
                break;
            case "shatterpoint2024":
                source.sendFeedback(() -> Text.literal("Successfully redeemed!"), false);

                break;
            case "Boat":

                break;
            default:
                // Default action if the input doesn't match any cases
                source.sendFeedback(() -> Text.literal("Invalid code: " + action), false);
                break;
        }
        return 1; // Return success
    }
}