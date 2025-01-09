package net.drk.GUI.HUD;

import com.mojang.blaze3d.systems.RenderSystem;
import net.drk.DarkMod;
import net.drk.PlayerData;
import net.drk.PlayerdataManager;
import net.drk.command.FriendListManager;
import net.drk.player.ShieldManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public class BARS {
    private static final Identifier HP_BAR_NEW = Identifier.of(DarkMod.MOD_ID, "textures/gui/fill_hp_bar1.png");
    private static final Identifier HP_BAR_TEXTURE1 = Identifier.of(DarkMod.MOD_ID, "textures/gui/fill_shield_bar.png");
    private static final Identifier HUNGER_BAR = Identifier.of(DarkMod.MOD_ID, "textures/gui/fill_food_bar_1.png");

    private static final Identifier HP_BAR_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/outline_hp");
    private static final Identifier FOOD_BAR_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/outline_food");
    private static final Identifier LEVEL_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/level");
    private static final Identifier COIN_TEXTURE = Identifier.of(DarkMod.MOD_ID, "gui/coin1");


    private static boolean showHealthBar = false; // Tracks which bar to display

    // Main render method, called during HUD render events
    public static void render(DrawContext context) {

        MinecraftClient client = MinecraftClient.getInstance();

        // Check if HUD elements are hidden (F1 toggles this state)
        if (client.options.hudHidden) {
            return; // Do not render if HUD is hidden
        }
        else {
            renderMethods(context);
        }
    }
    public static void renderMethods(DrawContext context) {
        drawFoodBar(context);
        drawHPBar(context);
        drawLevelIcon(context);
        drawLumin(context);
        drawHungerBar(context);
        if (showHealthBar) {
            drawHealthBar(context);
        } else {
            drawShieldBar(context);
        }
    }
    public static void toggleBar() {
        showHealthBar = !showHealthBar; // Toggle the bar state
    }
    // Draw the health bar texture on the left side
    private static void drawHPBar(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Adjusted X coordinate to move the health bar further left
        int healthBarX = screenWidth / 2 - 110; // Further left by decreasing the value
        int healthBarY = screenHeight - 42;     // Y coordinate remains the same

        // Bind the texture and draw the health bar
        RenderSystem.setShaderTexture(0, HP_BAR_TEXTURE);
        context.drawGuiTexture(HP_BAR_TEXTURE, healthBarX, healthBarY, 88, 17);
    }

    // Draw the food bar texture on the right side
    private static void drawFoodBar(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Adjusted X coordinate to move the food bar further right
        int foodBarX = screenWidth / 2 + 20; // Further right by increasing the value
        int foodBarY = screenHeight - 42;    // Y coordinate remains the same

        // Bind the texture and draw the food bar
        RenderSystem.setShaderTexture(0, FOOD_BAR_TEXTURE);
        context.drawGuiTexture(FOOD_BAR_TEXTURE, foodBarX, foodBarY, 88, 17);
    }

    // Draw the level texture in the exact center of the screen (both X and Y)
    private static void drawLevelIcon(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Position the level icon in the exact center
        int levelIconX = screenWidth / 2 - 34 / 2;  // Centered horizontally, considering width (34px)
        int levelIconY = screenHeight - 37; // Centered vertically, considering height (11px)

        // Bind the texture and draw the level icon
        RenderSystem.enableBlend();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        RenderSystem.setShaderTexture(0, LEVEL_TEXTURE);
        context.drawGuiTexture(LEVEL_TEXTURE, levelIconX, levelIconY, 34, 11); // Updated to 34x11 size
    }

    private static void drawLumin(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Position the Lumin icon in the exact center
        int luminIconX = screenWidth / 2 - 110; // Further left by decreasing the value
        int luminIconY = screenHeight - 54; // Centered vertically, considering height (11px)

        // Retrieve player lumin amount
        ClientPlayerEntity clientPlayer = client.player;

        if (clientPlayer != null) {
            String playerName = clientPlayer.getName().getString();
            PlayerData playerData = PlayerdataManager.getPlayerData(playerName);
            UUID playerUUID = clientPlayer.getUuid();




                // Pass the multiplier to getIconAmount
                String luminAmount = playerData.getLuminCurrency().getIconAmount();

                // Bind the texture and draw the Lumin icon
                RenderSystem.enableBlend();
                context.setShaderColor(1.0F, 1.0F, 1.0F, 1);
                RenderSystem.setShaderTexture(0, COIN_TEXTURE);
                context.drawGuiTexture(COIN_TEXTURE, luminIconX, luminIconY, 11, 10); // Update the size as needed

                // Draw the Lumin amount next to the icon
                context.drawText(client.textRenderer, luminAmount, luminIconX + 15, luminIconY + 1, 0xFFFFFF, true);
            }
        }


    private static void drawShieldBar(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Bar dimensions
        int barWidth = 128; // Reduced width of the shield bar
        int barHeight = 32; // Reduced height of the shield bar
        int actualBarHeight = 6; // Reduced height of the actual shield bar

        // Position the bar in the middle of the screen
        int shieldBarX = screenWidth / 2 - 92; // Further left by decreasing the value
        int shieldBarY = screenHeight - 35;     // Y coordinate remains the same

        // Retrieve the player and their shield data
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer != null) {
            ShieldManager shieldManager = ShieldManager.getInstance();

            // Get the player's shield value directly from your getClientShield() method
            BigInteger currentShield = shieldManager.getClientShield(clientPlayer);
            BigInteger maxShield = shieldManager.getMaxClientShield(clientPlayer); // Assuming getMaxShield works similarly

            // Calculate the threshold for 54% of max shield
            double thresholdShield = 1.75 * maxShield.doubleValue();

            // Calculate the shield percentage (scale current shield based on the 54% threshold)
            double shieldPercentage = thresholdShield > 0 ?
                    Math.min(currentShield.doubleValue(), thresholdShield) / thresholdShield : 0;

            // Calculate the width of the filled portion based on the adjusted shield percentage
            int filledWidth = (int) (barWidth * shieldPercentage);

            // Bind the texture for the shield bar
            RenderSystem.setShaderTexture(0, HP_BAR_TEXTURE1);

            // Draw the filled portion of the actual shield bar
            if (filledWidth > 0) {
                context.drawTexture(
                        HP_BAR_TEXTURE1,
                        shieldBarX, // Center horizontally within the background
                        shieldBarY, // Center vertically within the background
                        0, 0, filledWidth, actualBarHeight, barWidth, barHeight
                );
            }
        }
    }
    private static void drawHealthBar(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Bar dimensions
        int barWidth = 128; // Adjust as necessary for health bar
        int barHeight = 32;
        int actualBarHeight = 6;

        // Position the bar (place it below the shield bar or adjust as needed)
        int healthBarX = screenWidth / 2 - 92; // Align with the shield bar horizontally
        int healthBarY = screenHeight - 35;   // Adjust the Y position to differentiate from shield bar

        // Retrieve the player and their health data
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer != null) {
            // Get the player's current health and max health
            float currentHealth = clientPlayer.getHealth();
            float maxHealth = clientPlayer.getMaxHealth();

            // Calculate the health percentage
            double healthPercentage = maxHealth > 0 ? currentHealth / maxHealth : 0;

            // Calculate the width of the filled portion based on health percentage
            int filledWidth = (int) (barWidth * healthPercentage);

            // Bind the texture for the health bar
            RenderSystem.setShaderTexture(0, HP_BAR_NEW);

            // Draw the filled portion of the health bar
            if (filledWidth > 0) {
                context.drawTexture(
                        HP_BAR_NEW,
                        healthBarX, // Center horizontally within the background
                        healthBarY, // Center vertically within the background
                        0, 0, filledWidth, actualBarHeight, barWidth, barHeight
                );
            }
        }
    }
    private static void drawHungerBar(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Bar dimensions
        int barWidth = 128; // Adjust as necessary for health bar
        int barHeight = 32;
        int actualBarHeight = 6;

        // Position the bar (place it below the shield bar or adjust as needed)
        int hungerBarX = (screenWidth / 2 - 38); // Adjusted from -38 to -35
        int hungerBarY = screenHeight - 35;   // Adjust the Y position to differentiate from shield bar

        // Retrieve the player and their health data
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer != null) {
            // Get the player's current food level
            float currentFood = clientPlayer.getHungerManager().getFoodLevel();
            int maxFood = 20;

            // Calculate the health percentage
            double foodPercentage = (double) currentFood / maxFood;
            // Calculate the width of the filled portion based on food percentage
            int filledWidth = (int) (barWidth * foodPercentage);

            // Bind the texture for the hunger bar
            RenderSystem.setShaderTexture(0, HUNGER_BAR);

            // Draw the filled portion of the hunger bar starting from the right
            if (filledWidth > 0) {
                context.drawTexture(
                        HUNGER_BAR,
                        hungerBarX + (barWidth - filledWidth), // Adjust to start filling from the right
                        hungerBarY,                           // Y position of the bar
                        barWidth - filledWidth, 0,            // Adjust texture coordinates to match flip
                        filledWidth, actualBarHeight,         // Width and height of the filled texture
                        barWidth, barHeight                  // Full size of the texture
                );
            }
        }
    }

}
