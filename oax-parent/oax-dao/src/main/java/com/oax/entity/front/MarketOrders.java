package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ：xiangwh
 * @ClassName:：MarketOrders
 * @Description： 交易对订单
 * @date ：2018年6月15日 上午9:23:01
 */
public class MarketOrders implements Serializable {

    private static final long serialVersionUID = 7275378044262742519L;

    private BigDecimal price;
    private BigDecimal qty;
    private BigDecimal amount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MarketOrders [price=" + price + ", qty=" + qty + ", amount=" + amount + "]";
    }

}
