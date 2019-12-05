package com.oax.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

public class RushToBuyVO {
    /**
     * 活动ID
     */
    @NotNull(message = "活动id不能为空")
    private Integer activeId;
    /**
     * 币种ID
     */
    @NotNull(message = "币种id不能为空")
    private Integer marketId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户id不能为空")
    private  Integer userId;

    /**
     * 已锁金额
     */
    @NotNull(message = "锁仓金额不能为空")
    private BigDecimal joinMoney;


    public Integer getActiveId() {
        return activeId;
    }

    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public BigDecimal getJoinMoney() {
        return joinMoney;
    }

    public void setJoinMoney(BigDecimal joinMoney) {
        this.joinMoney = joinMoney;
    }


    @Override
    public String toString() {
        return "RushToBuyVO{" +
                "activeId=" + activeId +
                ", marketId=" + marketId +
                ", userId=" + userId +
                ", joinMoney=" + joinMoney +
                '}';
    }
}
