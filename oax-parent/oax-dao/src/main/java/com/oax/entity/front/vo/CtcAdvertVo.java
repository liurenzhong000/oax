package com.oax.entity.front.vo;

import com.oax.entity.enums.CtcAdvertType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CtcAdvertVo {

    private Integer id;

    /**
     * 交易币种类型
     */
    private Integer coinId;

    /**
     * 交易币种类型名称
     */
    private String coinName;

    /**
     * 最小下单个数（最小买入数量/ 最小卖出数量）
     */
    private BigDecimal minQty;

    /**
     * 最大下单个数（最大买入数量/ 最大卖出数量）
     */
    private BigDecimal maxQty;

    /**
     * 留言信息
     */
    private String leaveMessage;

    /**
     * 人民币单价
     */
    private BigDecimal cnyPrice;

    /**
     * 交易类型
     */
    private CtcAdvertType type;

    /**
     * 商户用户id
     */
    private Integer userId;

}
