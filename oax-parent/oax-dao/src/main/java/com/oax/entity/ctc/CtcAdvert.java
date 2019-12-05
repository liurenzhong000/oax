package com.oax.entity.ctc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.oax.entity.enums.CtcAdvertStatus;
import com.oax.entity.enums.CtcAdvertType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CTC 广告
 * 不可做直接删除
 */
@Setter
@Getter
@Entity
public class CtcAdvert implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**商户id*/
    private Integer merchantId;

    private Integer userId;

    /**
     * 交易币种类型
     */
    private Integer coinId;

    /**
     * 交易币种类型名称
     */
    private String coinName;

    /**
     * 最小下单个数（最小买入数量/ 最小卖出数量）
     */
    private BigDecimal minQty;

    /**
     * 最大下单个数（最大买入数量/ 最大卖出数量）
     */
    private BigDecimal maxQty;

    /**
     * 留言信息
     */
    private String leaveMessage;

    /**
     * 人民币单价
     */
    private BigDecimal cnyPrice;

    /**
     * 总交易数量
     */
    private BigDecimal totalQty;

    /**
     * 剩余可交易数量
     */
    private BigDecimal remainQty;

    private CtcAdvertType type;

    /**
     * 广告状态（下架、上架）
     */
    private CtcAdvertStatus status;

    private Date createDate;

}
