package com.oax.entity.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2018/12/30 18:20
 * @Description: dice游戏聚合统计数据
 */
@Data
public class DiceStatisticsAggreVo {
    /**投注金额*/
    private BigDecimal allBetQty;

    /**中奖金额*/
    private BigDecimal allPayoutQty;

    /**收益金额*/
    private BigDecimal allBenefitQty;

    /**手续费*/
    private BigDecimal allChargesQty;

    /**用户投注次数*/
    private BigDecimal countBetTime;

    /**参与人数*/
    private Integer countUser;
}
