package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/11
 * Time: 18:32
 * <p>
 * 委托页面 展示vo
 */
@Data
public class OrdersPageVo {

    /**
     * 委托id
     */
    private int id;

    private int userId;

    /**
     * 用户手机
     */
    private String phone;

    /**
     * 用户Email
     */
    private String email;

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
     * 委托价 单价
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
     * 总价 qty*tradeQty
     */
    private BigDecimal totalPrices;

//    public BigDecimal getTotalPrices() {
//        if (price != null && qty != null)
//            return price.multiply(qty);
//        return totalPrices;
//    }

    /**
     * 类型： 1 买入 2 卖出
     */
    private int type;

    /**
     * 状态-1已撤单 0待撮合 1撮合中 2已完成撮合
     */
    private int status;

    /**
     * 创建时间
     */
    private Date createTime;


}
