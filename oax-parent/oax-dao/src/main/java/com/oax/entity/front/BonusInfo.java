package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 分红可用余额
 */
@Data
@Entity
public class BonusInfo {

    @Id
    @GeneratedValue
    @TableId(value = "user_id", type = IdType.INPUT)
    private Integer userId;

    /**分红可用余额*/
    private BigDecimal banlace;

    /**开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


}
