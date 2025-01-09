package net.drk.Z;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.drk.summoning.FileStorageManager;
import net.drk.summoning.storage.ItemFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class SummonWeaponScreen extends Screen {
    private final FileStorageManager storageManager = new FileStorageManager("loot_items.txt");
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_SPACING = 25;
    private int scrollOffset = 0;
    private int maxScroll;
    private List<String> items;
    private ButtonWidget summonButton;

    public SummonWeaponScreen() {
        super(Text.literal("Summon Weapon"));
    }

    @Override
    protected void init() {
        this.clearChildren();

        // Get player items from storage
        String playerName = MinecraftClient.getInstance().player.getName().getString();
        items = storageManager.getItemsFromStorage(playerName);

        // Calculate maximum scroll based on the number of items
        maxScroll = Math.max(0, items.size() * BUTTON_SPACING - (this.height - 80));

        // Create and add summon random item button (fixed at the bottom)
        summonButton = ButtonWidget.builder(
                        Text.literal("Summon Random Item"),
                        b -> summonRandomItem()
                ).size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .position(this.width / 2 - BUTTON_WIDTH / 2, this.height - 30)  // Fixed at the bottom
                .build();
        this.addDrawableChild(summonButton);

        // Create item buttons (positions will be updated during render)
        for (int i = 0; i < items.size(); i++) {
            int index = i;
            ButtonWidget itemButton = ButtonWidget.builder(
                            Text.literal("Summon: " + items.get(i)),
                            b -> summonWeapon(index)
                    ).size(BUTTON_WIDTH, BUTTON_HEIGHT)
                    .position(this.width / 2 - BUTTON_WIDTH / 2, this.height / 4 + i * BUTTON_SPACING)
                    .build();
            this.addDrawableChild(itemButton);
        }
    }

    // Custom scroll handler for mouse scroll

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scrollOffset = MathHelper.clamp(scrollOffset - (int) (amount * BUTTON_SPACING), 0, maxScroll);
        return true;
    }

    // Summon random item
    private void summonRandomItem() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("Attempting to summon a random item..."), false);
            client.player.networkHandler.sendCommand("Shatterpoint summon");
        }
    }

    // Summon specific item by index
    void summonWeapon(int index) {
        MinecraftClient playerName = MinecraftClient.getInstance();

        String itemName = storageManager.getItemFromStorage(String.valueOf(playerName), index);
        ItemStack itemStack = ItemFactory.createItemFromName(itemName);

        if (!itemStack.isEmpty()) {
            MinecraftClient.getInstance().player.giveItemStack(itemStack);
            MinecraftClient.getInstance().player.sendMessage(Text.literal("Summoned: " + itemName), false);
        } else {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("Failed to summon item!"), false);
        }
    }

    // Adjust positions of buttons based on scrollOffset
    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext, mouseX, mouseY, delta);

        // Dynamically adjust item button positions based on scrollOffset
        int yStart = this.height / 4 - scrollOffset;
        for (int i = 0; i < this.children().size(); i++) {
            if (this.children().get(i) instanceof ButtonWidget button) {
                if (button != summonButton) {
                    button.setPosition(this.width / 2 - BUTTON_WIDTH / 2, yStart + i * BUTTON_SPACING);
                    button.visible = button.getY() >= this.height / 4 - BUTTON_HEIGHT && button.getY() <= this.height - 40;
                }
            }
        }

        super.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawText(this.textRenderer, "Summon Weapon Menu", this.width / 2 - 100, 20, 0xFFFFFF, false);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
