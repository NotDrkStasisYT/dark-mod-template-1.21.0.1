package net.drk.command.shield.styles;

import net.drk.command.shield.ShieldStyle;
import net.minecraft.util.Formatting;

public class StormStyle implements ShieldStyle {

    @Override
    public Formatting getShieldColor(float percentage) {
        if (percentage <= 0.3) {
            return Formatting.DARK_GRAY; // Low shield is dark gray
        } else if (percentage <= 0.6) {
            return Formatting.GRAY;     // Medium shield is gray
        } else {
            return Formatting.BLUE;     // High shield is stormy blue
        }
    }
}
