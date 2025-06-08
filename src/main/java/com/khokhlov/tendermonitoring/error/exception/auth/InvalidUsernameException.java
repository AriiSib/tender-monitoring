package com.khokhlov.tendermonitoring.error.exception.auth;

public class InvalidUsernameException extends AuthenticationException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
