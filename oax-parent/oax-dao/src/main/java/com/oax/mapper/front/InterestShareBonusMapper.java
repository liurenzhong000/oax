package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.InterestShareBonus;

public interface InterestShareBonusMapper {
    List<InterestShareBonus> getInterestFee(@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    Integer insert(InterestShareBonus model);

    List<Map<String,Object>> myIncome(Integer userId);
}
