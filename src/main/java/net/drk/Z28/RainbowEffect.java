package net.drk.Z28;

import net.drk.Z28.NameTagEffect;

public class RainbowEffect extends NameTagEffect {
    private static final String[] COLORS = {"§c", "§6", "§e", "§a", "§b", "§d"};

    private int colorIndex = 0;

    public RainbowEffect() {
        super("Rainbow");
    }

    @Override
    public String applyEffect(String nickname) {
        StringBuilder coloredName = new StringBuilder();
        for (char c : nickname.toCharArray()) {
            coloredName.append(COLORS[colorIndex % COLORS.length]).append(c);
            colorIndex++;
        }
        return coloredName.toString();
    }
}
