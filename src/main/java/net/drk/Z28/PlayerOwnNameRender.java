package net.drk.Z28;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class PlayerOwnNameRender {

    public static void register() {
        // Register the callback to render the player's own nickname on the HUD
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            renderOwnName(context, tickDelta);
        });
    }

    private static void renderOwnName(DrawContext context, RenderTickCounter tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            // Get the player's styled nickname
            Text nickname = Text.literal(EffectManager.getStyledNickname(client.player));

            // Get the player's position for the name tag
            Vec3d playerPos = client.player.getPos();
            double distanceSquared = client.player.squaredDistanceTo(playerPos);

            // Only render if within a certain distance
            if (!(distanceSquared > 4096.0)) {
                // Similar to before, translate to the player's position
                context.getMatrices().push();
                context.getMatrices().translate(playerPos.x, playerPos.y + 2.5, playerPos.z); // Position above head
                context.getMatrices().multiply(client.gameRenderer.getCamera().getRotation()); // Align with camera
                context.getMatrices().scale(-0.025F, -0.025F, 0.025F); // Scale down the text

                // Get the transformation matrix for rendering
                Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

                // Set background opacity for the name tag
                float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

                // Get the TextRenderer
                TextRenderer textRenderer = client.textRenderer;

                // Center the nickname text
                float xPosition = -textRenderer.getWidth(nickname) / 2.0F;

                // Render the name tag above the player's head
                textRenderer.draw(nickname, xPosition, -10, 553648127, false, matrix4f, context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, backgroundColor, 15728880);

                // Pop the matrix
                context.getMatrices().pop();
            }
        }
    }
}
