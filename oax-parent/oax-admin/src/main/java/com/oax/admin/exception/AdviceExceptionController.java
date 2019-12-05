package com.oax.admin.exception;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.oax.common.ResultResponse;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/7
 * Time: 17:27
 * 全局异常 捕捉
 */
@RestControllerAdvice
public class AdviceExceptionController {

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResultResponse handle401(ShiroException e) {

        String message = e.getMessage();
        return new ResultResponse(false, message);
    }

    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResultResponse handle401() {
        return new ResultResponse(false, "Unauthorized");
    }

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultResponse globalException(Throwable ex) {
        ex.printStackTrace();
        return new ResultResponse(false, ex.getMessage());
    }

//    // 捕捉其他所有异常
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ResultResponse forbiddenException( Throwable ex) {
//        return new ResultResponse(false, ex.getMessage());
//    }


}
