package com.oax.entity.front;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * user_coin
 *
 * @author
 */
public class UserCoinInfo {
    private Integer id;

    private Integer userId;


    /**
     * 可用余额
     */
    private BigDecimal banlance;

    /**
     * 冻结余额
     */
    private BigDecimal freezingBanlance;
    /**
     * 总余额
     */
    private BigDecimal totalBanlance;
    private String shortName;
    private String fullName;
    private String image;
    private BigDecimal cnyPrice;
    private BigDecimal usdtPrice;
    private BigDecimal btcPrice;
    private BigDecimal withdrawFee;
    private Integer allowRecharge;
    private Integer allowWithdraw;
    private Integer type;
    private BigDecimal minOutQty;
    private BigDecimal maxOutQty;
    private BigDecimal useredWithdrawal;
    private BigDecimal withdrawalAmount;
    private List<Map<String, Object>> tradeList;
    public UserCoinInfo() {}
    public UserCoinInfo(Integer userId, BigDecimal banlance, BigDecimal freezingBanlance,
			BigDecimal totalBanlance,BigDecimal cnyPrice, BigDecimal usdtPrice,BigDecimal btcPrice,
			BigDecimal withdrawFee) {
		super();
		this.userId = userId;
		this.banlance = banlance;
		this.freezingBanlance = freezingBanlance;
		this.totalBanlance = totalBanlance;
		this.cnyPrice = cnyPrice;
		this.usdtPrice = usdtPrice;
		this.withdrawFee = withdrawFee;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public BigDecimal getTotalBanlance() {
        return totalBanlance;
    }

    public void setTotalBanlance(BigDecimal totalBanlance) {
        this.totalBanlance = totalBanlance;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(BigDecimal cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

	public BigDecimal getUsdtPrice() {
		return usdtPrice;
	}

	public void setUsdtPrice(BigDecimal usdtPrice) {
		this.usdtPrice = usdtPrice;
	}

	public BigDecimal getWithdrawFee() {
		return withdrawFee;
	}

	public void setWithdrawFee(BigDecimal withdrawFee) {
		this.withdrawFee = withdrawFee;
	}

	public Integer getAllowRecharge() {
		return allowRecharge;
	}

	public void setAllowRecharge(Integer allowRecharge) {
		this.allowRecharge = allowRecharge;
	}

	public Integer getAllowWithdraw() {
		return allowWithdraw;
	}

	public void setAllowWithdraw(Integer allowWithdraw) {
		this.allowWithdraw = allowWithdraw;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<Map<String, Object>> getTradeList() {
		return tradeList;
	}
	public void setTradeList(List<Map<String, Object>> tradeList) {
		this.tradeList = tradeList;
	}
	public BigDecimal getMinOutQty() {
		return minOutQty;
	}
	public void setMinOutQty(BigDecimal minOutQty) {
		this.minOutQty = minOutQty;
	}
	public BigDecimal getMaxOutQty() {
		return maxOutQty;
	}
	public void setMaxOutQty(BigDecimal maxOutQty) {
		this.maxOutQty = maxOutQty;
	}
	public BigDecimal getUseredWithdrawal() {
		return useredWithdrawal;
	}
	public void setUseredWithdrawal(BigDecimal useredWithdrawal) {
		this.useredWithdrawal = useredWithdrawal;
	}
	public BigDecimal getWithdrawalAmount() {
		return withdrawalAmount;
	}
	public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

    public BigDecimal getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(BigDecimal btcPrice) {
        this.btcPrice = btcPrice;
    }
}