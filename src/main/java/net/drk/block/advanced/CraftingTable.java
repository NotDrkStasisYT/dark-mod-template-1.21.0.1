package net.drk.block.advanced;

// Import necessary packages
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.text.Text;

public class CraftingTable extends Block {
    // Define properties to track the state of each drawer
    public static final BooleanProperty TOP_OPEN = BooleanProperty.of("top_open");
    public static final BooleanProperty BOTTOM_OPEN = BooleanProperty.of("bottom_open");

    public CraftingTable(Settings settings) {
        super(settings);
        // Set the default state of the block (both drawers closed)
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(TOP_OPEN, false)
                .with(BOTTOM_OPEN, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TOP_OPEN, BOTTOM_OPEN);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS; // Ensure only server-side logic runs
        }

        Vec3d hitPos = hit.getPos();
        double relativeY = hitPos.y - pos.getY();
        boolean isTopDrawer = relativeY >= 0.5;

        if (isTopDrawer) {
            // Toggle the top drawer state
            boolean newTopState = !state.get(TOP_OPEN);
            state = state.with(TOP_OPEN, newTopState);
            openDrawer(player, newTopState ? "top" : "top (closed)");
        } else {
            // Toggle the bottom drawer state
            boolean newBottomState = !state.get(BOTTOM_OPEN);
            state = state.with(BOTTOM_OPEN, newBottomState);
            openDrawer(player, newBottomState ? "bottom" : "bottom (closed)");
        }

        // Update the block state in the world
        world.setBlockState(pos, state);

        // Return success to indicate that the interaction was handled
        return ActionResult.SUCCESS;
    }

    private void openDrawer(PlayerEntity player, String drawer) {
        player.sendMessage(Text.literal("Opened " + drawer + " drawer!"), true);
        // TODO: Add drawer opening logic, such as animations or inventory
    }
}
