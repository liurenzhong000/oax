package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * BHB抢购活动 用户成功购买详情
 */
@Setter
@Getter
@Entity
public class PanicBuyOrder implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**用户id*/
    private Integer userId;

    /**活动id */
    private Integer activityId;

    /**购买个数*/
    private BigDecimal qty;

    /**总usdt个数*/
    private BigDecimal allUsdtQty;

    /**购买价格*/
    private BigDecimal buyPrice;

    /**当时的BHB/USDT 价格（点击立即购买时新增或更新）*/
    private BigDecimal currPrice;

    /**实际支付usdt个数*/
    private BigDecimal realUsdtQty;

    private Date createTime;

    /**支付时间*/
    private Date finishTime;

    /**
     * 订单状态
     * @see Status
     * */
    private Integer status;

    public enum Status{
        NO_PAY("待支付"), FINISH("完成"), CLOSE("失效");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }

}
