package com.oax.entity.front.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 14:17
 * @Description:
 */
@Data
public class DiceActivityVo {

    /**下注币种*/
    private Integer coinId;

    /**币种名称*/
    private String coinName;

    /**从eos中获取的hash*/
    private String eosHash;

    /**投注个数*/
    private BigDecimal betQty;

    /**赚取个数*/
    private BigDecimal payoutQty;

    /**小于这个数中奖*/
    private Integer rollUnder;

    /**开奖数字*/
    private Integer diceNumber;

    /**是否中奖*/
    private Boolean betWin;

    private Date createTime;

    /**账户余额*/
    private BigDecimal balance;
}
