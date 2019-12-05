package com.oax.admin.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 17:25
 * @Description:
 */
@Setter
@Getter
public class DiceConfigForm {

    /**币种id*/
    private Integer coinId;

    /**对应币种的最小投注金额*/
    private BigDecimal minBetQty;

    /**对应币种的最大投注金额*/
    private BigDecimal maxBetQty;

    /**手续费-获奖金额扣除手续费*/
    private BigDecimal charges;

    /**精度，小数点位数*/
    private Integer decimals;

    /**顺序：越小越前*/
    private Integer sequence;

    /**状态*/
    private Integer status;
}
