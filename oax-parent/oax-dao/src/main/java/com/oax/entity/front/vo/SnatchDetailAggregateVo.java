package com.oax.entity.front.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/18 20:05
 * @Description: 我的投注列表所有投注记录统计
 */
@Data
public class SnatchDetailAggregateVo {

    /**奖池id*/
    private Integer activityId;

    /**期号*/
    private Integer ordinal;

    /**币种名称*/
    private String coinName;

    /**投注注数*/
    private Integer betUnits;

    /**总投注金额*/
    private BigDecimal betQty;

    /**状态：0未开奖1已开奖*/
    private Integer status;

    /**中奖个数*/
    private Integer winCount;

    /**中奖金额*/
    private BigDecimal payoutQty;

    /**编号*/
    private String numberStr;

}
