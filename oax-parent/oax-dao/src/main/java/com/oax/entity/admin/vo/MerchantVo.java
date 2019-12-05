package com.oax.entity.admin.vo;

import com.oax.entity.enums.PaymentWay;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class MerchantVo {

    /**商家id*/
    private Integer id;

    private Integer userId;

    /**用户名*/
    private String userName;

    /**商家姓名*/
    private String idName;

    /**
     * 卖出个数
     */
    private BigDecimal saleQty;

    /**卖出金额*/
    private BigDecimal saleFee;

    /**
     * 卖出成交单数
     */
    private int saleSuccCount;

    /**
     * 生成卖出订单总数
     */
    private int saleTotalCount;

    /**
     * 买入个数
     */
    private BigDecimal buyQty;

    /**买入金额*/
    private BigDecimal buyFee;

    /**
     * 买入成交单数
     */
    private int buySuccCount;

    /**
     * 生成买入订单总数
     */
    private int buyTotalCount;

    /**
     * 商家入驻时间
     */
    private Date createDate;

    /**
     * 付款方式，当前只有0：银行卡
     */
    private PaymentWay paymentWay;

    /**
     * 付款方式描述
     */
    private String paymentWayDesc;

    /**
     * 卖出成交率
     */
    private BigDecimal saleRatio;

    /**
     * 买入成交率
     */
    private BigDecimal buyRatio;

    /**
     * 资金是否冻结
     */
    private boolean freezing;

}
