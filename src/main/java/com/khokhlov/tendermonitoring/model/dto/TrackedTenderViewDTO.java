package com.khokhlov.tendermonitoring.model.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TrackedTenderViewDTO(
        String keyword,
        String title,
        String link,
        String procurementLaws,
        String stage,
        String purchaseObject,
        BigDecimal price,
        String currency,
        ZonedDateTime publishedDate,
        ZonedDateTime postedDate,
        ZonedDateTime expirationDate,
        String purchaseCode,
        String author
) {
}
