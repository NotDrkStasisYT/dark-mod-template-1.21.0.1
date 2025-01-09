package net.drk.entity.custom;

import java.math.BigInteger;

public class ShieldStrength {
    private BigInteger value;

    public ShieldStrength(String value) {
        this.value = new BigInteger(value);
    }

    public ShieldStrength(BigInteger value) {
        this.value = value;
    }

    public ShieldStrength add(ShieldStrength other) {
        return new ShieldStrength(this.value.add(other.value));
    }

    public ShieldStrength subtract(ShieldStrength other) {
        return new ShieldStrength(this.value.subtract(other.value));
    }

    public ShieldStrength multiply(BigInteger multiplier) {
        return new ShieldStrength(this.value.multiply(multiplier));
    }

    public ShieldStrength divide(BigInteger divisor) {
        return new ShieldStrength(this.value.divide(divisor));
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }

    public void setValue(BigInteger newValue) {
        this.value = newValue;
    }
}
