package com.oax.entity.front;

import java.math.BigDecimal;
import java.util.Date;

public class PromoteFund {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 币种名称
     */
    private Integer marketId;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 1-冻结，2-已释放
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 币种名称
     * @return market_id 币种名称
     */
    public Integer getMarketId() {
        return marketId;
    }

    /**
     * 币种名称
     * @param marketId 币种名称
     */
    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    /**
     * 金额
     * @return money 金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 金额
     * @param money 金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 1-冻结，2-已释放
     * @return status 1-冻结，2-已释放
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1-冻结，2-已释放
     * @param status 1-冻结，2-已释放
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     * @return update_time 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}