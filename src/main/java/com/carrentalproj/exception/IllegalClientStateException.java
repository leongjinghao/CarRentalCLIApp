package com.carrentalproj.exception;

public class IllegalClientStateException extends RuntimeException {
    public IllegalClientStateException(String message) {
        super(message);
    }
}
