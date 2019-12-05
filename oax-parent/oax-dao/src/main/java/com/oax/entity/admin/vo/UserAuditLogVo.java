package com.oax.entity.admin.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/13
 * Time: 18:18
 * 用户审核 logvo
 */
@Data
public class UserAuditLogVo {

    //处理员ID
    private Integer adminId;
    //处理员姓名
    private String adminName;
    //处理时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date disposeTime;
    //描述
    private String describes;
}
