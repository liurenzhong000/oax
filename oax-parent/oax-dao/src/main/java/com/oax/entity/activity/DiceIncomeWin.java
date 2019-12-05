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
 * @Date: 2018/12/30 20:04
 * @Description: 根据用户的总收益来限制用户的概率
 */
@Data
@Entity
public class DiceIncomeWin implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer coinId;

    /**收入下限*/
    private BigDecimal minIncome;

    /**收入上限*/
    private BigDecimal maxIncome;

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
