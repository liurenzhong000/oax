package com.oax.entity.front.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/25 16:20
 * @Description: 充值统计实体，减少数据量传输
 */
@Setter
@Getter
public class RechargeSumVo {

    private BigDecimal qty;
}
