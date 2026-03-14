package com.trademind.auth.exception;

public class AccessTokenExpiredException extends RuntimeException {

    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
