package com.khokhlov.tendermonitoring.model.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String username;
    private String password;
    private String repeatPassword;
}
