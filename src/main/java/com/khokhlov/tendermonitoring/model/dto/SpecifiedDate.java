package com.khokhlov.tendermonitoring.model.dto;

import java.time.ZonedDateTime;

public record SpecifiedDate(
        ZonedDateTime postedDate,
        ZonedDateTime expirationDate
) {
}
