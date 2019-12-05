package com.oax.admin.vo;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.vo.DiceActivityStatisticsVo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2018/12/30 17:31
 * @Description: 后台数据统计
 */
@Data
public class DiceStatisticsData {

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

    private PageInfo<DiceActivityStatisticsVo> pageInfo;

}
