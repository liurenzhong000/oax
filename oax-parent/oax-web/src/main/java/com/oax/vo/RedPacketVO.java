package com.oax.vo;

public class RedPacketVO {
    /**
     * 红包id
     */
    private Integer redPacketId;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 来源
     */
    private  String source;
    private  Integer coinId;

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public Integer getRedPacketId() {
        return redPacketId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSource() {
        return source;
    }

    public void setRedPacketId(Integer redPacketId) {
        this.redPacketId = redPacketId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "RedPacketVO{" +
                "redPacketId=" + redPacketId +
                ", accountNumber='" + accountNumber + '\'' +
                ", source='" + source + '\'' +
                ", coinId=" + coinId +
                '}';
    }
}
