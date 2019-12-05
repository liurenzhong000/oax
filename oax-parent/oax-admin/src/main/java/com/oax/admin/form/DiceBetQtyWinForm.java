package com.oax.admin.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 14:56
 * @Description:
 */
@Setter
@Getter
public class DiceBetQtyWinForm {
    /**币种id*/
    private Integer coinId;

    /**投注金额下限*/
    private BigDecimal minBetQty;

    /**投注金额上限*/
    private BigDecimal maxBetQty;

    /**后台调控概率*/
    private Integer backWin;

    /**是否开启：0关闭 1开启
     * */
    private Integer status;
}
