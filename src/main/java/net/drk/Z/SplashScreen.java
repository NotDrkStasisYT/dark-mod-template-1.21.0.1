package net.drk.Z;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SplashScreen extends Screen {
    private static final Identifier LOGO_TEXTURE = Identifier.of("drkmod", "gui/backgroundlogo"); // Your logo path
    private long startTime;
    private static final long DISPLAY_TIME = 3000; // Display time in milliseconds (3 seconds)

    public SplashScreen() {
        super(Text.literal("Loading..."));
    }

    @Override
    protected void init() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        // Render the background (can add any additional background rendering logic here)
        this.renderBackground(drawContext, mouseX, mouseY, delta);

        // Draw the texture to fill the entire screen
        drawContext.drawGuiTexture(LOGO_TEXTURE, 0, 0, this.width, this.height); // Fill the whole screen

        // Call the parent render method for additional elements
        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        super.tick();
        // Check if the display time has passed
        if (System.currentTimeMillis() - startTime >= DISPLAY_TIME) {
            // Close the splash screen and open the main menu or game screen
            this.client.setScreen(null); // This will transition back to the game
        }
    }

    @Override
    public boolean shouldPause() {
        return false; // Ensure the game continues running while showing the splash screen
    }
}
