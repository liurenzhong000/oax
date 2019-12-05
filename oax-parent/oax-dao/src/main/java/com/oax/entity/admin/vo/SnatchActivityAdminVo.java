package com.oax.entity.admin.vo;

import com.oax.entity.activity.SnatchActivity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 19:30
 * @Description: 每期奖池详情
 */
@Setter
@Getter
public class SnatchActivityAdminVo {

    private Integer id;

    /**配置id*/
    private Integer configId;

    /**奖池名称*/
    private String configName;

    /**期数*/
    private Integer ordinal;

    /**币种id*/
    private String coinName;

    /**当前完成进度*/
    private Integer finishQuantity;

    /**最大数量*/
    private Integer quantity;

    /**最大投注数量*/
    private Integer maxQuantity;

    /**中奖人数*/
    private Integer winNumber;

    /**最小投注单位*/
    private BigDecimal unit;

    /**开奖时间*/
    private Date lotteryTime;

    /**后台调控机器人中奖概率*/
    private Integer robotBackWin;

    /**计划添加机器人中奖人数*/
    private Integer robotWinCount;

    /**当前已添加机器人数*/
    private Integer currRobotWinCount;

    private Date createTime;

    /**是否已经结束0进行中，1已开奖
     * @see SnatchActivity.Status
     * */
    private Integer status;
}
