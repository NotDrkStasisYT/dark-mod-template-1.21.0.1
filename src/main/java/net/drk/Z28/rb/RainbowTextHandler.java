package net.drk.Z28.rb;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class RainbowTextHandler {

    private static final Formatting[] RAINBOW_COLORS = new Formatting[] {
            Formatting.RED, Formatting.GOLD, Formatting.YELLOW,
            Formatting.GREEN, Formatting.AQUA, Formatting.BLUE,
            Formatting.LIGHT_PURPLE
    };

    private static int tickCounter = 0;

    // Create a list to hold the floating texts
    private static List<AreaEffectCloudEntity> floatingTexts = new ArrayList<>();

    // Method to create rainbow text on an AreaEffectCloud entity
    public void createFloatingText(ServerWorld world, Vec3d position, String text) {
        AreaEffectCloudEntity cloudEntity = new AreaEffectCloudEntity(world, position.x, position.y, position.z);
        cloudEntity.setCustomNameVisible(true);
        cloudEntity.setInvisible(true);
        cloudEntity.setDuration(Integer.MAX_VALUE); // Prevent it from disappearing
        cloudEntity.setCustomName(applyRainbowGradient(text));

        // Add the cloud entity to the world and the list
        world.spawnEntity(cloudEntity);
        floatingTexts.add(cloudEntity);
    }

    // Apply rainbow gradient to the entire text
    public static MutableText applyRainbowGradient(String baseText) {
        MutableText rainbowText = Text.literal("");

        for (int i = 0; i < baseText.length(); i++) {
            char letter = baseText.charAt(i);
            Formatting[] letterColors = getGradientColors(i);
            MutableText coloredLetter = Text.literal(String.valueOf(letter));

            // Apply gradient colors to the letter
            for (Formatting color : letterColors) {
                coloredLetter.formatted(color);
            }
            rainbowText.append(coloredLetter);
        }
        return rainbowText;
    }

    // Get a gradient of two colors for smooth transition
    private static Formatting[] getGradientColors(int index) {
        // Determine which two colors to transition between
        int colorIndex1 = (index + tickCounter) % RAINBOW_COLORS.length;
        int colorIndex2 = (colorIndex1 + 1) % RAINBOW_COLORS.length;

        return new Formatting[]{
                RAINBOW_COLORS[colorIndex1],
                RAINBOW_COLORS[colorIndex2]
        };
    }

    // Method to update all floating texts
    public static void updateFloatingTexts() {
        tickCounter++;
        for (AreaEffectCloudEntity entity : floatingTexts) {
            entity.setCustomName(applyRainbowGradient(entity.getCustomName().getString()));
        }
    }
}
