package com.oax.vo;

public class CoinLockConfigVO {
    private Integer id;
    private Integer coinId;

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

    @Override
    public String toString() {
        return "CoinLockConfigVO{" +
                "id=" + id +
                ", coinId=" + coinId +
                '}';
    }
}
