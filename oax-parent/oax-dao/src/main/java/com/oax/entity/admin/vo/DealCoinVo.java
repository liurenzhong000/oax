package com.oax.entity.admin.vo;

/**
 * 交易对
 */
public class DealCoinVo {
    private Integer id;
    //市场id
    private Integer marketId;
    //交易对
    private String dealTroops;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public String getDealTroops() {
        return dealTroops;
    }

    public void setDealTroops(String dealTroops) {
        this.dealTroops = dealTroops;
    }
}
