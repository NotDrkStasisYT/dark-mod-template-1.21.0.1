package net.drk.command.display;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.drk.ActionbarManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ShowCurrentDisplayCommand {

    // Command registration method
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, ActionbarManager actionBarManager) {
        dispatcher.register(CommandManager.literal("showcurrentdisplay")
                .requires(source -> source.hasPermissionLevel(2))

                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    if (player != null) {
                        // Call a method to show the current display on the action bar
                        showCurrentDisplay(player, actionBarManager);
                    }
                    return Command.SINGLE_SUCCESS;
                }));
    }

    private static void showCurrentDisplay(ServerPlayerEntity player, ActionbarManager actionBarManager) {
        // Get the current display index from the action bar manager
        int currentIndex = actionBarManager.getCurrentDisplayIndex();

        // Optionally, display the current index in chat
        player.sendMessage(Text.literal("Current action bar display index: " + currentIndex), false);

        // Force the update to the action bar based on the current index
        actionBarManager.updateActionBar();
    }
}
