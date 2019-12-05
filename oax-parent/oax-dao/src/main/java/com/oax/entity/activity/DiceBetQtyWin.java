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
 * @Date: 2019/1/4 15:10
 * @Description: DICE 根据不同的币种的投注金额来限制概率
 */
@Data
@Entity
public class DiceBetQtyWin implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**币种id*/
    private Integer coinId;

    /**投注金额下限*/
    private BigDecimal minBetQty;

    /**投注金额上限*/
    private BigDecimal maxBetQty;

    /**后台调控概率*/
    private Integer backWin;

    /**是否开启：0关闭 1开启
     * @see Status
     * */
    private Integer status;

    public enum Status{
        CLOSE("关闭"), OPEN("开启");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }
}
