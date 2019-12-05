package com.oax.entity.front.vo;

import com.oax.entity.enums.CtcOrderStatus;
import com.oax.entity.enums.CtcOrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class CtcOrderForMerchantVo {

    /**ctc订单id*/
    private Long id;

    /**客户名称*/
    private String idName;

    /**联系方式*/
    private String contactWay;

    /**买入数量*/
    private BigDecimal qty;

    /**买入总金额*/
    private BigDecimal totalCost;

    /**单价*/
    private BigDecimal price;

    /**类型*/
    private CtcOrderType type;

    /**类型描述*/
    private String typeDesc;

    /**状态*/
    private CtcOrderStatus status;

    /**状态描述*/
    private String statusDesc;

    /**买入时间*/
    private Date createDate;

    /**到账时间*/
    private Date finishDate;

    /**申诉状态时是否为申诉人*/
    private boolean appealed;

    /**出币方*/
    private Integer fromUserId;

    /**收币方*/
    private Integer toUserId;
}
