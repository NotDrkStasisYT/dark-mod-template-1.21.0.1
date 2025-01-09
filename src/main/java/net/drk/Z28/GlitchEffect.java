package net.drk.Z28;

import java.util.Random;
import net.minecraft.util.Formatting;

public class GlitchEffect extends NameTagEffect {
    private static final char[] SYMBOLS = {'@', '#', '%', '&', '*', '!', '$', '?', '+', '='};
    private static final Formatting[] DARK_COLORS = {
            Formatting.DARK_GRAY, Formatting.GRAY, Formatting.WHITE, Formatting.BLACK
    };

    private final Random random = new Random();

    public GlitchEffect() {
        super("Glitch");
    }

    @Override
    public String applyEffect(String nickname) {
        StringBuilder glitchedName = new StringBuilder();

        // Choose a random index to glitch one letter
        int glitchIndex = random.nextInt(nickname.length());

        for (int i = 0; i < nickname.length(); i++) {
            // Choose a random color from dark tones
            Formatting color = DARK_COLORS[random.nextInt(DARK_COLORS.length)];

            if (i == glitchIndex) {
                // Replace this character with a random symbol
                char symbol = SYMBOLS[random.nextInt(SYMBOLS.length)];
                glitchedName.append(color).append(symbol);
            } else {
                // Keep the original character but apply random dark tone color
                glitchedName.append(color).append(nickname.charAt(i));
            }
        }

        return glitchedName.toString();
    }
}
