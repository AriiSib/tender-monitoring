package com.khokhlov.tendermonitoring.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LocalDate parseDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            return null;
        }

        rawDate = rawDate.trim();

        try {
            return LocalDate.parse(rawDate, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
