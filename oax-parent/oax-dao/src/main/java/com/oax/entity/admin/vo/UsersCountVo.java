package com.oax.entity.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/17
 * Time: 11:52
 * 首页统计中
 *          用户统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersCountVo {

    /**
     * 今日注册用户数
     */
    private int countUserRegister;

    /**
     * 总用户
     */
    private int countAllUser;

    /**
     * 活跃用户
     */
    private int countActiveUser;

    /**
     * 持币用户
     */
    private int countHasCoinUser;

    /**
     * 充值用户
     */
    private int countRechargeUser;

    /**
     * 提币用户
     */
    private int countWithdrawUser;

    /**
     * 下单用户
     */
    private int countOrderUser;

    /**
     * pc登录用户
     */
    private int countPcUser;

    /**
     * ios登录用户
     */
    private int countIosUser;

    /**
     * Android登录用户
     */
    private int countAndroidUser;
}
