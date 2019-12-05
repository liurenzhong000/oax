package com.oax.entity.ctc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.oax.entity.enums.MerchantStatus;
import com.oax.entity.enums.PaymentWay;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户信息
 */
@Getter
@Setter
@Entity
public class Merchant implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Column(unique = true)
    private Integer userId;

    /**
     * 卖出个数
     * (多币种时，不通用)
     */
    @Column(columnDefinition ="decimal(50,18) DEFAULT '0.00'",nullable = false)
    private BigDecimal saleQty;

    /**卖出金额*/
    @Column(columnDefinition ="decimal(50,18) DEFAULT '0.00'",nullable = false)
    private BigDecimal saleFee;

    /**
     * 卖出成交单数
     */
    private int saleSuccCount;

    /**
     * 生成卖出订单总数
     */
    private int saleTotalCount;

    /**
     * 买入个数
     */
    @Column(columnDefinition ="decimal(50,18) DEFAULT '0.00'",nullable = false)
    private BigDecimal buyQty;

    /**买入金额*/
    @Column(columnDefinition ="decimal(50,18) DEFAULT '0.00'",nullable = false)
    private BigDecimal buyFee;

    /**
     * 买入成交单数
     */
    private int buySuccCount;

    /**
     * 生成买入订单总数
     */
    private int buyTotalCount;

    /**
     * 商家入驻时间
     */
    private Date createDate;

    /**
     * 付款方式，当前只有0：银行卡
     */
    private PaymentWay paymentWay;

    /**
     * 当前记录是否删除（0 正常，1 删除）
     */
    private MerchantStatus status;

}
