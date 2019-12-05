package com.oax.entity.front.vo;

import com.oax.entity.enums.CtcOrderType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CtcOrderAdminVo {
    /**法币订单id*/
    private Long id;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 交易币种类型
     */
    private Integer coinId;

    private String coinName;

    /**
     * 交易时的币种单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal qty;

    private BigDecimal totalCost;

    private CtcOrderType type;

    private String typeDesc;
}
