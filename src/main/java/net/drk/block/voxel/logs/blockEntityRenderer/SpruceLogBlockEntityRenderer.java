package net.drk.block.voxel.logs.blockEntityRenderer;

import net.drk.block.voxel.logs.blockEntity.SpruceLogBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SpruceLogBlockEntityRenderer implements BlockEntityRenderer<SpruceLogBlockEntity> {

    public SpruceLogBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(SpruceLogBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState blockState = entity.getCachedState();

        // Get the TextRenderer instance
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        // Get the health and max health from the entity
        int maxHealth = (int) entity.getMaxHealth();
        int currentHealth = entity.getHealth();  // Get the current health directly from the entity

        if (currentHealth >= maxHealth) {
            return;
        }

        // Create the Text components for the health bar with styles (colors)
        StringBuilder coloredHealthBar = new StringBuilder();

        // Determine the number of total segments to display (e.g., 12 segments)
        int totalSegments = 12;

        // Calculate the number of filled segments based on current health
        int filledSegments = (int) ((float) currentHealth / (maxHealth - 1) * totalSegments);

        // Add empty segments (Gray color for no health) on the right
        for (int i = 0; i < totalSegments - filledSegments; i++) {
            coloredHealthBar.append("§7■"); // '§7' is the color code for gray
        }

        // Add filled segments (Orange color for full health) on the left
        for (int i = 0; i < filledSegments; i++) {
            coloredHealthBar.append("§6■"); // '§6' is the color code for orange
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

        // Translate to block center and apply rotation (you can adjust this to position it correctly)
        matrices.translate(0.5f, 0.6f, 0.5f); // Position above the block

        // Apply horizontal rotation (Y-axis rotation)
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(-horizontalAngle));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(pitch));

        matrices.scale(0.025f, 0.055f, 0.03f); // Scale up for better visibility

        // Render the health bar with colors
        textRenderer.draw(
                coloredHealthBar.toString(),
                -textRenderer.getWidth(coloredHealthBar.toString()) / 2.0f, // Center align text horizontally
                0,
                0xFFFFFF, // White color for text (the actual text color doesn't matter with colored squares)
                false, // No shadow
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.NORMAL,
                0,
                0xFFFFFF  // Use light color for rendering
        );

        // Pop matrix to reset transformations
        matrices.pop();
    }


}