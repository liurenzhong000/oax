package com.oax.entity.admin.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/9
 * Time: 10:35
 */
@Data
public class CountRedPacketDto {

    /**
     * 发出总额
     */
    private BigDecimal totalCny;

    /**
     * 领取总额
     */
    private BigDecimal grabCny;

    /**
     * 退还总额
     */
    private BigDecimal returnCny;
}
