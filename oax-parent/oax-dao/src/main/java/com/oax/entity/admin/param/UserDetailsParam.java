package com.oax.entity.admin.param;

public class UserDetailsParam extends PageParam {
    //用户id
    private Integer userId;
    //币的id 搜索
    private Integer coinId;

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
}
