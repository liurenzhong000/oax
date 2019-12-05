package com.oax.entity.admin.vo;

import javax.validation.constraints.Min;

public class CoinLockConfigVo {
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
}
