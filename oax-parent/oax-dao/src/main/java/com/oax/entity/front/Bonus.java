package com.oax.entity.front;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.io.Serializable;
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
@Entity
public class Bonus implements Serializable {


    /**
     * 分红收益id
     */
    @Id
    @GeneratedValue
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分红甲方
     */
    private Long fromUserId;

    /**
     * 分红乙方
     */
    private Long toUserId;

    /**
     * 分红时的持仓量
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
     * 左币id
     */
    private Long leftCoinId;

    /**
     * 右币id
     */
    private Long rightCoinId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**我的平均持仓*/
    private BigDecimal myAverageThreshold;

    /**下级的平均持仓*/
    private BigDecimal levelAverageThreshold;


}
