package com.oax.entity.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class MovesayMoneyActive {
    /**
     * 
     */
    private Integer id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 周期天数
     */
    @NotNull(message = "周期不能为空")
    private Integer date;

    /**
     * 年化收益率(单位*100)
     */
    @NotNull(message = "年化收益率不能为空")  
    private Integer profits;

    /**
     * 1-活动即将开始,2-抢购中,3-计息中,4-发放完毕，5-活动已结束
     */
    private Integer status;

    /**
     * 参与人数
     */
    private Integer joinNum;

    /**
     * 已锁金额(参与金额)
     */
    private BigDecimal joinMoney;

    /**
     * 币种id
     */
    @NotNull(message = "币种不能为空")    
    private Integer marketId;

    /**
     * 总金额
     */
    private BigDecimal money;

    /**
     * 锁仓上限
     */
    @NotNull(message = "锁仓上限不能为空")
    private BigDecimal limitMax;

    /**
     * 单笔起购数量
     */
    @NotNull(message = "单笔起购数量不能为空")    
    private BigDecimal limitMin;

    /**
     * 是否推荐项目,1:是,0:否
     */
    @NotNull(message = "是否推荐状态不能为空") 
    private Byte recommend;

    /**
     * 禁用与开启,1:开启,禁用:0
     */
    @NotNull(message = "是否开启状态不能为空")
    private Byte displayOpen;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 开始时间
     */
    @NotNull(message = "活动开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "活动结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 计息开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date interstartTime;

    /**
     * 计息结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date interendTime;

    /**
     * 余利宝活动类型(1 静态、 2 动态)
     */
    @NotNull(message = "余利宝活动类型不能为空")
    private Integer type;

    /**
     * 满多少可提现
     */
    private BigDecimal depositMoney;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 标题
     * @return title 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 标题
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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
     * 1-活动即将开始,2-抢购中,3-计息中,4-发放完毕，5-活动已结束
     * @return status 1-活动即将开始,2-抢购中,3-计息中,4-发放完毕，5-活动已结束
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1-活动即将开始,2-抢购中,3-计息中,4-发放完毕，5-活动已结束
     * @param status 1-活动即将开始,2-抢购中,3-计息中,4-发放完毕，5-活动已结束
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 参与人数
     * @return join_num 参与人数
     */
    public Integer getJoinNum() {
        return joinNum;
    }

    /**
     * 参与人数
     * @param joinNum 参与人数
     */
    public void setJoinNum(Integer joinNum) {
        this.joinNum = joinNum;
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
     * 总金额
     * @return money 总金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 总金额
     * @param money 总金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 锁仓上限
     * @return limit_max 锁仓上限
     */
    public BigDecimal getLimitMax() {
        return limitMax;
    }

    /**
     * 锁仓上限
     * @param limitMax 锁仓上限
     */
    public void setLimitMax(BigDecimal limitMax) {
        this.limitMax = limitMax;
    }

    /**
     * 单笔起购数量
     * @return limit_min 单笔起购数量
     */
    public BigDecimal getLimitMin() {
        return limitMin;
    }

    /**
     * 单笔起购数量
     * @param limitMin 单笔起购数量
     */
    public void setLimitMin(BigDecimal limitMin) {
        this.limitMin = limitMin;
    }

    /**
     * 是否推荐项目,1:是,0:否
     * @return recommend 是否推荐项目,1:是,0:否
     */
    public Byte getRecommend() {
        return recommend;
    }

    /**
     * 是否推荐项目,1:是,0:否
     * @param recommend 是否推荐项目,1:是,0:否
     */
    public void setRecommend(Byte recommend) {
        this.recommend = recommend;
    }

    /**
     * 禁用与开启,1:开启,禁用:0
     * @return display_open 禁用与开启,1:开启,禁用:0
     */
    public Byte getDisplayOpen() {
        return displayOpen;
    }

    /**
     * 禁用与开启,1:开启,禁用:0
     * @param displayOpen 禁用与开启,1:开启,禁用:0
     */
    public void setDisplayOpen(Byte displayOpen) {
        this.displayOpen = displayOpen;
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
     * 开始时间
     * @return start_time 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 开始时间
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 结束时间
     * @return end_time 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 结束时间
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 计息开始时间
     * @return interstart_time 计息开始时间
     */
    public Date getInterstartTime() {
        return interstartTime;
    }

    /**
     * 计息开始时间
     * @param interstartTime 计息开始时间
     */
    public void setInterstartTime(Date interstartTime) {
        this.interstartTime = interstartTime;
    }

    /**
     * 计息结束时间
     * @return interend_time 计息结束时间
     */
    public Date getInterendTime() {
        return interendTime;
    }

    /**
     * 计息结束时间
     * @param interendTime 计息结束时间
     */
    public void setInterendTime(Date interendTime) {
        this.interendTime = interendTime;
    }

    /**
     * 余利宝活动类型(1 静态、 2 动态)
     * @return type 余利宝活动类型(1 静态、 2 动态)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 余利宝活动类型(1 静态、 2 动态)
     * @param type 余利宝活动类型(1 静态、 2 动态)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 满多少可提现
     * @return deposit_money 满多少可提现
     */
    public BigDecimal getDepositMoney() {
        return depositMoney;
    }

    /**
     * 满多少可提现
     * @param depositMoney 满多少可提现
     */
    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }
}