package com.oax.entity.admin.param;

import java.util.Date;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShareBonusParam {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

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
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1;
    private Integer pageSize = 10;

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

    @Override
    public String toString() {
        return "ShareBonusParam{" +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}
