package net.drk.number;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberFormatter {

    // Define suffixes for large numbers
    private static final String[] SUFFIXES = {
            "", "K", "M", "B", "T", "Qd", "Qn", "Sx", "Sp", "Oc",
            "N", "Dc", "Ud", "Dd", "Td", "QdD", "QdN", "Sd", "St", "Ot",
            "Nt", "Tn", "Ut", "Dtn", "Ttn", "Qat", "Qit", "Sxg", "Stg",
            "Ogt", "Ngt", "Tg", "Utg", "Dtg", "Trt", "Qrt", "Qnt", "Sxt",
            "Snt", "Ots", "Nts", "Qut", "Uqt", "Dut", "Trqt", "Qrtq", "Qntq",
            "Sxqt", "Snqt", "Octq", "Ntq", "Qnq", "Uqn", "Dqn", "Tqn",
            "Qnqn", "Qiqn", "Sqnqn", "Stqn", "Oqnqn", "Nqnqn", "Sxgqn",
            "Sngqn", "Ogqn", "Ngqn", "Sxgxg", "Sxnxg", "Ogxg", "Ngtgxg",
            "Tgxg", "Utgxg", "Dgxg", "Udgxg", "Dngxg", "Tngxg", "Qngxg",
            "Qigxg", "Stgxg", "Snxg", "Ongxg", "Nngxg", "Sxgg", "Sngg",
            "Ogg", "Ngg", "Tgg", "Ugg", "Dgg", "Uggg", "Dngg", "Tngg",
            "Qngg", "Qigg", "Stgg", "Sngg", "Oggg", "Nggg", "Sxngg",
            "Snggg", "Onggg", "Nonggg"
    };

    public static String formatPrice(BigInteger price) {
        // Handle special case for zero
        if (price.equals(BigInteger.ZERO)) {
            return "0";
        }

        // Convert BigInteger to BigDecimal for more precise division
        BigDecimal decimalPrice = new BigDecimal(price);
        int suffixIndex = 0;
        BigDecimal unit = BigDecimal.valueOf(1000);

        // Move up the suffix index while the number is greater than or equal to 1000
        while (decimalPrice.compareTo(unit) >= 0 && suffixIndex < SUFFIXES.length - 1) {
            decimalPrice = decimalPrice.divide(unit);
            suffixIndex++;
        }

        // Format the number with one decimal place
        return String.format("%.1f%s", decimalPrice.doubleValue(), SUFFIXES[suffixIndex]);
    }
    public static String formatDamageB(BigInteger damage) {
        // Handle special case for zero
        if (damage.equals(BigInteger.ZERO)) {
            return "0";
        }

        BigInteger unit = BigInteger.valueOf(1000);
        int suffixIndex = 0;

        // Loop to find the appropriate suffix by dividing the damage by 1000
        while (damage.compareTo(unit) >= 0 && suffixIndex < SUFFIXES.length - 1) {
            damage = damage.divide(unit);
            suffixIndex++;
        }

        // Return the formatted damage with the appropriate suffix
        return damage.toString() + SUFFIXES[suffixIndex];
    }
    public static String formatDamage(double damage) {
        // Handle special case for zero
        if (damage == 0) {
            return "0";
        }

        try {
            // Convert double to BigDecimal for more precise division
            BigDecimal decimalDamage = BigDecimal.valueOf(damage);
            int suffixIndex = 0;
            BigDecimal unit = BigDecimal.valueOf(1000);

            // Move up the suffix index while the number is greater than or equal to 1000
            while (decimalDamage.compareTo(unit) >= 0 && suffixIndex < SUFFIXES.length - 1) {
                decimalDamage = decimalDamage.divide(unit);
                suffixIndex++;
            }

            // Format the number with one decimal place
            return String.format("%.1f%s", decimalDamage.doubleValue(), SUFFIXES[suffixIndex]);
        } catch (NumberFormatException e) {
            // Log invalid input and return a fallback
            System.err.println("Invalid damage value passed to formatDamage: " + damage);
            return "Invalid Damage";
        }
    }

}