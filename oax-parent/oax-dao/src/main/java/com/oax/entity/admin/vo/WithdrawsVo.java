package com.oax.entity.admin.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WithdrawsVo {
    //币名称 币种
    private String shortName;
    //转出数量
    private String qty;
    //手续费
    private String fee;
    //转出地址
    private String fromAddress;
    //转入地址
    private String toAddress;
    /**
     * 交易hash
     */
    private String txHashId;
    //转出时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTxHashId() {
        return txHashId;
    }

    public void setTxHashId(String txHashId) {
        this.txHashId = txHashId;
    }
}
