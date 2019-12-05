package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ：xiangwh
 * @ClassName:：ActualTrade
 * @Description： 实时交易出参
 * @date ：2018年6月23日 下午2:29:03
 */
public class TradeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private BigDecimal qty;
    private BigDecimal price;
    private Integer type;
    private String createTime;

    public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	@Override
	public String toString() {
		return "TradeInfo [id=" + id + ", qty=" + qty + ", price=" + price + ", type=" + type + ", createTime="
				+ createTime + "]";
	}

    
}
