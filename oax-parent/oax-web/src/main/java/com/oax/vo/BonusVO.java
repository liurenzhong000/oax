package com.oax.vo;

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
 * <p>
 *分红
 * </p>
 *
 * @author zl
 * @since 2018-11-26
 */
@Getter
@Setter
public class BonusVO implements Serializable {

    /**
     * 分红甲方
     */
    private Long fromUserId;

    /**
     * 持仓量
     */
    private BigDecimal thresholdNumber;

    /**
     * 平均持仓量
     */
    private BigDecimal averageThreshold;

    /**
     * 层级
     */
    private int hierarchy;

    /**
     * 分红收益
     */
    private BigDecimal bonus;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 操作时间
     */
    private Date createTime;


}
