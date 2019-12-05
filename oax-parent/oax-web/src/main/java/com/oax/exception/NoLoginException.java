package com.oax.exception;

/**
 * @Auther: hyp
 * @Date: 2019/1/23 18:38
 * @Description:
 */
public class NoLoginException extends RuntimeException{

    public NoLoginException() {
    }

    public NoLoginException(String message) {
        super(message);
    }

    public NoLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
