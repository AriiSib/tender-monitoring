package com.khokhlov.tendermonitoring.error.exception.auth;

public abstract class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
