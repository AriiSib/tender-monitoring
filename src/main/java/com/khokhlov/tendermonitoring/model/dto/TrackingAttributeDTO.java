package com.khokhlov.tendermonitoring.model.dto;

import java.util.List;

public record TrackingAttributeDTO(
        String keyword,
        int interval,
        List<String> stages,
        List<String> types,
        Integer pageSize,
        String purchaseCode
) {
}
