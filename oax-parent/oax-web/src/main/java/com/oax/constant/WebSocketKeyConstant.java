package com.oax.constant;

/**
 * @Auther: hyp
 * @Date: 2019/1/18 14:21
 * @Description: webSocket 连接常量
 */
public interface WebSocketKeyConstant {

    //一元夺宝首页，用户投注数据
    String SNATCH_BET_MSG = "/topic/snatchActivity/betMsg";

    //一元夺宝首页，用户投注数据
    String SNATCH_ACTIVITY_MSG = "/topic/snatchActivity/activityMsg";

    //一元夺宝开奖播报
    String SNATCH_LOTTERY_BROADCAST_MSG = "/topic/snatchActivity/lotteryMsg";
}
