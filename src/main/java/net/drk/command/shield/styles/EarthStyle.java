package net.drk.command.shield.styles;

import net.drk.command.shield.ShieldStyle;
import net.minecraft.util.Formatting;

public class EarthStyle implements ShieldStyle {

    @Override
    public Formatting getShieldColor(float percentage) {
        if (percentage <= 0.3) {
            return Formatting.DARK_GREEN; // Low shield is dark green (earthy)
        } else if (percentage <= 0.6) {
            return Formatting.GREEN;     // Medium shield is green (earthy)
        } else {
            return Formatting.GOLD;     // High shield is brown (earthy)
        }
    }
}
