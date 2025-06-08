package com.khokhlov.tendermonitoring.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TenderDTO(
        String title,
        String link,
        String procurementLaws,
        String stage,
        String purchaseObject,
        BigDecimal price,
        String currency,
        LocalDate postedDate,
        LocalDate updatedDate,
        LocalDate expirationDate,
        String purchaseCode,
        String author
) {
}
