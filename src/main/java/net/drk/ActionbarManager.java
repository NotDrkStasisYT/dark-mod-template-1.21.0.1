package net.drk;

import net.drk.number.NumberFormatter;
import net.drk.player.ShieldManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.math.BigInteger;

public class ActionbarManager {

    private final MinecraftClient client;
    private int currentDisplayIndex = 0; // Keeps track of the current display
    private boolean needsUpdate = true; // Indicates whether the action bar needs updating

    public ActionbarManager(MinecraftClient client) {
        this.client = client;
    }

    // Getter for the current display index
    public int getCurrentDisplayIndex() {
        return currentDisplayIndex;
    }

    // Setter for the display index, marks the action bar as needing an update
    public void setDisplayIndex(int index) {
        this.currentDisplayIndex = index;
        this.needsUpdate = true; // Mark as needing update when display index changes
    }

    // Method to update the action bar based on the current display
    public void updateActionBar() {
        if (client.player != null && needsUpdate) {
            switch (currentDisplayIndex) {
                case 0:

                case 1:
                    showLumin(client.player);
                    break;
                // Add more cases here if you have more display options
            }
            needsUpdate = false; // Reset the update flag after the update
        }
    }

    // Example implementation of the shield display


    // Example implementation of the lumin display
    private void showLumin(ClientPlayerEntity player) {
        PlayerData playerData = PlayerdataManager.getPlayerData(player.getName().getString());
        LuminCurrency playerLumin = playerData.getLuminCurrency();

        if (playerLumin == null) {
            playerLumin = new LuminCurrency(BigInteger.ZERO); // Default to zero if not present
            playerData.setLuminCurrency(playerLumin);
        }

        BigInteger currentLumin = playerLumin.getAmount();
        Formatting luminColor = Formatting.BLUE; // Customize as needed

        Text luminText = Text.literal(NumberFormatter.formatPrice(currentLumin))
                .formatted(luminColor);
        Text leftArrow = Text.literal("<").formatted(luminColor);
        Text rightArrow = Text.literal(">").formatted(luminColor);

        Text label = Text.literal(" Lumin: ").formatted(luminColor);

        Text message = ((MutableText) leftArrow)
                .append(label)
                .append(luminText)
                .append(rightArrow);

        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(message));
    }

    // Method to get the number of available displays
    public int getMaxDisplays() {
        return 2; // Adjust this number based on the number of displays you have
    }
}
