package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.oax.entity.enums.UserCoinDetailType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用戶币种资金变更记录表
 */
@Data
@Entity
public class UserCoinDetail {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 币种id
     */
    private Integer coinId;

    /**对应业务表的id（eg：ctc订单id，撮合记录id等）*/
    private String targetId;

    /**
     * 变更前余额
     */
    private BigDecimal beforeBalance;

    /**
     * 变更后余额
     */
    private BigDecimal afterBalance;

    /**
     * 变更前冻结余额
     */
    private BigDecimal beforeFreezing;

    /**
     * 变更后冻结余额
     */
    private BigDecimal afterFreezing;

    /**余额变化量*/
    private BigDecimal changeBalance;

    /**冻结余额变化量*/
    private BigDecimal changeFreezing;

    /**
     * 创建时间
     */
    private Date createTime;

    /**变化类型*/
    private UserCoinDetailType type;

}
