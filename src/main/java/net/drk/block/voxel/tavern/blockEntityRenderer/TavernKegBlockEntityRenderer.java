package net.drk.block.voxel.tavern.blockEntityRenderer;

import net.drk.block.voxel.tavern.blockEntity.TavernKegBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class TavernKegBlockEntityRenderer implements BlockEntityRenderer<TavernKegBlockEntity> {

    public TavernKegBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(TavernKegBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        ItemStack currentItem = entity.getCurrentItem();
        Item item = currentItem.getItem();
        // Get the TextRenderer instance
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        // Get the remaining time and max processing time
        int maxTime = entity.getMaxProcessingTime();
        int remainingTime = entity.getRemainingProcessingTime();

        boolean isDone = entity.isProcessingComplete();



        // Calculate the progress bar segments
        int totalSegments = 8; // Number of segments in the bar
        int filledSegments = (int) ((float) remainingTime / maxTime * totalSegments);

        // Build the progress bar with colors
        StringBuilder progressBar = new StringBuilder();

        if (item == Items.POTATO) {  // Check for cat or dog item
            // Add filled segments (green) first
            for (int i = 0; i < filledSegments; i++) {
                progressBar.append("§7■"); // Gray segment

            }

            // Add empty segments (gray) for the remaining time
            for (int i = 0; i < totalSegments - filledSegments; i++) {
                progressBar.append("§a■"); // Green segment
            }
        } else {
            for (int i = 0; i < filledSegments; i++) {
                progressBar.append("§7■"); // Gray segment

            }

            // Add empty segments (gray) for the remaining time
            for (int i = 0; i < totalSegments - filledSegments; i++) {
                progressBar.append("§6■"); // Green segment
            }
        }
        // Get the player's position
        MinecraftClient client = MinecraftClient.getInstance();
        Vec3d playerPos = client.cameraEntity.getPos();

        // Calculate the position of the block
        BlockPos blockPos = entity.getPos();
        Vec3d blockCenter = new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 1.5, blockPos.getZ() + 0.5);

        // Calculate the direction vector from the block to the player
        double deltaX = playerPos.x - blockCenter.x;
        double deltaZ = playerPos.z - blockCenter.z;

        // Calculate the horizontal angle (rotation around the Y-axis)
        float horizontalAngle = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) + 90;
        float pitch = client.cameraEntity.getPitch();

        // Push matrix for transformations
        matrices.push();

        // Translate to block center and apply rotation


        // Scale the text to fit above the keg

        // Render the progress bar
        if (remainingTime > 0 || maxTime > 0) {
            matrices.translate(0.5f, 1.5f, 0.5f); // Position above the keg
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-horizontalAngle));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
            matrices.scale(0.025f, 0.055f, 0.03f); // Scale up for better visibility

            textRenderer.draw(
                    progressBar.toString(),
                    -textRenderer.getWidth(progressBar.toString()) / 2.0f, // Center align text
                    0,
                    0xFFFFFF, // White color
                    false,
                    matrices.peek().getPositionMatrix(),
                    vertexConsumers,
                    TextRenderer.TextLayerType.NORMAL,
                    0,
                    light
            );
        }
        if (entity.hasItem() && (remainingTime <= 0 || maxTime <= 0)) {
            System.out.println("Displaying");
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            ItemStack stack = entity.getProcessedItem();
            matrices.translate(0.5f, 1.8f, 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getRenderingRotation()));
            itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        }
        // Pop matrix to reset transformations
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(TavernKegBlockEntity blockEntity) {
        return true; // Ensure rendering even if outside the block's bounding box
    }
    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
