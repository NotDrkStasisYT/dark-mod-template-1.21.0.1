package net.drk;

import java.math.BigInteger;

public class PlayerData {
    private LuminCurrency luminCurrency;
    private BigInteger luminEarnedLast24Hours;

    public PlayerData(BigInteger initialLumin) {
        this.luminCurrency = new LuminCurrency(initialLumin);
        this.luminEarnedLast24Hours = BigInteger.ZERO;
    }

    public LuminCurrency getLuminCurrency() {
        return luminCurrency;
    }

    public void setLuminCurrency(LuminCurrency luminCurrency) {
        this.luminCurrency = luminCurrency;
    }

    public BigInteger getLuminEarnedLast24Hours() {
        return luminEarnedLast24Hours;
    }

    public void addLuminEarned(BigInteger amount) {
        this.luminEarnedLast24Hours = this.luminEarnedLast24Hours.add(amount);
    }
}
