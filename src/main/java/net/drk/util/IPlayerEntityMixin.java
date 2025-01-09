package net.drk.util;

import net.minecraft.util.math.BlockPos;

public interface IPlayerEntityMixin {
    void trySit(BlockPos pos);
    boolean inSittingPos();
    void setSitting(boolean sitting);
}