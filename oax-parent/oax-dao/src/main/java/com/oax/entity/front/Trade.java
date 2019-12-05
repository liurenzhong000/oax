package com.oax.entity.front;

import java.math.BigDecimal;

/**
 * trade
 *
 * @author
 */
public class Trade {
    /**
     * 默认正常状态
     */
    public static final int DEAL_TYPE_NORMAL = 0;
    /**
     * 买入状态
     */
    public static final int DEAL_TYPE_BUY = 1;
    /**
     * 卖出状态
     */
    public static final int DEAL_TYPE_sale = 2;


    private Integer id;

    /**
     * 原始委托单id
     */
    private Integer orderId;

    /**
     * 撮合类型： 1 主动撮合 2被撮合
     */
    private Integer type;

    /**
     * 撮合方委托单id
     */
    private Integer matchOrderId;

    /**
     * 成交价
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private BigDecimal qty;

    /**
     * 创建时间
     */
    private String createTime;

    private String updateTime;
    /**
     * 是买还是买 1代表买入  2代表卖出
     */
    private Integer buyOrSell;
    /**
     * 市场交易对
     */
    private Integer marketId;
    /**
     * 用户id
     */
    private Integer userId;

    private String dealTroops;
    /**
     * 总额(交易主币) 拼接
     */
    private String main;

    private Integer feeCoinId;
    /**
     * 手续费与币名称 拼接
     */
    private BigDecimal fee;
    /**
     * 类型： 1 买入 2 卖出 默认0
     */
    private int dealType = DEAL_TYPE_NORMAL;
    
    private Integer orderType;
    private Integer leftCoinId;
    private String leftCoinName;
    private Integer rightCoinId;
    private String rightCoinName;
    
    
    public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getLeftCoinId() {
		return leftCoinId;
	}

	public void setLeftCoinId(Integer leftCoinId) {
		this.leftCoinId = leftCoinId;
	}

	public String getLeftCoinName() {
		return leftCoinName;
	}

	public void setLeftCoinName(String leftCoinName) {
		this.leftCoinName = leftCoinName;
	}

	public Integer getRightCoinId() {
		return rightCoinId;
	}

	public void setRightCoinId(Integer rightCoinId) {
		this.rightCoinId = rightCoinId;
	}

	public String getRightCoinName() {
		return rightCoinName;
	}

	public void setRightCoinName(String rightCoinName) {
		this.rightCoinName = rightCoinName;
	}

	public String getDealTroops() {
        return dealTroops;
    }

    public void setDealTroops(String dealTroops) {
        this.dealTroops = dealTroops;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public int getDealType() {
        return dealType;
    }

    public void setDealType(int dealType) {
        this.dealType = dealType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private static final long serialVersionUID = 1L;


    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public Integer getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(Integer buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMatchOrderId() {
        return matchOrderId;
    }

    public void setMatchOrderId(Integer matchOrderId) {
        this.matchOrderId = matchOrderId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Trade() {
        super();
    }

    public Integer getFeeCoinId() {
        return feeCoinId;
    }

    public void setFeeCoinId(Integer feeCoinId) {
        this.feeCoinId = feeCoinId;
    }

    public Trade(Integer orderId, Integer marketId, Integer type, Integer matchOrderId, BigDecimal price, BigDecimal qty, Integer feeCoinId,
                 BigDecimal fee, Integer orderType, Integer leftCoinId, String leftCoinName, Integer rightCoinId, String rightCoinName, Integer userId) {
        super();
        this.orderId = orderId;
        this.type = type;
        this.matchOrderId = matchOrderId;
        this.price = price;
        this.qty = qty;
        this.feeCoinId = feeCoinId;
        this.fee = fee;
        this.marketId = marketId;
        this.orderType = orderType;
        this.leftCoinId = leftCoinId;
        this.leftCoinName = leftCoinName;
        this.rightCoinId = rightCoinId;
        this.rightCoinName = rightCoinName;
        this.userId = userId;
    }

	@Override
	public String toString() {
		return "Trade [id=" + id + ", orderId=" + orderId + ", type=" + type + ", matchOrderId=" + matchOrderId
				+ ", price=" + price + ", qty=" + qty + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", buyOrSell=" + buyOrSell + ", marketId=" + marketId + ", userId=" + userId + ", dealTroops="
				+ dealTroops + ", main=" + main + ", feeCoinId=" + feeCoinId + ", fee=" + fee + ", dealType=" + dealType
				+ ", orderType=" + orderType + ", leftCoinId=" + leftCoinId + ", leftCoinName=" + leftCoinName
				+ ", rightCoinId=" + rightCoinId + ", rightCoinName=" + rightCoinName + "]";
	}


}