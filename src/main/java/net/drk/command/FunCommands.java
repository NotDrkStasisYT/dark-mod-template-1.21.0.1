package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.drk.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class FunCommands {

    private static final String[] JOKES = {
            "Why don't scientists trust atoms? Because they make up everything!",
            "Why did the scarecrow win an award? Because he was outstanding in his field!",
            "Why don’t programmers like nature? It has too many bugs."
    };

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("joke").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("give").executes(FunCommands::run)));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        Random random = new Random();
        String joke = JOKES[random.nextInt(JOKES.length)];
        context.getSource().sendFeedback(() -> Text.literal(joke), false);
        return 1;
    }

    }

