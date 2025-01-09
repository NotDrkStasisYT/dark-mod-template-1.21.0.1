package net.drk;

import java.math.BigDecimal;

public class MultiplierCalculator {
    private BigDecimal baseMultiplier; // Use BigDecimal for decimal multipliers

    public MultiplierCalculator(BigDecimal baseMultiplier) {
        this.baseMultiplier = baseMultiplier;
    }

    // Method to calculate the total multiplier, setting weaponMultiplier and onlineFriends to 0 if not present
    public BigDecimal calculateTotalMultiplier(Integer onlineFriends, BigDecimal weaponMultiplier) {
        // If onlineFriends is null, treat it as 0
        BigDecimal friendMultiplier = BigDecimal.valueOf(onlineFriends != null ? onlineFriends : 0);
        // If weaponMultiplier is null, treat it as 0
        BigDecimal finalWeaponMultiplier = (weaponMultiplier != null) ? weaponMultiplier : BigDecimal.ZERO;

        // Calculate total multiplier
        return baseMultiplier.add(friendMultiplier).add(finalWeaponMultiplier);
    }

    public BigDecimal getBaseMultiplier() {
        return baseMultiplier;
    }

    public void setBaseMultiplier(BigDecimal baseMultiplier) {
        this.baseMultiplier = baseMultiplier;
    }
}
