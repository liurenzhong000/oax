package com.oax.entity.front.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户中心-邀请的好友的列表
 */
@Data
public class InviteUserVo {
    //用户id
    private Integer id;
    //注册时间
    private Date registerTime;

    //等级
    private Integer level;

    private Integer checkStatus;

    //是否认证
    private Boolean authentication;

    //层级
    private String tier;
}
