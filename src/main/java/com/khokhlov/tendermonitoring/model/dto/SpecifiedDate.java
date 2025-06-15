package com.khokhlov.tendermonitoring.model.dto;

import java.time.LocalDateTime;

public record SpecifiedDate(
        LocalDateTime postedDate,
        LocalDateTime expirationDate
) {
}
