package net.drk.Z28;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.Z28.EffectManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AddEffectCommand {

    // Register the command
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("addEffect")
                        .then(CommandManager.argument("effect", StringArgumentType.string())
                                .executes(context -> {
                                    // Get the player who executed the command
                                    ServerPlayerEntity player = context.getSource().getPlayer();

                                    // Get the effect argument
                                    String effect = StringArgumentType.getString(context, "effect");

                                    // Apply the effect to the player (example logic, replace with actual effect handling)
                                    if (EffectManager.hasEffect(effect)) {
                                        EffectManager.applyEffectToPlayer(player, effect);
                                        player.sendMessage(Text.literal("Effect " + effect + " applied to your nickname!"), false);
                                    } else {
                                        player.sendMessage(Text.literal("Unknown effect: " + effect), false);
                                    }

                                    return 1;  // Success
                                })
                        )
                )
        );
    }
}
