package net.drk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BattleTracker {

    private static final Map<UUID, Long> playerLastActionTime = new HashMap<>();
    private static final Map<UUID, Boolean> playerInBattle = new HashMap<>();
    private static final long BATTLE_TIMEOUT = 30L * 20L; // 30 seconds in game ticks (20 ticks per second)

    public static void initialize() {
        // Register the tick event to check for players no longer in battle
        ServerTickEvents.END_WORLD_TICK.register(BattleTracker::onWorldTick);

        // Register the attack event to detect when a player attacks an entity
        AttackEntityCallback.EVENT.register(BattleTracker::onPlayerAttackEntity);
    }

    private static void onWorldTick(ServerWorld world) {
        long currentTick = world.getTime();

        for (ServerPlayerEntity player : world.getPlayers()) {
            UUID playerId = player.getUuid();
            boolean isTargeted = isPlayerTargeted(player);

            if (isTargeted || playerLastActionTime.containsKey(playerId)) {
                long lastActionTick = playerLastActionTime.getOrDefault(playerId, currentTick);

                // Check if player is targeted or within the battle timeout window
                if (isTargeted || (currentTick - lastActionTick <= BATTLE_TIMEOUT)) {
                    if (!playerInBattle.getOrDefault(playerId, false)) {
                        updatePlayerBattleStatus(player, currentTick);
                    }
                } else {
                    removeBattleTag(player);
                }
            }
        }
    }

    private static ActionResult onPlayerAttackEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable HitResult hitResult) {
        if (player instanceof ServerPlayerEntity) {
            updatePlayerBattleStatus((ServerPlayerEntity) player, ((ServerWorld) world).getTime());
        }
        return ActionResult.PASS;
    }

    private static void updatePlayerBattleStatus(ServerPlayerEntity player, long currentTick) {
        UUID playerId = player.getUuid();
        playerLastActionTime.put(playerId, currentTick);
        addBattleTag(player);
    }
    private static void addBattleTag(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        if (!playerInBattle.getOrDefault(playerId, false)) {
            playerInBattle.put(playerId, true);
            player.getCommandTags().add("InBattle");
            player.sendMessage(Text.literal("You are now in battle!"), false);
        }
    }

    private static void removeBattleTag(ServerPlayerEntity player) {
        UUID playerId = player.getUuid();
        if (playerInBattle.getOrDefault(playerId, false)) {
            playerInBattle.put(playerId, false);
            player.getCommandTags().remove("InBattle");
            player.sendMessage(Text.literal("You are no longer in battle."), false);
        }
    }

    private static boolean isPlayerTargeted(ServerPlayerEntity player) {
        for (MobEntity mob : player.getWorld().getEntitiesByClass(MobEntity.class, player.getBoundingBox().expand(16), e -> e.getTarget() == player && e.canSee(player))) {
            return true;
        }
        return false;
    }
}
