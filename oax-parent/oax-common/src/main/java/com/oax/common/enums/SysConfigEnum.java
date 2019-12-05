package com.oax.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/5
 * Time: 11:47
 * <p>
 * 系统 配置枚举类
 */
@Getter
public enum SysConfigEnum {

    /**
     * 交易对 手续费率
     */
    MARKET_FEE_RATE("marketFeeRate"),
    /**
     * 交易挖矿回馈门槛
     */
    TRADE_FEEDBACK("tradeFeedback"),
    /**
     * 交易挖矿回馈门槛
     */
    IS_THRESHOLD("is_threshold"),
    /**
     * ETH 查询高度
     */
    ETH_BLOCK_NUMBER("ETHBlockNumber"),

    /**
     * USDT区块高度
     */
    USDT_BLOCK_NUMBER("USDTBlockNumber"),

    /**
     * 首页Market展示数
     */
    INDEX_PAGE_MARKET_LIMIT("indexPageMarketLimit"),
	
	/**
     * level1 提现额度
     */
	LEVEL1_BTC("level1"),
    
    /**
     * level2 提现额度
     */
	LEVEL2_BTC("level2"),
    
    /**
     * level3 提现额度
     */
	LEVEL3_BTC("level3"),


    ;
    private String name;

    SysConfigEnum(String name) {
        this.name = name;
    }
}
