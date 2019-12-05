package com.oax.entity.front;

import java.math.BigDecimal;

public class PromoteProfit {
    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 币种名称
     */
    private String marketId;

    /**
     * 金额
     */
    private String money;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
