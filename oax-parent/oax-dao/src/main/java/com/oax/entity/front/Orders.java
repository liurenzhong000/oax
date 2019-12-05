package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * orders
 *
 * @author
 */
public class Orders implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer marketId;

    /**
     * 类型： 1 买入 2 卖出
     */
    private Integer type;

    /**
     * 委托价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 已成交数量
     */
    private BigDecimal tradeQty;

    /**
     * 状态-1已撤单 0待撮合 1撮合中 2已完成撮合
     */
    private Integer status;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 交易密码
     */
    private String password;

    private Integer leftCoinId;

    private Integer rightCoinId;
    
    private String leftCoinName;
    private String rightCoinName;
    private Integer rank;
    private static final long serialVersionUID = 1L;
     
	public String getLeftCoinName() {
		return leftCoinName;
	}

	public void setLeftCoinName(String leftCoinName) {
		this.leftCoinName = leftCoinName;
	}

	public String getRightCoinName() {
		return rightCoinName;
	}

	public void setRightCoinName(String rightCoinName) {
		this.rightCoinName = rightCoinName;
	}

	public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public BigDecimal getTradeQty() {
        return tradeQty;
    }

    public void setTradeQty(BigDecimal tradeQty) {
        this.tradeQty = tradeQty;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Integer getLeftCoinId() {
        return leftCoinId;
    }

    public void setLeftCoinId(Integer leftCoinId) {
        this.leftCoinId = leftCoinId;
    }

    public Integer getRightCoinId() {
        return rightCoinId;
    }

    public void setRightCoinId(Integer rightCoinId) {
        this.rightCoinId = rightCoinId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", userId=" + userId +
                ", marketId=" + marketId +
                ", type=" + type +
                ", price=" + price +
                ", qty=" + qty +
                ", tradeQty=" + tradeQty +
                ", status=" + status +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", password='" + password + '\'' +
                ", leftCoinId=" + leftCoinId +
                ", rightCoinId=" + rightCoinId +
                ", leftCoinName='" + leftCoinName + '\'' +
                ", rightCoinName='" + rightCoinName + '\'' +
                ", rank=" + rank +
                '}';
    }
}