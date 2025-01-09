package net.drk.command.shield.styles;

import net.drk.command.shield.ShieldStyle;
import net.minecraft.util.Formatting;

public class IceStyle implements ShieldStyle {

    @Override
    public Formatting getShieldColor(float percentage) {
        if (percentage <= 0.3) {
            return Formatting.DARK_BLUE; // Low shield is dark blue
        } else if (percentage <= 0.6) {
            return Formatting.BLUE;     // Medium shield is blue
        } else {
            return Formatting.AQUA;     // High shield is light blue
        }
    }
}
