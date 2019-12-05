package com.oax.entity.admin.param;

public class profitParam {
    /**
     * 第几页
     */
    private Integer pageNum;

    /**
     * 页面容量
     */
    private  Integer pageSize;

    /**
     * 用户Id
     */
    private  Integer userId;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
