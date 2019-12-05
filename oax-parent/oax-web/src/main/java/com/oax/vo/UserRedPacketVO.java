package com.oax.vo;

import javax.validation.constraints.Min;

public class UserRedPacketVO {

    private Integer userId;
    private Integer id;
    //分页参数
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserRedPacketVO{" +
                "userId=" + userId +
                ", id=" + id +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}
