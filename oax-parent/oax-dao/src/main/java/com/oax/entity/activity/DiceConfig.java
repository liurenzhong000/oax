package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 14:35
 * @Description: dice相关配置
 */
@Data
@Entity
public class DiceConfig implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**币种id*/
    private Integer coinId;

    /**对应币种的最小投注金额*/
    private BigDecimal minBetQty;

    /**对应币种的最大投注金额*/
    private BigDecimal maxBetQty;

    /**小数位数*/
    private Integer decimals;

    /**顺序：越小越前*/
    private Integer sequence;

    /**状态*/
    private Integer status;

    /**手续费-获奖金额扣除手续费*/
    private BigDecimal charges;

    public enum Status{
        CLOSE("关闭"), OPEN("开启");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }
}
