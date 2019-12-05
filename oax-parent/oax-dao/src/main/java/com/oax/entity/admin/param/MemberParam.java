package com.oax.entity.admin.param;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MemberParam extends PageParam {
    //用户id
    private Integer id;
    private String name;
    //手机号
    private String emailOrPhone;
    private String cardNo;
    // 来源 1 pc 2 ios 3 android
    private Integer source;
    //注册起始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regBeginDate;
    //注册结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regEndDate;
    //认证开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkBeginDate;
    //认证结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkEndDate;

    // 是否被锁定 0 未锁定 1锁定
    private Integer lockStatus;
    // 证件姓名
    private Integer level;


}
