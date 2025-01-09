package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreCommand {

    private static final long COOLDOWN_MS = 60000; // Cooldown period in milliseconds (e.g., 60 seconds)
    private static final Map<UUID, Long> playerCooldowns = new HashMap<>(); // Tracks the last time a boat was spawned per player

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("Shatterpoint")
                .then(CommandManager.literal("boat").executes(ScoreCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        UUID playerUUID = source.getPlayer().getUuid();
        long currentTime = System.currentTimeMillis();
        Long lastSpawnTime = playerCooldowns.get(playerUUID);

        if (lastSpawnTime != null && (currentTime - lastSpawnTime < COOLDOWN_MS)) {
            long timeLeft = COOLDOWN_MS - (currentTime - lastSpawnTime);
            long secondsLeft = timeLeft / 1000;
            long millisecondsLeft = timeLeft % 1000 / 100;
            String timeLeftStr = String.format("%d.%d seconds", secondsLeft, millisecondsLeft);
            source.sendFeedback(() -> Text.literal("You need to wait " + timeLeftStr + " before summoning another boat."), true);
            return 0; // Indicate failure due to cooldown
        }

        BlockPos playerPos = source.getPlayer().getBlockPos();
        String positionString = "(" + playerPos.getX() + ", " + playerPos.getY() + ", " + playerPos.getZ() + ")";
        Vec3d lookVec = source.getPlayer().getRotationVec(1.0F);
        World world = source.getWorld();

        Vec3d spawnPos = source.getPlayer().getPos().add(lookVec.normalize().multiply(3)); // 2 blocks in front of the player

        List<BoatEntity> nearbyBoats = world.getEntitiesByClass(BoatEntity.class,
                new Box(spawnPos.getX() - 5, spawnPos.getY() - 5, spawnPos.getZ() - 5,
                        spawnPos.getX() + 5, spawnPos.getY() + 5, spawnPos.getZ() + 5),
                boat -> true);

        boolean allBoatsFull = true;
        for (BoatEntity boat : nearbyBoats) {
            if (boat.getPassengerList().size() < 2) {
                allBoatsFull = false;
                break;
            }
        }

        if (!nearbyBoats.isEmpty() && !allBoatsFull) {
            source.sendFeedback(() -> Text.literal("A boat is already nearby! Cannot summon another one."), true);
            return 0; // Indicate failure to spawn a boat
        }

        // Create and spawn the boat entity
        BoatEntity boat = EntityType.BOAT.create(world);
        if (boat != null) {
            boat.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);
            world.spawnEntity(boat);
            source.sendFeedback(() -> Text.literal("Summoned Boat at " + positionString), true);
            playerCooldowns.put(playerUUID, currentTime); // Update the player's cooldown
        }

        return 1;
    }
}
