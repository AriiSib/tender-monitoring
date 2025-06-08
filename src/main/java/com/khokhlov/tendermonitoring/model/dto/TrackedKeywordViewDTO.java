package com.khokhlov.tendermonitoring.model.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record TrackedKeywordViewDTO(
        Long id,
        String keyword,
        int interval,
        ZonedDateTime lastPublishedDate,
        LocalDateTime lastUpdateFoundAt,
        boolean active
) {
}