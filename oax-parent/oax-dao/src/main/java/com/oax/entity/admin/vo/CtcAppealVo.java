package com.oax.entity.admin.vo;

import com.oax.entity.enums.CtcAppealStatus;
import com.oax.entity.enums.CtcOrderStatus;
import com.oax.entity.enums.CtcOrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class CtcAppealVo {

    /**申诉记录id*/
    private Integer id;

    /**
     * ctc订单号
     */
    private Long ctcOrderId;

    /**用户id*/
    private Integer userId;

    /**用户名称（手机号或邮箱）*/
    private String userName;

    /**用户姓名*/
    private String idName;

    /**商家id*/
    private Integer merchantId;

    /**商家用户id*/
    private Integer merchantUserId;

    /**商家姓名*/
    private String merchantIdName;

    /**商户用户名称（手机号或邮箱）*/
    private String merchantUserName;

    /**数量*/
    private BigDecimal qty;

    /**总金额*/
    private BigDecimal totalCost;

    /**单价*/
    private BigDecimal price;

    /**类型*/
    private CtcOrderType type;

    /**类型描述*/
    private String typeDesc;

    /**状态*/
    private CtcOrderStatus orderStatus;

    /**状态描述*/
    private String orderStatusDesc;

    /**买入时间*/
    private Date createDate;

    /**到账时间*/
    private Date finishDate;

    /**
     * 交易方式
     */
    private String tradeTypeDesc;

    /**
     * 申述详情
     */
    private String appealDesc;

    /**
     * 申述状态（申诉中，处理中，处理完成，已撤销）
     */
    private CtcAppealStatus status;

    private String statusDesc;

    /**
     * 申诉人
     */
    private String appealUser;

}
