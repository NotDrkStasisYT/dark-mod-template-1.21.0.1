package net.drk.zodiac;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.drk.zodiac.ZodiacManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChooseZodiacCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .then(CommandManager.literal("zodiac")
                                .then(CommandManager.literal("choose")
                                        .then(CommandManager.argument("sign", StringArgumentType.string())
                                                .executes(context -> {
                                                    ServerCommandSource source = context.getSource();
                                                    ServerPlayerEntity player = source.getPlayer();
                                                    String zodiacInput = StringArgumentType.getString(context, "sign").toUpperCase();

                                                    // Validate zodiac sign
                                                    try {
                                                        ZodiacManager.ZodiacSign zodiacSign = ZodiacManager.ZodiacSign.valueOf(zodiacInput);

                                                        // Check if the player already has a zodiac
                                                        if (ZodiacManager.hasZodiac(player)) {
                                                            // Remove the existing zodiac before setting a new one
                                                            ZodiacManager.removePlayerZodiac(player);
                                                           // PlayerZodiacBuffManager.removeZodiacBuff(player); // Remove buffs associated with the old zodiac
                                                            player.sendMessage(Text.literal("Your previous zodiac sign has been replaced.")
                                                                    .formatted(Formatting.YELLOW), false);
                                                        }

                                                        // Set the new zodiac sign
                                                        ZodiacManager.setPlayerZodiac(player, zodiacSign);
                                                        // Apply the corresponding buff for the new zodiac sign
                                                        //PlayerZodiacBuffManager.applyZodiacBuff(player);

                                                    } catch (IllegalArgumentException e) {
                                                        player.sendMessage(Text.literal("Invalid zodiac sign. Please choose a valid zodiac sign.")
                                                                .formatted(Formatting.RED), false);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }
}
