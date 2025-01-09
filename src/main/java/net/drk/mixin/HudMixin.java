package net.drk.mixin;
/**
 * @author
 * @reason
 */

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InGameHud.class)
public class HudMixin {

    /**
     * @author Dark
     * @reason t
     */
    @Overwrite
    public void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
        // Prevent rendering the health bar
        // Do nothing to stop the rendering of health
    }


    /**
     * @author Dark
     * @reason t
     */
    @Overwrite
    public void renderFood(DrawContext context, PlayerEntity player, int top, int right) {
        // Prevent rendering the hunger bar
        // Do nothing to stop the rendering of hunger
    }
    /**
     * @author Dark
     * @reason t
     */
    @Overwrite
    public void renderExperienceBar(DrawContext context, int x) {
        // Prevent rendering the hunger bar
        // Do nothing to stop the rendering of hunger
    }
    /**
     * @author Dark
     * @reason t
     */
    @Overwrite
    public void renderHotbar(DrawContext context, RenderTickCounter tickCounter) {
        // Prevent rendering the hunger bar
        // Do nothing to stop the rendering of hunger
    }
}
