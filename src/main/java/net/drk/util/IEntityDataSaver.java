package net.drk.util;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();

    // When saving NBT data (for example when the player disconnects), save custom data
    void writeCustomDataToNbt(NbtCompound nbt);

    // When loading NBT data (for example when the player joins), load custom data
    void readCustomDataFromNbt(NbtCompound nbt);
}
