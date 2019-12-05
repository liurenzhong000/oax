package com.oax.vo;

import lombok.Data;

import java.util.Date;

/**
 * BHB抢购活动 首页数据
 */
@Data
public class PanicBuyActivityDataVo {

    /**达标人数 reach + baseReach*/
    private Integer reachAll;

    /**完成需要的达标数*/
    private Integer reachNeed;

    /**参与人数*/
    private Integer participateAll;

    /**活动开奖时间*/
    private Date endDate;



    /**用户助力分享码，标识用户和活动id*/
    private String shareCode;

    /**当前活动id*/
    private Integer activityId;

    /**
     * 状态：0活动未开始  1活动已开始，未参加  2未参加，已结束  3.未开奖，已参加  4.未开奖，已达标  5.开奖了，未达标 6开奖了，拥有购买资格
     * @see Status
     * */
    private Integer status;

    public enum Status{
        NO_START("活动未开始"), NO_PARTICIPATE("活动已开始，未参加"), OVER("未参加，已结束"), PARTICIPATE("未开奖，已参加"),
        REACH("未开奖，已达标"), NO_REACH("开奖了，未达标"), CAN_BUY("开奖了，拥有购买资格"), BUYED("开奖了，拥有购买资格,已购买");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }
}
