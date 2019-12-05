package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 20:06
 * 成交记录 页面展示 vo
 */
@Data
public class TradePageVo {

    /**
     * 成交id
     */
    private int id;


    /**
     * 买家
     */
    private int userId;

    /**
     * 订单id
     */
    private int orderId;

    /**
     * 关联撮合方订单ID
     */
    private int matchOrderId;

    /**
     * 市场对应 目标币种名称
     */
    private String marketCoin;

    /**
     * 分区对应 币种名称
     */
    private String marketCategoryCoin;

    /**
     * 市场名称 marketCoin/marketCategoryCoin
     */
    private String marketName;

    public String getMarketName() {
        if (StringUtils.isNoneEmpty(marketCategoryCoin) &&
                StringUtils.isNoneEmpty(marketCoin))
            return marketCoin + "/" + marketCategoryCoin;
        return marketName;
    }

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 总价 qty*price
     */
    private BigDecimal totalPrices;

//    public BigDecimal getTotalPrices() {
//        if (price != null && qty != null)
//            return price.multiply(qty);
//        return totalPrices;
//    }

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 委托单类型 类型： 1 买入 2 卖出
     */
    private Integer orderType;

    /**
     *  撮合方式（主动/被动）
     */
    private Integer tradeType;

    /**
     * 创建时间
     */
    private Date createTime;
}
