package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 14:30
 * @Description: 用户投注记录
 */
@Entity
@Data
public class SnatchDetail {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**配置id，用来按类别查询投注记录*/
    private Integer configId;

    /**活动期数id*/
    private Integer activityId;

    /**用户id*/
    private Integer userId;

    /**投注金额*/
    private BigDecimal betQty;

    /**中奖金额*/
    private BigDecimal payoutQty;

    /**收取的费用*/
    private BigDecimal chargesQty;

    /**币种id*/
    private Integer coinId;

    /**编号*/
    private Integer number;

    /**编号字符拼接*/
    private String numberStr;

    /**中奖hash*/
    private String hash;

    /**是否机器人*/
    private Boolean robot;

    /**创建日期*/
    private Date createTime;

    /**0未开奖 1未中奖 2中奖
     * @see Status
     * */
    private Integer status;

    public enum Status{
        UNKNOWN("未开奖"), NOT_WIN("未中奖"), WIN("中奖");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }
}
