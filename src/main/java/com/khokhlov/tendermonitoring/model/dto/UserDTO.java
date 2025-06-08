package com.khokhlov.tendermonitoring.model.dto;

import com.khokhlov.tendermonitoring.model.entity.Role;

public record UserDTO(
        Long id,
        String username,
        Role role
) {
}
