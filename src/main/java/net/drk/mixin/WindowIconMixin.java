package net.drk.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
@Environment(EnvType.CLIENT)
@Mixin(Window.class)
public class WindowIconMixin {

    @Shadow
    private long handle; // The GLFW window handle

    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    private void onSetIcon(CallbackInfo info) {
        try {
            // Load the custom icon images (16x16 and 32x32)
            BufferedImage icon16 = loadImage("/assets/drkmod/textures/gui/sp16.png");
            BufferedImage icon32 = loadImage("/assets/drkmod/textures/gui/sp32.png");

            // Convert images to GLFW-compatible format
            GLFWImage.Buffer icons = GLFWImage.malloc(2);
            icons.put(0, convertToGLFWImage(icon16));
            icons.put(1, convertToGLFWImage(icon32));

            // Apply the icons to the GLFW window
            GLFW.glfwSetWindowIcon(handle, icons);

            // Free memory
            icons.free();

            // Cancel the original setIcon method
            info.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to load images from resources
    private BufferedImage loadImage(String path) throws IOException {
        InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream(path));
        return ImageIO.read(inputStream);
    }

    // Helper method to convert BufferedImage to GLFWImage
    private GLFWImage convertToGLFWImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);

        // Copy pixel data to ByteBuffer
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgba = image.getRGB(x, y);
                buffer.put((byte) ((rgba >> 16) & 0xFF)); // Red
                buffer.put((byte) ((rgba >> 8) & 0xFF));  // Green
                buffer.put((byte) (rgba & 0xFF));         // Blue
                buffer.put((byte) ((rgba >> 24) & 0xFF)); // Alpha
            }
        }

        buffer.flip(); // Prepare the buffer for reading

        GLFWImage glfwImage = GLFWImage.malloc();
        glfwImage.set(width, height, buffer);
        return glfwImage;
    }
}
