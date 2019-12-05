package com.oax.entity.front.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 18:28
 * @Description:
 */
@Setter
@Getter
public class DiceConfigVo {
    private Integer id;

    /**币种id*/
    private Integer coinId;

    /**对应币种的最小投注金额*/
    private BigDecimal minBetQty;

    /**对应币种的最大投注金额*/
    private BigDecimal maxBetQty;

    private Integer decimals;

    /**币种英文缩写名称*/
    private String coinName;

    /**币种对应icon图片oss地址*/
    private String coinImageUrl;

    /**顺序：越小越前*/
    private Integer sequence;

    /**状态*/
    private Integer status;

    /**手续费-获奖金额扣除手续费*/
    private BigDecimal charges;
}
