package com.oax.mapper.front;

import java.util.List;

import com.oax.entity.front.UserLoginLog;

public interface UserLoginLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLoginLog record);

    int insertSelective(UserLoginLog record);

    UserLoginLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLoginLog record);

    int updateByPrimaryKey(UserLoginLog record);

    List<UserLoginLog> selectByUserId(Integer userId);

    UserLoginLog selectByUserIdOne(Integer userId);

	int deleteByCounts(Integer counts);

	void moveToHistory(Integer counts);
}