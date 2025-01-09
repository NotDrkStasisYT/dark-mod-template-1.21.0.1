package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ModifyAlphaCommand {
    private static final Map<String, Float> weaponAlphas = new HashMap<>(); // Track alpha per weapon

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("modifyAlpha")
                        .then(CommandManager.argument("weaponName", StringArgumentType.string())
                                .then(CommandManager.argument("action", StringArgumentType.string())
                                        .then(CommandManager.argument("value", FloatArgumentType.floatArg())
                                                .executes(context -> {
                                                    String weaponName = StringArgumentType.getString(context, "weaponName");
                                                    String action = StringArgumentType.getString(context, "action");
                                                    float value = FloatArgumentType.getFloat(context, "value");

                                                    modifyAlpha(context.getSource(), weaponName, action, value);
                                                    return 1;
                                                })
                                        )
                                )
                        ));
    }

    private static void modifyAlpha(ServerCommandSource source, String weaponName, String action, float value) {
        float currentAlpha = weaponAlphas.getOrDefault(weaponName, 1.0f); // Default alpha is 1.0

        if ("increase".equalsIgnoreCase(action)) {
            currentAlpha += value;
        } else if ("set".equalsIgnoreCase(action)) {
            currentAlpha = value;
        } else {
            source.sendError(Text.literal("Invalid action. Use 'increase' or 'set'."));
            return;
        }

        weaponAlphas.put(weaponName, currentAlpha);
        float finalCurrentAlpha = currentAlpha;
        source.sendFeedback(()->Text.literal("Alpha for " + weaponName + " is now: " + finalCurrentAlpha), false);
    }
}