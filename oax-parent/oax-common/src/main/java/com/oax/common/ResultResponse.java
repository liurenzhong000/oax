package com.oax.common;

public class ResultResponse {

    public final static String CODE_NOLOGIN = "-1";//用户未登陆
    public final static String CODE_NOBANKCARD = "-2";//用户未绑定银行卡，前端跳转绑定银行卡页面
    public final static String CODE_NOPHONE = "-3";//用户未绑定手机号，前端跳转绑定手机页面
    public final static String CODE_NOEMAIL = "-4";//用户未绑定邮箱，前端跳转绑定邮箱页面
    public final static String CODE_NOAUTH = "-5";//用户未实名认证，前端跳转身份证认证页面
    public final static String CODE_NOTRANPW = "-6";//用户未设置交易密码，前端跳转到添加交易密码的页面

    private String code;
    private boolean success;
    private String msg;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public ResultResponse() {
        super();
    }

    public ResultResponse(boolean success, String msg) {
        super();
        if (success) {
            this.code = "1";
        } else {
            this.code = "0";
        }

        this.success = success;
        this.msg = msg;
    }

    public ResultResponse(boolean success, Object data) {
        super();
        if (success) {
            this.code = "1";
        } else {
            this.code = "0";
        }
        this.success = success;
        this.data = data;
    }

    public ResultResponse(String code, String msg) {
        super();
        this.success = false;
        this.code = code;
        this.msg = msg;
    }

    public ResultResponse(String code, boolean success, String msg) {
        super();
        this.code = code;
        this.success = success;
        this.msg = msg;
    }

    public ResultResponse(Object data) {
        this.code = "1";
        this.success = true;
        this.data = data;
    }

    public ResultResponse(boolean success, String msg, Object data) {
        this.code = "1";
        this.success = success;
        this.data = data;
        this.msg = msg;
    }
}
