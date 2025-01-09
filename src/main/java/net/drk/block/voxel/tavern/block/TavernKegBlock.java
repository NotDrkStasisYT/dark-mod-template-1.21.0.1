package net.drk.block.voxel.tavern.block;

import net.drk.block.entities.ModBlockEntities;
import net.drk.block.voxel.tavern.blockEntity.TavernKegBlockEntity;
import net.drk.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TavernKegBlock extends Block implements BlockEntityProvider {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public TavernKegBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TavernKegBlockEntity kegEntity) {
                ItemStack heldItem = player.getMainHandStack(); // Get the item in the player's main hand

                // Check if the player is holding a valid input item
                if (heldItem.getItem() == Items.WHEAT || heldItem.getItem() == Items.POTATO || heldItem.getItem() == ModItems.ECLIPSE_SPEAR) {
                    if (kegEntity.insertItem(heldItem, player)) { // Insert the item into the keg with player reference
                        heldItem.decrement(1); // Remove 1 item from the player's hand
                        return ActionResult.SUCCESS;
                    }
                } else if (kegEntity.hasItem() && kegEntity.isProcessingComplete()) {
                    // If the keg has a finished item, allow the player to extract it
                    ItemStack finishedItem = kegEntity.extractItem(player); // Ensure only the player who inserted the item can extract it
                    if (!finishedItem.isEmpty()) {
                        if (!player.giveItemStack(finishedItem)) {
                            player.dropItem(finishedItem, false); // Drop the item if the player's inventory is full
                        }
                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return ActionResult.PASS;
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0.00001f, -0.2f, 1f, 1.4f, 1.2f);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TavernKegBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.TAVERN_KEG ? (world1, pos, state1, entity) -> {
            if (entity instanceof TavernKegBlockEntity kegEntity) {
                TavernKegBlockEntity.tick(world1, pos, state1, kegEntity);
            }
        } : null;
    }
}
