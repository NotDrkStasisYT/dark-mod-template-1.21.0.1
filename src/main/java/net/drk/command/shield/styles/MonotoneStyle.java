package net.drk.command.shield.styles;

import net.drk.command.shield.ShieldStyle;
import net.minecraft.util.Formatting;

public class MonotoneStyle implements ShieldStyle {

    @Override
    public Formatting getShieldColor(float percentage) {
        return Formatting.GRAY; // Single color for all percentages
    }
}
