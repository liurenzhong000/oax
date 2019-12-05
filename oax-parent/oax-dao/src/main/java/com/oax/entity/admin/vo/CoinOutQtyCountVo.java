package com.oax.entity.admin.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:49
 */
@Data
public class CoinOutQtyCountVo {

    /**
     * 币种id
     */
    private int coinId;

    /**
     * 币种名称
     */
    private String coinName;

    /**
     * 钱包地址
     */
    private String mainAddress;
    /**
     * 币种类型：1eth代币 2 BTC 3 EHT_TOKEN
     */
    private Integer type;

    /**
     * 钱包剩余
     */
    private String walletBalance;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * omni唯一标识
     */
    private Integer propertyid;
}
