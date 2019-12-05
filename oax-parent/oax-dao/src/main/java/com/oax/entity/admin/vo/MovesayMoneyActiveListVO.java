package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

public class MovesayMoneyActiveListVO {
    /**
     *
     */
    private String id;

    /**
     * 活动id
     */
    private Integer activeId;

    /**
     * 币种id
     */
    private Integer marketId;



    /**
     * 用户id

     */
    private Integer userId;


    /**
     * 用户名称
     */
    private String userName;


    /**
     * 已锁金额(参与金额)
     */
    private BigDecimal joinMoney;

    /**
     * 累计锁仓
     */
    private BigDecimal cumulativeJoin;


    /**
     * 年化收益率(单位*100)
     */
    private Integer profits;

    /**
     * 收益金额
     */
    private BigDecimal profitsMoney;


    /**
     * 已提现收益
     */
    private BigDecimal depositMoney;


    /**
     * 计息-开始时间
     */
    private Date startTime;

    /**
     * 计息-结束时间
     */
    private Date endTime;

    /**
     * 1-待计息,2-计息中，3-已发放
     */
    private Integer status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getActiveId() {
        return activeId;
    }

    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Integer getProfits() {
        return profits;
    }

    public void setProfits(Integer profits) {
        this.profits = profits;
    }

    public BigDecimal getProfitsMoney() {
        return profitsMoney;
    }

    public void setProfitsMoney(BigDecimal profitsMoney) {
        this.profitsMoney = profitsMoney;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public BigDecimal getCumulativeJoin() {
        return cumulativeJoin;
    }

    public void setCumulativeJoin(BigDecimal cumulativeJoin) {
        this.cumulativeJoin = cumulativeJoin;
    }

    public BigDecimal getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
