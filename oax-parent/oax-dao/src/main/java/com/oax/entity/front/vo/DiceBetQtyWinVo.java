package com.oax.entity.front.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 15:10
 * @Description:
 */
@Setter
@Getter
public class DiceBetQtyWinVo {
    private Integer id;

    /**币种id*/
    private Integer coinId;

    /**币种名称*/
    private String coinName;

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
