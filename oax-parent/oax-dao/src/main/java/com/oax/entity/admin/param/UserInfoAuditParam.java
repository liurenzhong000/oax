package com.oax.entity.admin.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserInfoAuditParam extends PageParam {
    //认证等级 一级
    public static final Integer LEVEL_TYPE_ONE = 1;
    //认证等级 二级
    public static final Integer LEVEL_TYPE_TWO = 2;
    //认证等级 三级
    public static final Integer LEVEL_TYPE_THREE = 3;
    //审核状态  1待审核
    public static final Integer CHECK_STATUS_CHECK_PENDING = 1;
    //是否被锁定 0未锁定
    public static final Integer LOCK_STATUS_ZERO = 0;
    //用户id
    private Integer id;
    // 证件姓名
    private String name;
    //模糊查询条件 电话/邮箱
    private String emailOrPhone;
    // 证件号
    private String cardNo;
    //注册起始时间
    private String regBeginDate;
    //注册结束时间
    private String regEndDate;
    // 等级 0未激活 1 2 3
    private Integer level;
    //lv2审核状态  -1未通过  0未认证  1待审核 2审核通过
    private Integer checkStatus;
    // 是否被锁定 0 未锁定 1锁定
    private Integer lockStatus;
    //lv2审核通过时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
}
