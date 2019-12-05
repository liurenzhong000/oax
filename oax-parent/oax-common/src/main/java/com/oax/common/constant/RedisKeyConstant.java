package com.oax.common.constant;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 11:26
 * @Description:
 */
public interface RedisKeyConstant {

    //ip请求url请求限制
    String ASSESS_LIMIT_KEY = "assess_limit:";

    //DICE 对应coinId 的正常数值 相乘的倍率 dice_random_rate: + coinId
//    String RANDOM_RATE_KEY = "dice_random_rate:";

    //DICE 用户对应币种的盈利数 dice_income: + userId + : + coinId
    String DICE_INCOME_KEY = "dice_income:";
    //DICE 用户对应币种的盈利数 dice_income_zset: + coinId
    String DICE_INCOME_ZSET_KEY = "dice_income_zset:";

    String COUNT_SIGN_ZSET_KEY = "count_sign_zset";

    String BONUS_RECORD_ZSET_KEY = "bonus_record_zset";
    //DICE 后台dice统计缓存 dice_statistics_aggre: + coinId
    String DICE_STATISTICS_AGGRE_KEY = "dice_statistics_aggre:";

    //用户20:00时的余额记录，用来决定bonus_balance的值 bonus_snapshoot_log：+userId
    String BONUS_SNAPSHOOT_LOG_KEY = "bonus_snapshoot_log:";

    //BHB投注数
    String DICE_BHB_BET_QTY_KEY = "dice_bhb_bet_qty";
    //BCB产出量
    String DICE_BCB_MINING_KEY = "dice_bcb_mining";
    //BHB手续费
    String DICE_BHB_CHARGES_KEY = "dice_bhb_charges";
    //一元夺宝开奖播报
    String SNATCH_LOTTERY_MSG_KEY = "snatch_lottery_msg";

    String LOCK_WITHDRAW = "lock_withdraw:";

}
