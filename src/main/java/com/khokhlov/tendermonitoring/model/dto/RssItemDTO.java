package com.khokhlov.tendermonitoring.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public record RssItemDTO(
        String title,
        String purchaseCode,
        String link,
        String procurementLaws,
        String stage,
        String purchaseObject,
        BigDecimal price,
        String currency,
        ZonedDateTime publishedDate,
        LocalDate postedDate,
        LocalDate updatedDate,
        String identificationCode,
        String author
) {
}
