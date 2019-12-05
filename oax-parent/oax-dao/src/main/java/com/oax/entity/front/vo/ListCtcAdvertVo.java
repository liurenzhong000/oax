package com.oax.entity.front.vo;

import com.oax.entity.admin.User;
import com.oax.entity.ctc.Merchant;
import com.oax.entity.enums.CtcAdvertStatus;
import com.oax.entity.enums.CtcAdvertType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class ListCtcAdvertVo {

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

    private CtcAdvertStatus status;

    /**
     * 类别描述
     */
    private String typeDesc;

    /**
     * 状态描述
     */
    private String statusDesc;

    private Date createDate;

    /**
     * 剩余可交易数量
     */
    private BigDecimal remainQty;

    private BigDecimal totalQty;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 商家信息
     */
    private Merchant merchant;

}
