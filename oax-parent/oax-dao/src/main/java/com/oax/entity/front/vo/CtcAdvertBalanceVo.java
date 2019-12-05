package com.oax.entity.front.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 商户发布广告前调用，获取对应币种的余额
 */
@Setter
@Getter
public class CtcAdvertBalanceVo {

    /**余额*/
    private BigDecimal banlance;

    /**币种名称*/
    private String coinName;

}
