package com.oax.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 14:31
 * @Description: dice活动首页数据
 */
@Data
public class DiceIndexVo {

    /**注册分享码*/
    private String code;

    /**活动连续天数*/
    private Integer continueDays;

    /**今天是否已经签到*/
    private Boolean singIn;

    /**账户对应coinId的余额*/
    private BigDecimal balance = BigDecimal.ZERO;

    /**最小投注金额*/
    private BigDecimal minBetQty;

    /**最大投注金额*/
    private BigDecimal maxBetQty;

    /**余额显示精度，赢取金额进度*/
    private Integer decimals;

    /**币种名称*/
    private String coinName;

}
