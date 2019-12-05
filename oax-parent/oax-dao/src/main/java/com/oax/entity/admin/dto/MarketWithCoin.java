package com.oax.entity.admin.dto;

import com.oax.entity.front.Market;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 20:21
 * market 且带出 币种
 */
public class MarketWithCoin extends Market {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String coinName;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

}
