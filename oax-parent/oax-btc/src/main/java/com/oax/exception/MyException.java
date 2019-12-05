package com.oax.exception;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 19:10
 * 项目异常
 */
public class MyException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyException(String message) {
        super(message);
    }
}
