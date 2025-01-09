package net.drk.Z;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Z extends Screen {
    private static final int LONG_GUI_HEIGHT = 40; // Height of the long GUI
    private static final int BIG_GUI_HEIGHT = 80; // Height of the big GUI
    private static final int GUI_WIDTH = 180; // Width of both GUIs
    private static final int GUI_SPACING = 10;  // Space between each small GUI
    private static final int MAIN_GUI_WIDTH = 256;  // Width of the fixed main GUI
    private static final int MAIN_GUI_HEIGHT = 256; // Height of the fixed main GUI

    // Textures for the long and big GUIs
    private static final Identifier LONG_GUI_TEXTURE = Identifier.of("drkmod", "textures/gui/long_gui_texture.png"); // Long GUI texture path
    private static final Identifier BIG_GUI_TEXTURE = Identifier.of("drkmod", "textures/gui/big_gui_texture.png"); // Big GUI texture path

    private final List<Identifier> guis = new ArrayList<>();
    private double scrollOffset = 0;
    private double scrollSpeed = 10;
    private int maxScroll;
    private boolean showBottomGui = false;
    private ButtonWidget bottomGuiButton; // Special button that appears when you scroll to the bottom
    private static final Identifier MAIN_GUI_TEXTURE = Identifier.of("drkmod", "textures/gui/main_gui.png"); // Main GUI texture

    public Z() {
        super(Text.literal("Scrollable Shop"));
    }

    @Override
    protected void init() {
        // Generate 20 GUIs (just placeholders, replace with actual items/textures)
        for (int i = 0; i < 20; i++) {
            guis.add(Identifier.of("drkmod", "textures/gui/panel_" + i + ".png"));
        }

        // Calculate the maximum scroll distance for the long and big GUIs, including the smaller GUIs
        maxScroll = (guis.size() * (BIG_GUI_HEIGHT + GUI_SPACING)) + LONG_GUI_HEIGHT - (MAIN_GUI_HEIGHT - 40);
        maxScroll = Math.max(0, maxScroll);

        // Initialize the bottom GUI button (invisible until fully scrolled)
        bottomGuiButton = ButtonWidget.builder(
                        Text.literal("Bottom Button"),
                        button -> this.onBottomGuiButtonClick()
                ).size(GUI_WIDTH, 20)
                .position(this.width / 2 - GUI_WIDTH / 2, this.height - 50)
                .build();
        bottomGuiButton.visible = false;
        this.addDrawableChild(bottomGuiButton);
    }

    private void onBottomGuiButtonClick() {
        // Logic for when the bottom GUI button is clicked
        System.out.println("Bottom GUI button clicked!");
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // Smooth scroll based on mouse wheel input
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - verticalAmount * scrollSpeed));

        // Show the bottom GUI when fully scrolled down
        showBottomGui = scrollOffset >= maxScroll;
        bottomGuiButton.visible = showBottomGui;

        return true;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        // Render the background
        this.renderBackground(drawContext, mouseX, mouseY, delta);

        // Draw the fixed main GUI in the center of the screen
        int mainGuiX = (this.width - MAIN_GUI_WIDTH) / 2;
        int mainGuiY = (this.height - MAIN_GUI_HEIGHT) / 2;
        drawContext.drawTexture(MAIN_GUI_TEXTURE, mainGuiX, mainGuiY, 0, 0, MAIN_GUI_WIDTH, MAIN_GUI_HEIGHT);

        // Draw the long GUI at the top (scrollable)
        int longGuiY = mainGuiY + 20 + (int)scrollOffset; // Positioned with some padding
        drawContext.drawTexture(LONG_GUI_TEXTURE, mainGuiX + 20, longGuiY, 0, 0, GUI_WIDTH, LONG_GUI_HEIGHT); // Draw long GUI texture

        // Draw the big GUI below the long GUI (scrollable)
        int bigGuiY = longGuiY + LONG_GUI_HEIGHT + GUI_SPACING; // Space between long and big GUI
        drawContext.drawTexture(BIG_GUI_TEXTURE, mainGuiX + 20, bigGuiY, 0, 0, GUI_WIDTH, BIG_GUI_HEIGHT); // Draw big GUI texture

        // Draw scrollable GUIs under the big GUI
        int scrollableGuiYStart = bigGuiY + BIG_GUI_HEIGHT + GUI_SPACING; // Start position for scrollable GUIs
        for (int i = 0; i < guis.size(); i++) {
            int guiY = (int) (scrollableGuiYStart + (i * (BIG_GUI_HEIGHT + GUI_SPACING)) - scrollOffset);

            // Always draw the GUIs; they are still present, but their visibility is constrained to the scrolling
            Identifier guiTexture = guis.get(i);
            drawContext.drawTexture(guiTexture, mainGuiX + 20, guiY, 0, 0, GUI_WIDTH, BIG_GUI_HEIGHT);  // Draw scrollable GUIs
        }

        // Render the bottom GUI button if visible
        if (showBottomGui) {
            bottomGuiButton.render(drawContext, mouseX, mouseY, delta);
        }

        // Render any additional elements (like buttons, etc.)
        super.render(drawContext, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
