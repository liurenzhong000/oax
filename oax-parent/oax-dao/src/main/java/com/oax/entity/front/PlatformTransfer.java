package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * platform_transfer
 *
 * @author
 */
@Data
@ToString
public class PlatformTransfer implements Serializable {
    private Integer id;

    /**
     * 类型 1：申请手续费 2：提币到主地址 3：转到冷钱包
     */
    private Integer type;

    /**
     * 币id
     */
    private Integer coinId;

    /**
     * 转出地址
     */
    private String fromAddress;

    /**
     * 转入地址
     */
    private String toAddress;

    /**
     * 转账数量
     */
    private BigDecimal qty;

    /**
     * 交易hash
     */
    private String txId;

    /**
     * 交易费
     */
    private BigDecimal txFee;

    /**
     * 状态 0：失败 1：已广播 2：已打包 3：已确认
     */
    private Integer status;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 转账使用gasPrice
     */
    private Integer gasPrice;

    /**
     * 转账使用gasLimit
     */
    private Integer gasLimit;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;


}