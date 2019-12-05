package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.activity.DiceActivityService;
import com.oax.admin.vo.DiceStatisticsData;
import com.oax.common.*;
import com.oax.common.constant.RedisKeyConstant;
import com.oax.common.json.JsonHelper;
import com.oax.entity.activity.DiceActivity;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.admin.vo.DiceActivityStatisticsVo;
import com.oax.entity.admin.vo.DiceStatisticsAggreVo;
import com.oax.mapper.activity.DiceActivityMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Auther: hyp
 * @Date: 2018/12/27 13:52
 * @Description:
 */
@Service
public class DiceActivityServiceImpl implements DiceActivityService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DiceActivityMapper diceActivityMapper;

    @Override
    public DiceStatisticsData statistics(Integer userId, Integer coinId, PageParam param) {
        if (userId == null && param.getStartTime() == null && param.getEndTime() == null) {//避免大表全量查询，从redis取
            return getUserIdsOrderByIncomeFromRedis(coinId, param);
        }else {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
            List<DiceActivityStatisticsVo> statisticsVos = diceActivityMapper.statistics(userId, coinId, param);
            DiceStatisticsAggreVo statisticsAggre = diceActivityMapper.statisticsAggre(userId, coinId, param);
            DiceStatisticsData diceStatisticsData = BeanHepler.copy(statisticsAggre, DiceStatisticsData.class);
            diceStatisticsData.setPageInfo(new PageInfo<>(statisticsVos));
            return diceStatisticsData;
        }
    }

    //从redis中获取
    private DiceStatisticsData getUserIdsOrderByIncomeFromRedis(Integer coinId, PageParam param){
        AssertHelper.notEmpty(coinId, "币种id不能为空");
        int start = RedisPageHelper.getStart(param.getPageNum(), param.getPageSize());
        int end = RedisPageHelper.getEnd(param.getPageNum(), param.getPageSize());
        Set<String> userIds = redisUtil.reverseRange(RedisKeyConstant.DICE_INCOME_ZSET_KEY + coinId, start, end);
        List<DiceActivityStatisticsVo> statisticsVos = new ArrayList<>();
        if (!EmptyHelper.isEmpty(userIds)) {
            statisticsVos = diceActivityMapper.statisticsByUserIds(userIds, coinId, param);
        }
        Integer size = redisUtil.zsize(RedisKeyConstant.DICE_INCOME_ZSET_KEY + coinId).intValue();
        DiceStatisticsAggreVo statisticsAggre = redisUtil.getObject(RedisKeyConstant.DICE_STATISTICS_AGGRE_KEY + coinId, DiceStatisticsAggreVo.class);
        if (statisticsAggre == null) {
            statisticsAggre = diceActivityMapper.statisticsAggre(null, coinId, param);
            redisUtil.setString(RedisKeyConstant.DICE_STATISTICS_AGGRE_KEY + coinId, JsonHelper.writeValueAsString(statisticsAggre), 3600);//1小数
        }
        DiceStatisticsData diceStatisticsData = BeanHepler.copy(statisticsAggre, DiceStatisticsData.class);
        PageInfo pageInfo = new PageInfo<>(statisticsVos);
        pageInfo.setTotal(size);
        diceStatisticsData.setPageInfo(pageInfo);
        return diceStatisticsData;
    }

    @Override
    public PageInfo list(Integer userId, Integer coinId, PageParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<DiceActivity> diceActivities = diceActivityMapper.pageForAdmin(userId, coinId, param);
        return new PageInfo<>(diceActivities);
    }

}
