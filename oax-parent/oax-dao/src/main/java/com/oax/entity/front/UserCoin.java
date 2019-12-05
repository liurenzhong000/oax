package com.oax.entity.front;

import java.math.BigDecimal;
import java.util.Date;

/**
 * user_coin
 *
 * @author
 */
public class UserCoin {
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 币种id
     */
    private Integer coinId;


    /**
     * 可用余额
     */
    private BigDecimal banlance;

    /**
     * 冻结余额
     */
    private BigDecimal freezingBanlance;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 总转入
     */
    private BigDecimal sumShiftTo;
    /**
     * 总转出
     */
    private BigDecimal sumShiftOut;
    /**
     * 总转出手续费
     */
    private BigDecimal sumFee;
    /**
     * 总交易手续费
     */
    private BigDecimal sumDealFee;
    /**
     * 币种搜索 : 模糊查询
     */
    private String coinSeek;

    /**
     * 乐观锁
     */
    private Integer version;

    public static UserCoin newInstance(Integer userId, Integer coinId){
        UserCoin userCoin = new UserCoin();
        userCoin.setUserId(userId);
        userCoin.setCoinId(coinId);
        return userCoin;
    }
    public BigDecimal getSumShiftTo() {
        return sumShiftTo;
    }

    public void setSumShiftTo(BigDecimal sumShiftTo) {
        this.sumShiftTo = sumShiftTo;
    }

    public BigDecimal getSumShiftOut() {
        return sumShiftOut;
    }

    public void setSumShiftOut(BigDecimal sumShiftOut) {
        this.sumShiftOut = sumShiftOut;
    }

    public BigDecimal getSumFee() {
        return sumFee;
    }

    public void setSumFee(BigDecimal sumFee) {
        this.sumFee = sumFee;
    }

    public BigDecimal getSumDealFee() {
        return sumDealFee;
    }

    public void setSumDealFee(BigDecimal sumDealFee) {
        this.sumDealFee = sumDealFee;
    }

    public String getCoinSeek() {
        return coinSeek;
    }

    public void setCoinSeek(String coinSeek) {
        if (coinSeek != null && coinSeek.trim().length() > 0) {
            this.coinSeek = coinSeek;
        }
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public BigDecimal getBanlance() {
        return banlance;
    }

    public void setBanlance(BigDecimal banlance) {
        this.banlance = banlance;
    }

    public BigDecimal getFreezingBanlance() {
        return freezingBanlance;
    }

    public void setFreezingBanlance(BigDecimal freezingBanlance) {
        this.freezingBanlance = freezingBanlance;
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

    @Override
    public String toString() {
        return "UserCoin{" +
                "id=" + id +
                ", userId=" + userId +
                ", coinId=" + coinId +
                ", banlance=" + banlance +
                ", freezingBanlance=" + freezingBanlance +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", sumShiftTo=" + sumShiftTo +
                ", sumShiftOut=" + sumShiftOut +
                ", sumFee=" + sumFee +
                ", sumDealFee=" + sumDealFee +
                ", coinSeek='" + coinSeek + '\'' +
                '}';
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}