package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 09:15
 * @Description: DICE 投注猜大小游戏
 */
@Entity
@Data
public class DiceActivity implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**下注币种*/
    private Integer coinId;

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

    /**后台调控的（随机数乘以倍率）*/
    private Integer randomRate;

    /**投注个数概率*/
    private Integer backWin;

    /**收取的费用*/
    private BigDecimal chargesQty;

    /**投注数概率*/
    private Integer rollUnderWin;

}
