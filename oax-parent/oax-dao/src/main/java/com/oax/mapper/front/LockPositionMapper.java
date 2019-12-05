package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.LockPosition;
import com.oax.entity.front.LockPositionInfo;
import com.oax.entity.front.UserCoin;

@Mapper
public interface LockPositionMapper {
    Integer insert(LockPosition lockPosition);

    Integer subtractUserCoin(UserCoin userCoin);

    Integer declock(@Param("id") Integer id, @Param("userId") Integer userId);

    List<Map<String,Object>> getPage(Map<String, Object> map);

    LockPosition selectLockPositionById(Integer id);

    Integer addUserCoin(UserCoin userCoin);

    Integer sysDeblocking(@Param("id") Integer id, @Param("userId") Integer userId);

    List<LockPositionInfo> getLockPositionList(@Param("endTime") String endTime);

    List<Map<String, Object>> getInterestSharebonusList(Integer lockPositionId);

    List<LockPosition> getExpirePositionList(@Param("endTime") String endTime);
}
