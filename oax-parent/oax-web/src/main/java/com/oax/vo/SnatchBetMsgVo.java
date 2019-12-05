package com.oax.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/18 14:03
 * @Description: 一元夺宝首页用户投注记录信息
 */
@Data
public class SnatchBetMsgVo {

    /**用户投注时间*/
    private Date createTime;

    /**用户id （手机号或邮箱）*/
    private String phoneOrEmail;

    /**币种名称short_name*/
    private String coinName;

    /**期数*/
    private Integer ordinal;

    /**奖池名称*/
    private String configName;

    /**用户投注金额*/
    private BigDecimal betQty;

    /**用户投注编号第一个*/
    private String numberStr;

    /**投注注数*/
    private Integer betTime;

}
