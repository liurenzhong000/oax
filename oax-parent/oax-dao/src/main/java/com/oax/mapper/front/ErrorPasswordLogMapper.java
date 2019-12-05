package com.oax.mapper.front;

import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.ErrorPasswordLog;

public interface ErrorPasswordLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ErrorPasswordLog record);

    int insertSelective(ErrorPasswordLog record);

    ErrorPasswordLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErrorPasswordLog record);

    int updateByPrimaryKey(ErrorPasswordLog record);

    Integer selectByUserIdInDay(@Param("userId") Integer userId, @Param("type") Integer type);

    int deleteByTime(@Param("beginTime")String beginTime,@Param("endTime")String endTime);

	void moveToHistory(@Param("beginTime")String beginTime,@Param("endTime") String endTime);

    Integer selectByIpInDay(@Param("ip")String ip, @Param("type") Integer type);
}