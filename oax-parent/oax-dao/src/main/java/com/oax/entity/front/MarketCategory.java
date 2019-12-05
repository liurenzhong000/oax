package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;

/**
 * market_category
 *
 * @author
 */
public class MarketCategory implements Serializable {
    private Integer id;

    /**
     * 市场分类对应的coin_id
     */
    @NotNull(message = "市场分类对应的币种id不能为空")
    private Integer coinId;

    /**
     * 人民币汇率
     */
    @NotNull(message = "人民币汇率不能为空")
    private BigDecimal cnyPrice;

    /**
     * 美元汇率
     */
    @NotNull(message = "美元汇率不能为空")
    private BigDecimal usdtPrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sortNum;

    /**
     * 是否可用 0不可用 1可用
     */
    private Byte isUse;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 市场名 列:BTC
     */
    @Transient
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoinId() {
        return coinId;
    }


    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(BigDecimal cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public BigDecimal getUsdtPrice() {
        return usdtPrice;
    }

    public void setUsdtPrice(BigDecimal usdtPrice) {
        this.usdtPrice = usdtPrice;
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

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Byte getIsUse() {
        return isUse;
    }

    public void setIsUse(Byte isUse) {
        this.isUse = isUse;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}