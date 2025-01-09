package net.drk.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.drk.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements IEntityDataSaver {

    @Unique
    private final NbtCompound persistentData = new NbtCompound();

    @Override
    public NbtCompound getPersistentData() {
        return persistentData;
    }

    // When saving NBT data (for example when the player disconnects), save custom data
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("drkmod_data", persistentData);
    }

    // When loading NBT data (for example when the player joins), load custom data
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("drkmod_data")) {
            persistentData.copyFrom(nbt.getCompound("drkmod_data"));
        }
    }
}
