package com.oax.common;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/30
 * Time: 16:12
 */
public class PageResultResponse<T> {

    /**
     * 页数
     */
    private int pageNum;
    /**
     * 每页展示数
     */
    private int pageSize;
    /**
     * 总数
     */
    private long total;
    /**
     * 一共多少页
     */
    private int pages;
    /**
     * 数据集合
     */
    private List<T> list;
    /**
     * 前一页码
     */
    private int prePage;
    /**
     * 后一页码
     */
    private int nextPage;
    /**
     * 是否是第一页
     */
    private boolean isFirstPage;
    /**
     * 是否最后一页
     */
    private boolean isLastPage;
    /**
     * 是否存在上一页
     */
    private boolean hasPreviousPage;
    /**
     * 是否存在下一页
     */
    private boolean hasNextPage;

    /**
     * 页面参数
     */
    private Object param;

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
