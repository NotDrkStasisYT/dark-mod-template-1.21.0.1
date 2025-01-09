package net.drk.GUI.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import net.drk.DarkMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class HP_BAR {
    private static final Identifier HP_BAR_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/outline_hp");

    // Main render method, called during HUD render events
    public static void render(DrawContext context) {
        drawFoodBar(context);
    }

    // Draw the food bar texture near the health bar
    private static void drawFoodBar(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();



        // Health bar is usually around 39 pixels from the bottom of the screen, so we'll position this near it
        int healthBarX = screenWidth / 2 - 91; // X coordinate of health bar (left side)
        int healthBarY = screenHeight - 39; // Y coordinate of the health bar (distance from bottom)

        // Position your food bar just to the right of the health bar
        int x = healthBarX; // Move it to the right by 100 pixels from the health bar
        int y = healthBarY;       // Align vertically with the health bar

        // Bind the texture and draw it near the health bar
        RenderSystem.setShaderTexture(0, HP_BAR_TEXTURE);
        context.drawGuiTexture(HP_BAR_TEXTURE, x, y, 88, 17);
    }
}
