package com.oax.api;

public class ApiException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String errCode;

    public ApiException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public ApiException(Exception e) {
        super(e);
        this.errCode = e.getClass().getName();
    }

    public String getErrCode() {
        return this.errCode;
    }

}
