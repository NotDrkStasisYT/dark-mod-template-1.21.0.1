package net.drk.logic;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.ItemEntity;

public class CancelPickup {
    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ItemEntity itemEntity) {
                itemEntity.setPickupDelay(Integer.MAX_VALUE); // Prevent automatic pickup
            }
        });
    }
}
