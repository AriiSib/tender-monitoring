package com.khokhlov.tendermonitoring.model.dto;

import java.time.LocalDateTime;

public record TrackedKeywordViewDTO(
        Long id,
        String keyword,
        int interval,
        LocalDateTime lastPublishedDate,
        LocalDateTime lastUpdateFoundAt,
        boolean active
) {
}