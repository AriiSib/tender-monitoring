package com.khokhlov.tendermonitoring.util;

import java.math.BigDecimal;

public class PriceParser {
    public static BigDecimal parsePrice(String rawPrice) {
        if (rawPrice == null || rawPrice.isBlank()) {
            return BigDecimal.ZERO;
        }

        String cleaned = rawPrice
                .replaceAll("\\s+", "")
                .replace("₽", "")
                .replace(",", ".");

        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
