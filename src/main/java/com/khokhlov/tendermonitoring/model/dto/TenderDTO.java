package com.khokhlov.tendermonitoring.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TenderDTO(
        String title,
        String link,
        String searchParams,
        String stage,
        String purchaseObject,
        BigDecimal price,
        LocalDate publishedDate,
        LocalDate updatedDate,
        LocalDate deadline,
        String purchaseCode,
        String author
) {

}
