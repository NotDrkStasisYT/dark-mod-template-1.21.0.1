package net.drk.Z;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class SummonWeaponListWidget extends EntryListWidget<SummonWeaponListWidget.SummonWeaponEntry> {

    private final SummonWeaponScreen screen;

    // Constructor matching the parent class
    public SummonWeaponListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, SummonWeaponScreen screen) {
        super(client, width, height, top, bottom);
        this.screen = screen;
    }

    // Populate the list with items
    public void addItems(List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            this.addEntry(new SummonWeaponEntry(items.get(i), i, screen));
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    // Custom entry for each item
    public class SummonWeaponEntry extends EntryListWidget.Entry<SummonWeaponEntry> {
        private final String itemName;
        private final int index;
        private final SummonWeaponScreen screen;
        private final ButtonWidget summonButton;

        public SummonWeaponEntry(String itemName, int index, SummonWeaponScreen screen) {
            this.itemName = itemName;
            this.index = index;
            this.screen = screen;

            // Create summon button for each item
            this.summonButton = ButtonWidget.builder(
                    Text.literal("Summon: " + itemName),
                    button -> screen.summonWeapon(index)
            ).size(150, 20).build();
        }

        @Override
        public void render(DrawContext drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            drawContext.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(itemName).formatted(Formatting.WHITE), x, y + 5, 0xFFFFFF, false);
            summonButton.setPosition(x + entryWidth - 160, y);
            summonButton.render(drawContext, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return summonButton.mouseClicked(mouseX, mouseY, button);
        }
    }
}
