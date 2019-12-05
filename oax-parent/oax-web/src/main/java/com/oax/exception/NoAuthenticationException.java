package com.oax.exception;

/**
 * 用户未实名认证异常，提示前端进行跳转到身份证认证页面
 */
public class NoAuthenticationException extends RuntimeException{
    public NoAuthenticationException(String message) {
        super(message);
    }

    public NoAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
