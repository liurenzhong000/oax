package com.oax.exception;

public class VoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String code="0";

	public VoException(String message) {
        super(message);
    }
	
	public VoException(String code,String message) {
		super(message);
        this.code=code;
    }
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
