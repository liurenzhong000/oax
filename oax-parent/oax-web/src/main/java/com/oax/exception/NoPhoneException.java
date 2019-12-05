package com.oax.exception;

public class NoPhoneException extends RuntimeException{

    public NoPhoneException(String message) {
        super(message);
    }

    public NoPhoneException(String message, Throwable cause) {
        super(message, cause);
    }
}
