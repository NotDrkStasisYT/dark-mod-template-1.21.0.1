
package net.drk.event;

import net.drk.command.FriendListManager;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public class AttackEventListener {
    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world instanceof ServerWorld && player instanceof ServerPlayerEntity) {
                // Ensure the entity is also a player before proceeding
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity attacker = (ServerPlayerEntity) player;
                    ServerPlayerEntity target = (ServerPlayerEntity) entity;

                    // Check if the attacker and target are friends
                    if (FriendListManager.isFriend(attacker.getUuid(), target.getUuid())) {
                        // Cancel the attack on friends
                        return ActionResult.FAIL;
                    }
                }
            }
            // Allow the attack to proceed if not targeting a player or if not friends
            return ActionResult.PASS;
        });
    }
}
