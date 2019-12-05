package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 14:52
 * @Description: 根据用户投注数来进行概率调控
 */
@Data
@Entity
public class DiceRollUnderWin {
    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**币种id*/
    private Integer coinId;

    /**投注数下限*/
    private Integer minRollUnder;

    /**投注数上限*/
    private Integer maxRollUnder;

    /**后台调控概率*/
    private Integer backWin;

    /**是否开启：0关闭 1开启
     * @see DiceBetQtyWin.Status
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
