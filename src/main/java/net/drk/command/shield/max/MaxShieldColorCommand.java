package net.drk.command.shield.max;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.command.shield.max.MaxShieldColorManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class MaxShieldColorCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("settings")
                        .then(CommandManager.literal("actionbar")
                                .then(CommandManager.literal("overshield")
                                        .then(CommandManager.literal("styles")
                                                .then(CommandManager.literal("maxshieldcolor")

                                                        .then(CommandManager.argument("color", StringArgumentType.word())
                                                        .executes(context -> {
                                                            String colorName = StringArgumentType.getString(context, "color").toUpperCase();
                                                            Formatting color = parseColor(colorName);

                                                            if (color != null) {
                                                                ServerPlayerEntity player = context.getSource().getPlayer();
                                                                if (player != null) {
                                                                    MaxShieldColorManager.setMaxShieldColor(player, color);
                                                                }
                                                            }
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                )
                        )
                )
        ));
    }

    private static Formatting parseColor(String colorName) {
        try {
            return Formatting.valueOf(colorName);
        } catch (IllegalArgumentException e) {
            // Handle invalid color input
            return null;
        }
    }
}
