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
 */
@Data
@Entity
public class UserCoinSnapshoot {

    @Id
    @GeneratedValue
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;

    private Integer coinId;

    private Date createTime;

    private BigDecimal balance;

    private BigDecimal freezingBalance;

}
