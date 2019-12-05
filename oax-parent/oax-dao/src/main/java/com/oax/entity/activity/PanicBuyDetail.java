package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * BHB抢购活动 用户参与详情
 */
@Setter
@Getter
@Entity
public class PanicBuyDetail implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**活动id*/
    private Integer activityId;

    /**用户活动分享标识码*/
    private String shareCode;

    /**用户中奖可以购买的个数*/
    private BigDecimal qty;

    /**剩余可交易量*/
    private BigDecimal remainQty;

    /**参与时间*/
    private Date createTime;

    /**
     * 达标时间
     */
    private Date finishTime;

    /***
     * 达标后的购买有效期
     */
    private Date validTime;

    /**
     * 助力人数
     */
    private Integer helpCount;

    /**
     * 当前助力值
     */
    private Integer helpValue;

    /**
     * 当前用户对应任务的状态
     * @see Status
     */
    private Integer status;

    public enum Status{
        PARTICIPATE("已参与"), SUCCESS("达标");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }
}
