package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.InvitingFeedback;

@Mapper
public interface InvitingFeedbackMapper {
    List<InvitingFeedback> pullInvitingFeedbackList(@Param("beginTime") String beginTime, @Param("endTime")String endTime);

    Integer save(InvitingFeedback invitingFeedback);
    BigDecimal getMyInvitingFeedback(@Param("userId") Integer userId, @Param("beginTime")String beginTime, @Param("endTime")String endTime);
}
