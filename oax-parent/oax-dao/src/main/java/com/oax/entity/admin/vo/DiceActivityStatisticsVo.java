package com.oax.entity.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2018/12/30 15:15
 * @Description: dice游戏统计数据
 */
@Data
public class DiceActivityStatisticsVo {

    /**用户id*/
    private Integer userId;

    /**投注金额*/
    private BigDecimal betQty;

    /**中奖金额*/
    private BigDecimal payoutQty;

    /**收益金额*/
    private BigDecimal benefitQty;

    /**手续费*/
    private BigDecimal chargesQty;

}
