package com.oax.entity.admin.vo;

import com.oax.entity.enums.CtcOrderStatus;
import com.oax.entity.enums.CtcOrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * CTC用户买入
 */
@Setter
@Getter
public class CtcOrderVo {

    /**ctc订单id*/
    private Long id;

    /**用户id*/
    private Integer userId;

    /**用户名称（手机号或邮箱）*/
    private String userName;

    /**用户名*/
    private String idName;

    /**商家id*/
    private Integer merchantId;

    /**商家用户名*/
    private Integer merchantUserId;

    /**商家姓名*/
    private String merchantIdName;

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

    /**
     * 交易方式
     */
    private String tradeTypeDesc;
}
