package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Robot implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 交易对id
     */
    private Integer marketId;

    /**
     * 交易对名称
     */
    private String marketName;

    /**
     * 价格下限
     */
    private BigDecimal startPrice;

    /**
     * 价格上限
     */
    private BigDecimal endPrice;

    /**
     * 买单数量下限
     */
    private BigDecimal buyStartNumber;

    /**
     * 买单数量上限
     */
    private BigDecimal buyEndNumber;

    /**
     * 卖单数量下限
     */
    private BigDecimal sellStartNumber;

    /**
     * 卖单数量上限
     */
    private BigDecimal sellEndNumber;

    /**
     * 价格精度
     */
    private BigDecimal accuracy;

    /**
     * 状态：0：关;1开
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * apikey
     */
    private String apiKey;

    /**
     * secret
     */
    private String apiSecret;

    /**
     * 固定休秒数
     */
    private Integer fixSleep;

    /**
     * 动态休眠秒数
     */
    private Integer dynamicSleep;

}