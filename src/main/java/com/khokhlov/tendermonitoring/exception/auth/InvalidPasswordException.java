package com.khokhlov.tendermonitoring.exception.auth;

public class InvalidPasswordException extends AuthenticationException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
