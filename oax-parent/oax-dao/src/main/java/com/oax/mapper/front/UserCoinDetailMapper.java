package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.admin.param.ActivityCoinParam;
import com.oax.entity.admin.vo.UserCoinDetailVo;
import com.oax.entity.front.UserCoinDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UserCoinDetailMapper extends BaseMapper<UserCoinDetail> {

    List<UserCoinDetail> selectListByUserId(Integer userId);

    BigDecimal getAverageThreshold(@Param("userId") Integer userId, @Param("coinId") Integer coinId,
                                   @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<UserCoinDetailVo> getActivityUserDetailList(ActivityCoinParam param);
}
