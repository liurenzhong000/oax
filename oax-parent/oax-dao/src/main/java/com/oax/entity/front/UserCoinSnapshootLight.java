package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户余额快照
 * 用户BHB余额快照，只记录有进行充值和法币交易的用户
 */
@Data
@Entity
public class UserCoinSnapshootLight {

    @Id
    @GeneratedValue
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**用户id*/
    private Integer userId;

    /**币种id*/
    private Integer coinId;

    /**开始时间*/
    private Date createTime;

    /**结束时间*/
    private BigDecimal balance;

    /**冻结金额*/
    private BigDecimal freezingBalance;

    /**用户统计的金额*/
    private BigDecimal bonusBalance;

}
