/**
 *
 */
package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ：xiangwh
 * @ClassName:：MarketTradeCoin @Description： 货币交易对信息
 * @date ：2018年6月4日 下午3:07:33
 */
public class MarketInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    // 代币编号
    private Integer coinId;
    // 代币名称
    private String coinName;
    // 市场分类对应的coinId
    private Integer marketCoinId;
    // 市场分类对应的币名称
    private String marketCoinName;
    // 代币 24小时交易量
    private BigDecimal tradeQty;
    // 24小时最高点价格
    private BigDecimal maxPrice;
    // 24小时内最低点价格
    private BigDecimal minPrice;
    // 涨幅
    private BigDecimal incRate;
    // 最新价格(最近交易价格)
    private BigDecimal lastTradePrice;
    // 市场id (即交易对ID)
    private Integer marketId;
    // 24小时成交总额
    private BigDecimal totalAmount;
    // 交易对交易币的价格
    private Integer priceDecimals;
    // 交易对交易币的数量精度
    private Integer qtyDecimals;
    // 人名币汇率
    private BigDecimal cnyPrice;
    // 0表示未收藏  1表示已收藏
    private Integer isCollection = 0;
    // 左币的图标
    private String coinImage;

    private String fullCoinName;

    public String getFullCoinName() {
        return fullCoinName;
    }

    public void setFullCoinName(String fullCoinName) {
        this.fullCoinName = fullCoinName;
    }

    public String getCoinImage() {
		return coinImage;
	}

	public void setCoinImage(String coinImage) {
		this.coinImage = coinImage;
	}

	public Integer getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Integer isCollection) {
		this.isCollection = isCollection;
	}

	public BigDecimal getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(BigDecimal cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public Integer getPriceDecimals() {
        return priceDecimals;
    }

    public void setPriceDecimals(Integer priceDecimals) {
        this.priceDecimals = priceDecimals;
    }

    public Integer getQtyDecimals() {
        return qtyDecimals;
    }

    public void setQtyDecimals(Integer qtyDecimals) {
        this.qtyDecimals = qtyDecimals;
    }


    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public Integer getMarketCoinId() {
        return marketCoinId;
    }

    public void setMarketCoinId(Integer marketCoinId) {
        this.marketCoinId = marketCoinId;
    }

    public String getMarketCoinName() {
        return marketCoinName;
    }

    public void setMarketCoinName(String marketCoinName) {
        this.marketCoinName = marketCoinName;
    }


    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getIncRate() {
        return incRate;
    }

    public void setIncRate(BigDecimal incRate) {
        this.incRate = incRate;
    }

    public BigDecimal getTradeQty() {
        return tradeQty;
    }

    public void setTradeQty(BigDecimal tradeQty) {
        this.tradeQty = tradeQty;
    }

    public BigDecimal getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(BigDecimal lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "MarketInfo{" +
                "coinId=" + coinId +
                ", coinName='" + coinName + '\'' +
                ", marketCoinId=" + marketCoinId +
                ", marketCoinName='" + marketCoinName + '\'' +
                ", tradeQty=" + tradeQty +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", incRate=" + incRate +
                ", lastTradePrice=" + lastTradePrice +
                ", marketId=" + marketId +
                ", totalAmount=" + totalAmount +
                ", priceDecimals=" + priceDecimals +
                ", qtyDecimals=" + qtyDecimals +
                ", cnyPrice=" + cnyPrice +
                ", isCollection=" + isCollection +
                ", coinImage='" + coinImage + '\'' +
                ", fullCoinName='" + fullCoinName + '\'' +
                '}';
    }
}
