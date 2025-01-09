package net.drk.command.Settings;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.drk.command.shield.PlayerShieldStyleManager;
import net.drk.command.shield.ShieldStyle;
import net.drk.command.shield.styles.*;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class OvershieldSettingsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("settings")
                        .then(CommandManager.literal("actionbar")
                                .then(CommandManager.literal("overshield")
                                        .then(CommandManager.literal("styles")
                                                .then(CommandManager.literal("shieldstyle")


                                                        .then(CommandManager.literal("default").executes(context -> setStyle(context.getSource().getPlayer(), new DefaultStyle())))
                                        .then(CommandManager.literal("monotone").executes(context -> setStyle(context.getSource().getPlayer(), new MonotoneStyle())))
                                        .then(CommandManager.literal("fire").executes(context -> setStyle(context.getSource().getPlayer(), new FireStyle())))
                                        .then(CommandManager.literal("ice").executes(context -> setStyle(context.getSource().getPlayer(), new IceStyle())))
                                        .then(CommandManager.literal("storm").requires(source -> source.getPlayer().getCommandTags().contains("stormstyle")).executes(context -> setStyle(context.getSource().getPlayer(), new StormStyle())))
                                        .then(CommandManager.literal("earth").requires(source -> source.getPlayer().getCommandTags().contains("earthstyle")).executes(context -> setStyle(context.getSource().getPlayer(), new EarthStyle())))
                                )
                        )
                )
        )));
    }

    private static int setStyle(ServerPlayerEntity player, ShieldStyle style) {
        if (player != null) {
            PlayerShieldStyleManager.setStyle(player, style);
        }
        return Command.SINGLE_SUCCESS;
    }
}
