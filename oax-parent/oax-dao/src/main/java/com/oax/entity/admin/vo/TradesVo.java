package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TradesVo {
    //交易对
    private String dealTroops;
    //成交价 单价
    private BigDecimal price;
    //成交数量 数量
    private BigDecimal qty;
    //总额(交易主币) 拼接
    private String main;
    //手续费与币名称 拼接
    private String fee;
    //创建时间 交易时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //是买还是买 1代表买入  2代表卖出
    private Integer buyOrSell;
    //市场分区 币名称
    private String bazaarCoin;
    //目标币种 币名称
    private String targetCoin;

    public String getDealTroops() {
        return dealTroops;
    }

    public void setDealTroops(String dealTroops) {
        this.dealTroops = dealTroops;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(Integer buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public String getBazaarCoin() {
        return bazaarCoin;
    }

    public void setBazaarCoin(String bazaarCoin) {
        this.bazaarCoin = bazaarCoin;
    }

    public String getTargetCoin() {
        return targetCoin;
    }

    public void setTargetCoin(String targetCoin) {
        this.targetCoin = targetCoin;
    }
}
