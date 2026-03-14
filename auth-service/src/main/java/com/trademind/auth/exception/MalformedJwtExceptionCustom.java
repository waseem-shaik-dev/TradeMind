package com.trademind.auth.exception;

public class MalformedJwtExceptionCustom extends RuntimeException {

    public MalformedJwtExceptionCustom(String message) {
        super(message);
    }
}
