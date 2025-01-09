package net.drk.command.player;

import com.mojang.brigadier.CommandDispatcher;
import net.drk.FakePlayer;
import net.drk.FakePlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class SpawnFakePlayerCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("spawnfakeplayer")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();
                            ServerWorld world = (ServerWorld) player.getWorld();
                            BlockPos playerPos = player.getBlockPos();

                            // Create and spawn the fake player with a unique UUID
                            FakePlayer fakePlayer = FakePlayerManager.createFakePlayer(context.getSource().getServer(), world, "FakePlayer", playerPos.getX(), playerPos.getY(), playerPos.getZ());
                            fakePlayer.changeGameMode(GameMode.CREATIVE); // Ensure the player is visible in CREATIVE mode

                            // Send confirmation message
                            player.sendMessage(Text.literal("Spawned fake player at your location!"), false);
                            return 1;
                        })
                )
        );
    }
}
