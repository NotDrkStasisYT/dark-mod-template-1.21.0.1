package net.drk.command.shield;

import net.minecraft.util.Formatting;

public interface ShieldStyle {
    Formatting getShieldColor(float percentage);
}
