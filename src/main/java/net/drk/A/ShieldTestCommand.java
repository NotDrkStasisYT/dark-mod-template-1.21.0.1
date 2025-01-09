package net.drk.A;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.math.BigInteger;

public class ShieldTestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("testshield")
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    BigInteger newShield = BigInteger.valueOf(500); // Example new shield value
                    BigInteger maxShield = BigInteger.valueOf(1000); // Example max shield value

                    ShieldNetworking.sendShieldDataToPlayer(player); // Update the player's shield data

                    context.getSource().sendFeedback(()->Text.of("Sent new shield data to player."), false);
                    return 1;
                })
        );
    }
}
