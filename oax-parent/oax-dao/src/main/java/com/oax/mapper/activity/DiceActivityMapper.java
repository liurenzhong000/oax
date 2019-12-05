package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.DiceActivity;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.admin.vo.DiceActivityStatisticsVo;
import com.oax.entity.admin.vo.DiceStatisticsAggreVo;
import com.oax.entity.front.vo.DiceActivityVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 09:31
 * @Description: DICE游戏
 */
public interface DiceActivityMapper extends BaseMapper<DiceActivity> {

    List<DiceActivityVo> pageForWebByUserId(@Param("userId") Integer userId, @Param("coinId") Integer coinId);

    BigDecimal sumAllBetQty();

    BigDecimal sumAllPayoutQty();

    BigDecimal countUser();

    List<DiceActivityStatisticsVo> statistics(@Param("userId") Integer userId, @Param("coinId") Integer coinId, @Param("param") PageParam param);

    //聚合统计
    DiceStatisticsAggreVo statisticsAggre(@Param("userId") Integer userId, @Param("coinId") Integer coinId, @Param("param") PageParam param);

    List<DiceActivity> pageForAdmin(@Param("userId") Integer userId, @Param("coinId") Integer coinId, @Param("param") PageParam param);

    List<DiceActivityStatisticsVo> statisticsByUserIds(@Param("userIds")  Set<String> userIds,  @Param("coinId") Integer coinId,  @Param("param") PageParam param);
}
