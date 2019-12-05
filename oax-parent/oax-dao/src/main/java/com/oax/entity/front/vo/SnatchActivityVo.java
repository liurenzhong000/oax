package com.oax.entity.front.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 19:26
 * @Description:
 */
@Data
public class SnatchActivityVo {

    private Integer id;

    /**奖池类别*/
    private Integer configId;

    /**奖池名称*/
    private String configName;

    /**期数*/
    private Integer ordinal;

    /**币种id*/
    private Integer coinId;

    /**币种名称*/
    private String coinName;

    /**当前完成进度*/
    private Integer finishQuantity;

    /**最大数量*/
    private Integer quantity;

    /**最大投注数量*/
    private Integer maxQuantity;

    /**单注中奖金额*/
    private BigDecimal unitPayoutQty;

    /**最小投注单位*/
    private BigDecimal unit;

    /**可中奖人数*/
    private Integer winNumber;

//    /**已经投注注数*/
//    private Integer bettedUnits;


}
