package com.khokhlov.tendermonitoring.error.exception.auth;

public class InvalidPasswordException extends AuthenticationException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
