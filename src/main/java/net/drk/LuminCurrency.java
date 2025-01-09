package net.drk;

import net.drk.network.lumin.LuminNetworking;
import net.drk.number.NumberFormatter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class LuminCurrency {
    private static final LuminCurrency INSTANCE = new LuminCurrency();

    private BigInteger amount; // Total lumin
    private BigInteger mobLumin; // Lumin earned from killing mobs (to track separately)
    private Map<String, BigInteger> playerLumin = new HashMap<>(); // Store lumin per player

    public LuminCurrency() {
        this.amount = BigInteger.ZERO;
        this.mobLumin = BigInteger.ZERO;
    }

    public LuminCurrency(BigInteger initialAmount) {
        this.amount = initialAmount;
        this.mobLumin = BigInteger.ZERO;
    }

    public void addLumin(ServerPlayerEntity player, BigInteger amount) {
        this.amount = this.amount.add(amount);
        playerLumin.put(player.getName().getString(), playerLumin.getOrDefault(player.getName().getString(), BigInteger.ZERO).add(amount));
        LuminNetworking.sendLuminDataToPlayer(player);
    }


    public static LuminCurrency getInstance() {
        return INSTANCE;
    }

    // Method to add lumin earned from killing mobs, scaled by a multiplier
    public void addMobLumin(BigInteger baseMobAmount, BigDecimal multiplier) {
        BigDecimal mobAmountAsDecimal = new BigDecimal(baseMobAmount);
        BigDecimal scaledMobAmount = mobAmountAsDecimal.multiply(multiplier);

        // Convert back to BigInteger for storage
        BigInteger finalMobAmount = scaledMobAmount.setScale(0, RoundingMode.DOWN).toBigInteger(); // Use rounding if needed

        this.mobLumin = this.mobLumin.add(finalMobAmount); // Track mob lumin separately
        this.amount = this.amount.add(finalMobAmount); // Update total lumin
    }

    // New method to add 1% of mob's max health as lumin

    public void addLuminFromMaxShield(BigInteger maxShield, BigDecimal multiplier) {
        // Calculate 1% of the max shield
        BigDecimal onePercentShield = new BigDecimal(maxShield).multiply(BigDecimal.valueOf(0.01));

        // Apply the multiplier
        BigDecimal luminWithMultiplier = onePercentShield.multiply(multiplier);

        // Convert to BigInteger and add to the lumin total
        BigInteger luminFromShield = luminWithMultiplier.setScale(0, RoundingMode.DOWN).toBigInteger();
        this.mobLumin = this.mobLumin.add(luminFromShield); // Track mob lumin separately
        this.amount = this.amount.add(luminFromShield); // Update total lumin
    }

    public void subtractLumin(BigInteger amount) {
        if (this.amount.compareTo(amount) >= 0) {
            this.amount = this.amount.subtract(amount);
        } else {
            throw new IllegalArgumentException("Insufficient Lumin");
        }
    }

    public BigInteger getAmount(ServerPlayerEntity player) {
        return playerLumin.getOrDefault(player.getName().getString(), amount);
    }

    public BigInteger getAmount() {
        return amount;
    }

    public BigInteger getMobLumin() {
        return mobLumin; // Getter for mob lumin, if needed
    }

    public String getFormattedAmount() {
        return NumberFormatter.formatPrice(amount) + " Lumin";
    }

    public String getIconAmount() {
        return NumberFormatter.formatPrice(amount) + " Lumin";
    }

    // Update client lumin data, similar to ShieldManager
    public void updateClientLumin(String playerName, BigInteger lumin) {
        playerLumin.put(playerName, lumin); // Store lumin per player
        System.out.println("Updated lumin data for " + playerName + ": " + lumin);

    }

    // Method to send lumin data to the client (similar to ShieldNetworking)
    public void onPlayerLogin(ServerPlayerEntity player) {
        // Assuming we have a method to send lumin data to the client
        LuminNetworking.sendLuminDataToPlayer(player);
    }
}
