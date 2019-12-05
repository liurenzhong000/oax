package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * withdraw
 *
 * @author
 */
@Data
public class Withdraw implements Serializable {
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 币种id
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
     * 提币数量
     */
    private BigDecimal qty;
    
    /**
     * btc估值
     */
    private BigDecimal btcPrice;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 交易哈希
     */
    private String txId;


    /**
     * 区块到账手续费
     */
    private BigDecimal txFee;

    /**
     * 状态 -2:终审不通过 -1：审核不通过 0：待审核 1:待终审 2：已转出 3：已广播 4：已确认
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 转账使用 gasprice
     */
    private Integer gasPrice;

    /**
     * 转账使用 gaslimit
     */
    private Integer gasLimit;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public BigDecimal getActualQty(){
        return qty.subtract(fee);
    }


}