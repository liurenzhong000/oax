package com.oax.vo;

import java.util.Date;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LockPositionRecordVO{

    private Integer coinId;

    private Integer userId;

    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public Integer getPageNo() {
        return pageNo;
    }
    public void setPageNo(Integer pageNo) {
        if(pageNo==null) pageNo = 1;
        this.pageNo = pageNo;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        if (pageSize==null) pageSize = 10;
        this.pageSize = pageSize;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LockPositionRecordVO{" +
                "coinId=" + coinId +
                ", userId=" + userId +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }
}
