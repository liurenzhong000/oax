package com.oax.admin.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 15:21
 * @Description:
 */
@Data
public class DiceRollUnderWinForm {

    /**币种id*/
    private Integer coinId;

    /**投注数下限*/
    private BigDecimal minRollUnder;

    /**投注数上限*/
    private BigDecimal maxRollUnder;

    /**后台调控概率*/
    private Integer backWin;

    /**是否开启：0关闭 1开启
     * */
    private Integer status;

}
