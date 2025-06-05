package com.khokhlov.tendermonitoring.exception.auth;

public class InvalidUsernameException extends AuthenticationException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
