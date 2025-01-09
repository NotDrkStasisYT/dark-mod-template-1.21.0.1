package net.drk.command.shield.styles;

import net.drk.command.shield.ShieldStyle;
import net.minecraft.util.Formatting;

public class FireStyle implements ShieldStyle {

    @Override
    public Formatting getShieldColor(float percentage) {
        if (percentage <= 0.3) {
            return Formatting.RED;    // Low shield is fiery red
        } else if (percentage <= 0.6) {
            return Formatting.GOLD;   // Medium shield is golden
        } else {
            return Formatting.YELLOW; // High shield is bright yellow
        }
    }
}
