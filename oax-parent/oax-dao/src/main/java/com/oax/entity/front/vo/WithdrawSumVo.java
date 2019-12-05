package com.oax.entity.front.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/25 16:24
 * @Description: 提现统计实体
 */
@Setter
@Getter
public class WithdrawSumVo {

    private BigDecimal qty;

    private BigDecimal fee;

    private BigDecimal txFee;
}
