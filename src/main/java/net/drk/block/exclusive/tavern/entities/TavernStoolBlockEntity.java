package net.drk.block.exclusive.tavern.entities;

import net.drk.block.entities.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TavernStoolBlockEntity extends BlockEntity {
    private PlayerEntity seatedPlayer;

    public TavernStoolBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TAVERN_STOOL, pos, state);
    }

    public PlayerEntity getSeatedPlayer() {
        return seatedPlayer;
    }

    public void setSeatedPlayer(PlayerEntity player) {
        this.seatedPlayer = player;
    }

    public void removePlayer() {
        this.seatedPlayer = null;
    }

    public static void tick(World world, BlockPos pos, BlockState state, TavernStoolBlockEntity blockEntity) {
        if (!world.isClient && blockEntity.getSeatedPlayer() != null) {
            PlayerEntity playerEntity = blockEntity.getSeatedPlayer();

            // Set the player’s position above the stool
            playerEntity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            playerEntity.setSprinting(false);  // Ensure they are not sprinting
            playerEntity.stopRiding();         // Ensure they are not riding anything

            // Optional: Update player’s pose if you are using custom logic like sitting pose
            // (Ensure you have a mixin or custom method for this if you need it)
            // ((ILivingEntityMixin)playerEntity).setSittingPose(true);

            playerEntity.calculateDimensions(); // Recalculate player dimensions

            // If player is sneaking, remove them from the stool
            if (playerEntity.isSneaking()) {
                // Optional: Reset sitting pose
                // ((ILivingEntityMixin)playerEntity).setSittingPose(false);

                // Remove player from the stool and reset the block's state
                blockEntity.setSeatedPlayer(null);
            }
        }
    }

}
