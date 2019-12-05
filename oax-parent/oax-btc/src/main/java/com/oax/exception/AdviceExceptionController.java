package com.oax.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.oax.common.ResultResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/26
 * Time: 14:21
 * 全局异常捕捉
 */
@RestControllerAdvice
@Slf4j
public class AdviceExceptionController {

    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultResponse globalException(Throwable ex) {
        log.error("全局捕捉异常", ex);
        return new ResultResponse(false, ex.getMessage());
    }
}
