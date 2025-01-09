package net.drk.Z28;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

public class PlayerNameRenderer {

    public static void register() {
        // Register the callback to render the player's nickname on the HUD
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            renderPlayerNickname(context, tickDelta);
        });
    }

    // Render the player's nickname above their head
    private static void renderPlayerNickname(DrawContext context, RenderTickCounter tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            // Get the player's styled nickname
            Text nickname = Text.literal(EffectManager.getStyledNickname(client.player));

            // Get the player's position for the name tag
            Vec3d playerPos = client.player.getPos();
            double distanceSquared = client.player.squaredDistanceTo(playerPos);

            // Only render the name tag if the player is within range
            if (!(distanceSquared > 4096.0)) {
                boolean isSneaking = client.player.isSneaky();
                int yOffset = "deadmau5".equals(nickname.getString()) ? -10 : 0;

                // Translate to the player's name tag position
                context.getMatrices().push();
                context.getMatrices().translate(playerPos.x, playerPos.y + 2.5, playerPos.z); // Position above head
                context.getMatrices().multiply(client.gameRenderer.getCamera().getRotation()); // Align with camera
                context.getMatrices().scale(-0.025F, -0.025F, 0.025F); // Scale down the text

                // Get the transformation matrix for rendering
                Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();

                // Set the opacity for background
                float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

                // Get the TextRenderer
                TextRenderer textRenderer = client.textRenderer;

                // Calculate the position to center the name tag text
                float xPosition = -textRenderer.getWidth(nickname) / 2.0F;

                // Render the name tag
                textRenderer.draw(nickname, xPosition, (float) yOffset, 553648127, false, matrix4f, context.getVertexConsumers(), isSneaking ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, backgroundColor, 15728880);

                // If not sneaking, draw the text with full visibility
                if (!isSneaking) {
                    textRenderer.draw(nickname, xPosition, (float) yOffset, -1, false, matrix4f, context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
                }

                // Pop the matrix to revert to the previous state
                context.getMatrices().pop();
            }
        }
    }
}
