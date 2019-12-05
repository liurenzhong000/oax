package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/23
 * Time: 17:09
 * 交易手续费展示
 */
@Data
public class TradeFeeVo {

    private Integer coinId;

    private String coinName;
    /**
     * 买入手续费
     */
    private BigDecimal buyerFee;

    /**
     * 卖出手续费
     */
    private BigDecimal sellFee;

    /**
     * 总手续费
     */
    private BigDecimal countFee;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
