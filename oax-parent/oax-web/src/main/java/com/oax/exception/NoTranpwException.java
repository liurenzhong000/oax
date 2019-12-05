package com.oax.exception;

public class NoTranpwException extends RuntimeException{

    public NoTranpwException(String message) {
        super(message);
    }

    public NoTranpwException(String message, Throwable cause) {
        super(message, cause);
    }
}
