package com.oax.entity.front.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberCoinVo {

    private Integer id;

    private BigDecimal banlance;

    private Integer fromUserId;
}
