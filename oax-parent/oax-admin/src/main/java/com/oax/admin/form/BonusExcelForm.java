package com.oax.admin.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 10:56
 * @Description:
 */
@Data
public class BonusExcelForm {

    /**达标人数*/
    private Integer reachCount;

    /**bhb / usdt 比例*/
    private BigDecimal bhbUsdtRatio;

    /**总分红量*/
    private BigDecimal allBonus;

    /**分红操作开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**分红操作结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
