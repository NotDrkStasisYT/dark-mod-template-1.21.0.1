package net.drk.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.drk.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCopyHandler implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        ((IEntityDataSaver) newPlayer).getPersistentData().putIntArray("drk.homepos",
                ((IEntityDataSaver) oldPlayer).getPersistentData().getIntArray("drk.homepos"));
    }
}