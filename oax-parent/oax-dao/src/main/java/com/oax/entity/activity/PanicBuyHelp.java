package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * BHB抢购活动 助力详情
 */
@Setter
@Getter
@Entity
public class PanicBuyHelp implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**用户id*/
    private Integer userId;

    /**活动id*/
    private Integer activityId;

    /**助力好友手机号*/
    private String phone;

    /**助力值*/
    private Integer helpValue;

    /**助力时间*/
    private Date createTime;
}
