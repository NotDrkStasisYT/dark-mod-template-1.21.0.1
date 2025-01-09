package net.drk;

import com.mojang.authlib.GameProfile;
import net.drk.FakePlayer;
import net.fabricmc.fabric.impl.event.interaction.FakePlayerNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;
import java.util.UUID;
import com.google.common.collect.MapMaker;
import net.minecraft.world.GameMode;

public class FakePlayerManager {

    // Cache of fake players to prevent redundant instantiations
    private static final Map<FakePlayerKey, FakePlayer> FAKE_PLAYER_MAP = new MapMaker().weakValues().makeMap();

    // Inner class for keying the fake player map
    private record FakePlayerKey(ServerWorld world, GameProfile profile) {
    }

    /**
     * Retrieve a fake player for the specified world and game profile.
     * Reuses instances for the same parameters, ensuring only one fake player is created per combination.
     */
    public static FakePlayer get(ServerWorld world, GameProfile profile) {
        // Use the cache to retrieve or create a new fake player
        return FAKE_PLAYER_MAP.computeIfAbsent(new FakePlayerKey(world, profile), key -> new FakePlayer(key.world, key.profile));
    }

    /**
     * Creates and spawns a fake player in the world with a unique UUID and name.
     */
    public static FakePlayer createFakePlayer(MinecraftServer server, ServerWorld world, String playerName, double x, double y, double z) {
        // Create a unique UUID for each fake player
        UUID uniqueUUID = UUID.randomUUID(); // Generate a unique UUID for this fake player
        GameProfile profile = new GameProfile(uniqueUUID, playerName);
        FakePlayer fakePlayer = get(world, profile);

        // Set position
        fakePlayer.setPosition(x, y, z);

        // Manually send player info to clients
        PlayerManager playerManager = server.getPlayerManager();
        playerManager.sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, fakePlayer));

        // Add the player to the world
        world.spawnEntity(fakePlayer);

        // Now add the fake player to the player list
        playerManager.getPlayerList().add(fakePlayer);

        // Manually send the game mode info to clients
        playerManager.sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, fakePlayer));

        // Set the player's game mode manually
        fakePlayer.changeGameMode(GameMode.SPECTATOR); // Set Creative mode for visibility

        return fakePlayer;
    }
}