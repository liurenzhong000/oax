package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.PanicBuyDetail;
import com.oax.entity.admin.param.PanicBuyDetailParam;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PanicBuyDetailMapper extends BaseMapper<PanicBuyDetail> {

    PanicBuyDetail getCanBuyDetail(Integer userId);

    //用户是否参加活动
    boolean participated(@Param("activityId") Integer activityId,@Param("userId") Integer userId);

    //用户是否达标
    boolean reached(@Param("activityId")Integer activityId, @Param("userId") Integer userId);

    PanicBuyDetail selectByUserIdAndActivityId(@Param("userId") Integer userId, @Param("activityId") Integer activityId);

    List<PanicBuyDetail> pageForAdmin(PanicBuyDetailParam param);

    int updateReachUser(@Param("activityId") Integer id,@Param("validTime") Date validTime);
}
