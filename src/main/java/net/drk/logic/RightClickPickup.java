package net.drk.logic;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;

public class RightClickPickup {
    public static void register() {
        // Register event handler for right-click interactions
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            // Make sure it's the right entity and only on the server side
            if (!world.isClient() && entity instanceof ItemEntity itemEntity) {
                // Ensure the player is right-clicking the correct item entity
                if (hitResult.getEntity() == entity) {
                    ItemStack stack = itemEntity.getStack(); // Get the item stack from the entity

                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        // Try to insert the item into the player's inventory
                        if (serverPlayer.getInventory().insertStack(stack)) {
                            // If the player can pick up the item, remove it from the world
                            itemEntity.remove(Entity.RemovalReason.DISCARDED);
                            // Optionally, send a message to the player confirming the pickup
                            serverPlayer.sendMessage(stack.getName().copy().append(" has been picked up."), true);
                        } else {
                            // If the inventory is full, send a message to the player
                            serverPlayer.sendMessage(Text.of("Your inventory is full! Cannot pick up item."), true);
                        }
                    }
                    return ActionResult.SUCCESS; // Indicate that the interaction was handled
                }
            }
            return ActionResult.PASS; // Allow other interactions to continue
        });
    }
}
