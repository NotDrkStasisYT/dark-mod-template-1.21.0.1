package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.drk.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class TeleportCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("Shatterpoint").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("teleport")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("set").executes(TeleportCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        IEntityDataSaver player = ((IEntityDataSaver) context.getSource().getPlayer());
        BlockPos playerPos = context.getSource().getPlayer().getBlockPos();
        String positionString = "(" + playerPos.getX() + ", " + playerPos.getY() + ", " + playerPos.getZ() + ")";

        player.getPersistentData().putIntArray("drk.homepos",
                new int[] { playerPos.getX(), playerPos.getY(), playerPos.getZ() });

        return 1;
    }
}
