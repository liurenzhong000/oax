package com.oax.exception;

public class NoBankCardException extends RuntimeException{
    public NoBankCardException(String message) {
        super(message);
    }

    public NoBankCardException(String message, Throwable cause) {
        super(message, cause);
    }
}