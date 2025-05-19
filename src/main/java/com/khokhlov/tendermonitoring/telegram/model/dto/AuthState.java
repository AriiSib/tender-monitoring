package com.khokhlov.tendermonitoring.telegram.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthState {
    private String username;
    private boolean waitingForPassword;
}
