package com.example.exception;

public class AuthenticationExceptionCustom extends RuntimeException {
    public AuthenticationExceptionCustom(String message) {
        super(message);
    }

    public AuthenticationExceptionCustom(String message, Throwable cause) {
        super(message, cause);
    }
}

