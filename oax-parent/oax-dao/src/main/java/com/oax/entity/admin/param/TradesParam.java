package com.oax.entity.admin.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TradesParam extends PageParam {
    //用户id
    private Integer userId;
    //根据币的id 查询
    private Integer coinId;
    //交易开始时间 : 高级查询
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date beginTime;
    //交易结束时间 : 高级查询
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date endTime;
    //是买还是买 1代表买入  2代表卖出
    private Integer buyOrSell;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(Integer buyOrSell) {
        this.buyOrSell = buyOrSell;
    }
}
