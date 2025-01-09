package net.drk.block.voxel.tavern.block;

import net.drk.block.entities.ModBlockEntities;
import net.drk.block.voxel.tavern.blockEntity.TavernKegStationBlockEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TavernKegStation extends Block implements BlockEntityProvider {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public TavernKegStation(Settings settings) {
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
            if (entity instanceof TavernKegStationBlockEntity kegEntity) {
                Vec3d hitPos = hit.getPos();  // Get the hit position in world coordinates
                double relativeX = hitPos.x - pos.getX();  // Calculate the X offset from the keg block's position
                boolean isLeftSide = relativeX < 0;  // Check if the hit is on the left side (X < 0)
                boolean isRightSide = relativeX >= 0;  // Check if the hit is on the right side (X >= 0)

                // Check if the hit is on the top half or bottom half of the block
                if (isLeftSide) {
                    System.out.println("Hit the left side of the keg at: " + hitPos);
                } else {
                    System.out.println("Hit the right side of the keg at: " + hitPos);
                }

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
        return VoxelShapes.cuboid(-0.9999f, -0.9999f, -0.2f, 1.9999f, 1.4f, 1.2f);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TavernKegStationBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.TAVERN_KEG_STATION ? (world1, pos, state1, entity) -> {
            if (entity instanceof TavernKegStationBlockEntity kegEntity) {
                TavernKegStationBlockEntity.tick(world1, pos, state1, kegEntity);
            }
        } : null;
    }
}
