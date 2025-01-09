package net.drk.hotbar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.drk.DarkMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class CustomInventoryHud {
    private static final Identifier INVENTORY_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/inventory");
    private static final Identifier HUD_OUTLINE_LEFT_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/hud_outline_left");
    private static final Identifier HUD_OUTLINE_LEFT_EMPTY_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/hud_outline_left_empty");
    private static final Identifier MOUSE_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/mouse");
    private static final Identifier HUD_OUTLINE_RIGHT = Identifier.of(DarkMod.MOD_ID, "gui/hud_outline_right");


    private static final int INVENTORY_WIDTH = 183;
    private static final int INVENTORY_HEIGHT = 23;
    private static final int SLOT_SIZE = 20;
    private static final int MOUSE_WIDTH = 8;
    private static final int MOUSE_HEIGHT = 5;
    private static final int OFFHAND_WIDTH = 134;
    private static final int OFFHAND_HEIGHT = 49;
    private static final int OFFHAND_EMPTY_SIZE = 16; // Size of the empty slot in the offhand
    // Main render method, called during HUD render events
    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Check if HUD elements are hidden (F1 toggles this state)
        if (client.options.hudHidden) {
            return; // Do not render if HUD is hidden
        }
        else {
            drawInventory(context);
        }

    }

    // Draw the inventory texture at the bottom of the screen
    private static void drawInventory(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int inventoryX = (screenWidth - INVENTORY_WIDTH) / 2;
        int inventoryY = screenHeight - INVENTORY_HEIGHT - 0; // Positioned at the bottom

        // Draw the offhand slot directly aligned with the inventory
        drawOffhandSlot(context, inventoryX, inventoryY);

        // Bind the texture and draw the inventory
        RenderSystem.setShaderTexture(0, INVENTORY_TEXTURE);
        context.drawGuiTexture(INVENTORY_TEXTURE, inventoryX, inventoryY, INVENTORY_WIDTH, INVENTORY_HEIGHT);

        // Draw the items in the inventory
        drawItems(context, inventoryX, inventoryY);
        drawInventoryButton(context, inventoryX, inventoryY);

        // Draw the mouse selector over the selected slot
        drawMouseSelector(context, inventoryX, inventoryY);
    }

    // Draw the items from the player's inventory into the GUI
    private static void drawItems(DrawContext context, int inventoryX, int inventoryY) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null) return;

        int maxSlots = 9; // Maximum number of slots to render
        for (int i = 0; i < Math.min(maxSlots, player.getInventory().main.size()); i++) {
            ItemStack stack = player.getInventory().main.get(i);
            if (!stack.isEmpty()) {
                int slotX = inventoryX + (i * SLOT_SIZE) + 3; // Adjust for spacing
                int slotY = inventoryY + 5;

                context.drawItem(stack, slotX, slotY);
                context.drawItemInSlot(client.textRenderer, stack, slotX, slotY);
            }
        }

    }

    // Draw the offhand slot texture in the same position as the inventory
    private static void drawOffhandSlot(DrawContext context, int inventoryX, int inventoryY) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        // Position the offhand slot aligned with the inventory X and Y
        int offhandX = inventoryX - 26; // Same X position as the inventory
        int offhandY = inventoryY + 5; // Same Y position as the inventory

        int offhandX1 = inventoryX - 43; // Same X position as the inventory
        int offhandY1 = inventoryY - 25; // Same Y position as the inventory

        // Draw the outline of the offhand slot
        RenderSystem.setShaderTexture(0, HUD_OUTLINE_LEFT_TEXTURE);
        context.drawGuiTexture(HUD_OUTLINE_LEFT_TEXTURE, offhandX1, offhandY1, OFFHAND_WIDTH, OFFHAND_HEIGHT);

        // Draw the empty slot inside the offhand slot
        RenderSystem.setShaderTexture(0, HUD_OUTLINE_LEFT_EMPTY_TEXTURE);
        context.drawGuiTexture(HUD_OUTLINE_LEFT_EMPTY_TEXTURE, offhandX, offhandY, OFFHAND_EMPTY_SIZE, OFFHAND_EMPTY_SIZE);

        // Draw the player's offhand item if present
        if (player != null) {
            ItemStack offhandItem = player.getOffHandStack();
            if (!offhandItem.isEmpty()) {
                // Fixed coordinates for centering
                int offhandItemX = inventoryX - 26; // Adjust as needed for center
                int offhandItemY = inventoryY + 5; // Adjust as needed for center

                // Render the offhand item
                context.drawItem(offhandItem, offhandItemX, offhandItemY);
                context.drawItemInSlot(client.textRenderer, offhandItem, offhandItemX, offhandItemY);
            }
        }

    }

    // Draw the mouse selector over the selected slot
    private static void drawMouseSelector(DrawContext context, int inventoryX, int inventoryY) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null) return;

        int selectedSlot = player.getInventory().selectedSlot; // Get the currently selected slot

        int selectorX = inventoryX + (selectedSlot * SLOT_SIZE) + (SLOT_SIZE / 2) - (MOUSE_WIDTH / 2) + 1; // Center the mouse image horizontally in the slot
        int selectorY = inventoryY - 0; // Position the mouse image slightly above the slot

        // Bind the mouse texture (selector icon) and draw it over the selected slot
        RenderSystem.setShaderTexture(0, MOUSE_TEXTURE);
        context.drawGuiTexture(MOUSE_TEXTURE, selectorX, selectorY, MOUSE_WIDTH, MOUSE_HEIGHT);
    }
    private static void drawInventoryButton(DrawContext context, int inventoryX, int inventoryY) {

        // Position the level icon in the exact center
        int levelIconX = inventoryX + 91; // Same X position as the inventory
        int levelIconY = inventoryY - 25; // Same Y position as the inventory

        // Bind the texture and draw the level icon
        RenderSystem.enableBlend();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        RenderSystem.setShaderTexture(0, HUD_OUTLINE_RIGHT);
        context.drawGuiTexture(HUD_OUTLINE_RIGHT, levelIconX, levelIconY, 134, 49); // Updated to 34x11 size

    }
}
