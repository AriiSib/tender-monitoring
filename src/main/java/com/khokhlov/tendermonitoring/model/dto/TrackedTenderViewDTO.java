package com.khokhlov.tendermonitoring.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TrackedTenderViewDTO(
        String keyword,
        String title,
        String link,
        String procurementLaws,
        String stage,
        String purchaseObject,
        BigDecimal price,
        String currency,
        LocalDateTime publishedDate,
        LocalDateTime postedDate,
        LocalDateTime expirationDate,
        String purchaseCode,
        String author
) {
}
