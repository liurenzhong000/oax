package com.oax.entity.ctc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.oax.entity.enums.CtcOrderStatus;
import com.oax.entity.enums.CtcOrderType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CTC 交易订单
 */
@Setter
@Getter
@Entity
public class CtcOrder implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 卖出方
     */
    private Integer fromUserId;

    /**
     * 买入方
     */
    private Integer toUserId;

    /**
     * 交易币种类型
     */
    private Integer coinId;

    /**
     * 交易时的币种单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 交易总价
     */
    private BigDecimal totalCost;

    /**
     * 交易时间
     */
    private Date createDate;

    /**
     * 交易完成时间
     */
    private Date finishDate;

    /**
     *  NO_PAY("未付款"), PAYED("已付款"), APPEAL("申诉中"), PROCESSING("处理中"), FINISH("交易完成"), CANCEL("已取消")
     */
    private CtcOrderStatus status;

    /**
     * 类型： 0 买入 1 卖出
     */
    private CtcOrderType type;

    private Integer ctcAdvertId;

    //=============冗余下单时收人民币方的银行卡信息========
    /**开户用户名称*/
    private String realName;

    /**开户银行名称*/
    private String bankName;

    /**开户省市*/
    private String city;

    /**开户支行*/
    private String bankBranch;

    /**银行卡号*/
    private String cardNo;

}
