package net.drk.block.sitting;

import com.mojang.serialization.MapCodec;
import net.drk.block.entities.ModBlockEntityTypes;
import net.drk.block.exclusive.tavern.entities.TavernStoolBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TavernStool extends BlockWithEntity {

    public TavernStool(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends TavernStool> getCodec() {
        throw new UnsupportedOperationException("Custom codec not implemented. Register it via a serializer.");
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TavernStoolBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlockEntityTypes.TAVERN_STOOL, TavernStoolBlockEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity entity = world.getBlockEntity(pos);

            if (entity instanceof TavernStoolBlockEntity blockEntity) {
                if (blockEntity.getSeatedPlayer() == null) {
                    blockEntity.setSeatedPlayer(player);
                    player.sendMessage(net.minecraft.text.Text.literal("Seated."), true);

                    return ActionResult.SUCCESS;
                } else {
                    player.sendMessage(net.minecraft.text.Text.literal("This seat is already occupied."), true);
                }
            }
        }
        return ActionResult.PASS;
    }

}
