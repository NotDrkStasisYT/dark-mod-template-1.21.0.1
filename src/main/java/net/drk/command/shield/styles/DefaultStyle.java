package net.drk.command.shield.styles;

import net.drk.command.shield.ShieldStyle;
import net.minecraft.util.Formatting;

public class DefaultStyle implements ShieldStyle {

    @Override
    public Formatting getShieldColor(float percentage) {
        if (percentage <= 0.1) {
            return Formatting.RED;    // Critical, danger
        } else if (percentage <= 0.3) {
            return Formatting.GOLD;   // Low shield
        } else if (percentage <= 0.5) {
            return Formatting.YELLOW; // Medium shield
        } else if (percentage <= 0.75) {
            return Formatting.AQUA;   // High shield
        } else {
            return Formatting.GREEN;  // Full shield
        }
    }
}
