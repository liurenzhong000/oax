/**
 * 
 */
package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName:：MarketTradeCoin @Description： 货币交易对信息
 * @author ：xiangwh
 * @date ：2018年6月4日 下午3:07:33
 * 
 */
public class MarketPriceInfo implements Serializable {

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
		return "MarketTradeCoin [coinId=" + coinId + ", coinName=" + coinName + ", marketCoinId=" + marketCoinId
				+ ", marketCoinName=" + marketCoinName + ", tradeQty=" + tradeQty + ", maxPrice=" + maxPrice
				+ ", minPrice=" + minPrice + ", incRate=" + incRate + ", lastTradePrice=" + lastTradePrice
				+ ", marketId=" + marketId + ", totalAmount=" + totalAmount + ", priceDecimals=" + priceDecimals
				+ ", qtyDecimals=" + qtyDecimals + ", cnyPrice=" + cnyPrice + "]";
	}

	
	
	
}
