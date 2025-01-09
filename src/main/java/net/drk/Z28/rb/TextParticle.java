package net.drk.Z28.rb;

import net.drk.Z28.rb.Color1;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public final class TextParticle extends Particle {

    private String text;

    public TextParticle(ClientWorld world, Vec3d pos, Vec3d velocity) {
        super(world, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z);

        // Set gravity to 0 to prevent falling
        gravityStrength = 0.0F; // Prevent the particle from falling

        // Set vertical velocity to 0
        setVelocity(velocity.x, 0, velocity.z); // Keep horizontal velocity, set vertical to 0

        // Adjust other parameters as needed
        velocityMultiplier = 0.99F; // You can keep this to adjust how fast it moves
        maxAge = Integer.MAX_VALUE; // Set max age to a large number to keep it floating indefinitely
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        var cameraPos = camera.getPos();

        float particleX = (float) (prevPosX + (x - prevPosX) * (double) tickDelta - cameraPos.x);
        float particleY = (float) (prevPosY + (y - prevPosY) * (double) tickDelta - cameraPos.y);
        float particleZ = (float) (prevPosZ + (z - prevPosZ) * (double) tickDelta - cameraPos.z);

        Matrix4f matrix = new Matrix4f();
        matrix = matrix.translation(particleX, particleY, particleZ);
        matrix = matrix.rotate(camera.getRotation());
        matrix = matrix.rotate((float) Math.PI, 0.0F, 1.0F, 0.0F);
        matrix = matrix.scale(-0.025F, -0.025F, -0.025F);

        var client = MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;
        var vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();

        float textX = textRenderer.getWidth(text) / -2.0F;
        float textY = 0.0F;

        for (int i = 0; i < text.length(); i++) {
            float hue = (i + System.currentTimeMillis() / 100) % 360 / 360.0F;
            int textColor = Color1.HSBtoRGB(hue, 1.0F, 1.0F);

            float r = (textColor >> 16 & 255) / 255.0F;
            float g = (textColor >> 8 & 255) / 255.0F;
            float b = (textColor & 255) / 255.0F;

            setColor(r, g, b, 1.0F);
            textRenderer.draw(String.valueOf(text.charAt(i)), textX + (i * textRenderer.getWidth(String.valueOf(text.charAt(i)))), textY, textColor, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        }
        vertexConsumers.draw();
    }
}
