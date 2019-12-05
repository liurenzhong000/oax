package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 09:44
 * @Description: 夺宝游戏配置
 */
@Entity
@Data
public class SnatchConfig {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer coinId;

    /**奖池名称*/
    private String name;

    /**数量*/
    private Integer quantity;

    /**最大投注数量*/
    private Integer maxQuantity;

    /**单位*/
    private BigDecimal unit;

    /**中奖人数*/
    private Integer winNumber;

    /**当期号，序号*/
    private Integer ordinal;

    /**布局排序序号*/
    private Integer sequence;

    /**乐观锁*/
    private Integer version;

    /**后台调控机器人投注概率*/
    private Integer robotBackWin;

    /**机器人中奖人数*/
    private Integer robotWinCount;

    /**是否开启 0关闭，1开启
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
