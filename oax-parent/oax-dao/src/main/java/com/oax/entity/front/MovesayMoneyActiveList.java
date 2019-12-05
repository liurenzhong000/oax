package com.oax.entity.front;

import java.math.BigDecimal;
import java.util.Date;

public class MovesayMoneyActiveList {
    /**
     * 订单编号
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
     * 周期天数
     */
    private Integer date;

    /**
     * 计息方式(1-静态  2-动态)
     */
    private Integer type;

    /**
     * 年化收益率(单位*100)
     */
    private Integer profits;

    /**
     * 收益金额
     */
    private BigDecimal profitsMoney;

    /**
     * 已锁金额(参与金额)
     */
    private BigDecimal joinMoney;

    /**
     * 累计投入锁仓
     */
    private BigDecimal cumulativeJoin;

    /**
     * 已提现收益
     */
    private BigDecimal depositMoney;

    /**
     * 1-待计息,2-计息中，3-已发放
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 计息-开始时间
     */
    private Date startTime;

    /**
     * 计息-结束时间
     */
    private Date endTime;

    /**
     * 订单编号
     * @return id 订单编号
     */
    public String getId() {
        return id;
    }

    /**
     * 订单编号
     * @param id 订单编号
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 活动id
     * @return active_id 活动id
     */
    public Integer getActiveId() {
        return activeId;
    }

    /**
     * 活动id
     * @param activeId 活动id
     */
    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    /**
     * 币种id
     * @return market_id 币种id
     */
    public Integer getMarketId() {
        return marketId;
    }

    /**
     * 币种id
     * @param marketId 币种id
     */
    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    /**
     * 用户id
     * @return user_id 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 用户id
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 周期天数
     * @return date 周期天数
     */
    public Integer getDate() {
        return date;
    }

    /**
     * 周期天数
     * @param date 周期天数
     */
    public void setDate(Integer date) {
        this.date = date;
    }

    /**
     * 计息方式(1-静态  2-动态)
     * @return type 计息方式(1-静态  2-动态)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 计息方式(1-静态  2-动态)
     * @param type 计息方式(1-静态  2-动态)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 年化收益率(单位*100)
     * @return profits 年化收益率(单位*100)
     */
    public Integer getProfits() {
        return profits;
    }

    /**
     * 年化收益率(单位*100)
     * @param profits 年化收益率(单位*100)
     */
    public void setProfits(Integer profits) {
        this.profits = profits;
    }

    /**
     * 收益金额
     * @return profits_money 收益金额
     */
    public BigDecimal getProfitsMoney() {
        return profitsMoney;
    }

    /**
     * 收益金额
     * @param profitsMoney 收益金额
     */
    public void setProfitsMoney(BigDecimal profitsMoney) {
        this.profitsMoney = profitsMoney;
    }

    /**
     * 已锁金额(参与金额)
     * @return join_money 已锁金额(参与金额)
     */
    public BigDecimal getJoinMoney() {
        return joinMoney;
    }

    /**
     * 已锁金额(参与金额)
     * @param joinMoney 已锁金额(参与金额)
     */
    public void setJoinMoney(BigDecimal joinMoney) {
        this.joinMoney = joinMoney;
    }

    /**
     * 累计投入锁仓
     * @return cumulative_join 累计投入锁仓
     */
    public BigDecimal getCumulativeJoin() {
        return cumulativeJoin;
    }

    /**
     * 累计投入锁仓
     * @param cumulativeJoin 累计投入锁仓
     */
    public void setCumulativeJoin(BigDecimal cumulativeJoin) {
        this.cumulativeJoin = cumulativeJoin;
    }

    /**
     * 已提现收益
     * @return deposit_money 已提现收益
     */
    public BigDecimal getDepositMoney() {
        return depositMoney;
    }

    /**
     * 已提现收益
     * @param depositMoney 已提现收益
     */
    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }

    /**
     * 1-待计息,2-计息中，3-已发放
     * @return status 1-待计息,2-计息中，3-已发放
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1-待计息,2-计息中，3-已发放
     * @param status 1-待计息,2-计息中，3-已发放
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     * @return update_time 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 计息-开始时间
     * @return start_time 计息-开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 计息-开始时间
     * @param startTime 计息-开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 计息-结束时间
     * @return end_time 计息-结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 计息-结束时间
     * @param endTime 计息-结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}