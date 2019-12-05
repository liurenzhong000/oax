package com.oax.entity.front;

import java.math.BigDecimal;
import java.util.Date;

public class MovesayOperateLog {
    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private String orderId;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 币种名称
     */
    private Integer marketId;

    /**
     * (1-复投  2-提现  3-抢购，4-每日释放，5-到期释放)
     */
    private Integer type;

    /**
     * 操作金额
     */
    private BigDecimal money;

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
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 
     * @return order_id 
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId 
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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
     * (1-复投  2-提现  3-抢购，4-每日释放，5-到期释放)
     * @return type (1-复投  2-提现  3-抢购，4-每日释放，5-到期释放)
     */
    public Integer getType() {
        return type;
    }

    /**
     * (1-复投  2-提现  3-抢购，4-每日释放，5-到期释放)
     * @param type (1-复投  2-提现  3-抢购，4-每日释放，5-到期释放)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 操作金额
     * @return money 操作金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 操作金额
     * @param money 操作金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
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