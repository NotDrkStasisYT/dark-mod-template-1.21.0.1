package net.drk.Z28;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ChangeNicknameCommand {

    // Store player nicknames
    private static final Map<ServerPlayerEntity, String> playerNicknames = new HashMap<>();

    // Register the command
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("setNickname")
                        .then(CommandManager.argument("nickname", StringArgumentType.string())
                                .executes(context -> {
                                    // Get the player who executed the command
                                    ServerPlayerEntity player = context.getSource().getPlayer();

                                    // Get the new nickname
                                    String newNickname = StringArgumentType.getString(context, "nickname");

                                    // Set the nickname for the player
                                    setNickname(player, newNickname);

                                    // Notify the player
                                    player.sendMessage(Text.literal("Your nickname has been changed to: " + newNickname), false);

                                    return 1;  // Success
                                })
                        )
                )
        );
    }

    // Set the player's nickname
    private static void setNickname(ServerPlayerEntity player, String nickname) {
        playerNicknames.put(player, nickname);
    }

    // Get the player's nickname (fallback to default username if no nickname is set)
    public static String getNickname(ServerPlayerEntity player) {
        return playerNicknames.getOrDefault(player, player.getName().getString());
    }
}
