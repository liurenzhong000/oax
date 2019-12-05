package com.oax.entity.front;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;

/**
 * market
 *
 * @author
 */
public class Market implements Serializable {
    private Integer id;

    /**
     * 市场分区id
     */
    @NotNull(message = "市场分区id不能为空")
    private Integer marketCategoryId;

    /**
     * 目标币种id
     */
    @NotNull(message = "目标币种id不能为空")
    private Integer coinId;

    /**
     * 单价精度
     */
    @NotNull(message = "单价精度不能为空")
    private Integer priceDecimals;

    /**
     * 数量精度
     */
    @NotNull(message = "数量进度不能为空")
    private Integer qtyDecimals;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否展示 0不展示 1展示 默认不展示
     */
    private Byte isShow;

    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 是否自动下单
     */
    private Byte isAutoAdd;

    /**
     * 是否自动下单
     */
    private Byte isMine;

    /**
     * 市场币种id
     */
    @Transient
    private Integer categoryCoinId;


    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMarketCategoryId() {
        return marketCategoryId;
    }

    public void setMarketCategoryId(Integer marketCategoryId) {
        this.marketCategoryId = marketCategoryId;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public Integer getPriceDecimals() {
        return priceDecimals;
    }

    public void setPriceDecimals(Integer priceDecimals) {
        this.priceDecimals = priceDecimals;
    }

    public Integer getQtyDecimals() {
        return qtyDecimals;
    }

    public void setQtyDecimals(Integer qtyDecimals) {
        this.qtyDecimals = qtyDecimals;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getIsShow() {
        return isShow;
    }

    public void setIsShow(Byte isShow) {
        this.isShow = isShow;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getCategoryCoinId() {
        return categoryCoinId;
    }

    public void setCategoryCoinId(Integer categoryCoinId) {
        this.categoryCoinId = categoryCoinId;
    }

    public Byte getIsAutoAdd() {
        return isAutoAdd;
    }

    public void setIsAutoAdd(Byte isAutoAdd) {
        this.isAutoAdd = isAutoAdd;
    }

    public Byte getIsMine() {
        return isMine;
    }

    public void setIsMine(Byte isMine) {
        this.isMine = isMine;
    }
}