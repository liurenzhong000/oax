package com.oax.entity.front;

import java.io.Serializable;

/**
 * MarketCoin
 *
 * @author
 */
public class MarketCoinInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String marketCoinName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarketCoinName() {
        return marketCoinName;
    }

    public void setMarketCoinName(String marketCoinName) {
        this.marketCoinName = marketCoinName;
    }
}