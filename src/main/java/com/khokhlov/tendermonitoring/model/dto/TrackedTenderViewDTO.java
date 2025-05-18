package com.khokhlov.tendermonitoring.model.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TrackedTenderViewDTO(
        String keyword,
        String title,
        String link,
        BigDecimal price,
        ZonedDateTime expirationDate,
        ZonedDateTime publishedDate
) {
}
