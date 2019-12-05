package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class BonusLog {
    /**
     * 分红收益id
     */
    @Id
    @GeneratedValue
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;

    /**
     * 当前持币个数
     */
    private BigDecimal currQty;

    /**
     * 分红总收益
     */
    private BigDecimal bonus;

    private BigDecimal myBonus;

    private BigDecimal oneBonus;

    private Integer oneCount;

    private BigDecimal twoBonus;

    private Integer twoCount;

    private BigDecimal threeBonus;

    private Integer threeCount;

    private String oneUserIds;

    private String twoUserIds;

    private String threeUserIds;

    /**
     * 创建时间
     */
    private Date createTime;
}
