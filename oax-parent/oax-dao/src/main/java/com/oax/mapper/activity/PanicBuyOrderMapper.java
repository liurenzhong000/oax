package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.PanicBuyOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PanicBuyOrderMapper extends BaseMapper<PanicBuyOrder> {

    PanicBuyOrder selectByUserIdAndActivityId(@Param("userId") Integer userId, @Param("activityId") Integer activityId);

    List<PanicBuyOrder> selectForWeb(Integer userId);

    int updateCloseByActivityId(Integer activityId);
}
