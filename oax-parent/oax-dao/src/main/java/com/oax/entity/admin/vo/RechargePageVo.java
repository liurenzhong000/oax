package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/13
 * Time: 10:31
 * 转入记录页面 展示vo
 */
@Data
public class RechargePageVo {

    private int id;

    private int userId;

    private String email;

    private String phone;

    private String coinName;


    /**
     * 充值数
     */
    private BigDecimal qty;

    private String fromAddress;

    private String toAddress;

    private Date createTime;

    private int type;
}
