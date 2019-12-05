package com.oax.mapper.front;

import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.SmsCaptcha;

public interface SmsCaptchaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsCaptcha record);

    int insertSelective(SmsCaptcha record);

    SmsCaptcha selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsCaptcha record);

    int updateByPrimaryKey(SmsCaptcha record);

    SmsCaptcha selectByPhone(String phone);

    int deleteByTime(@Param("beginTime")String beginTime,@Param("endTime") String endTime);

	void moveToHistory(@Param("beginTime")String beginTime,@Param("endTime") String endTime);

	SmsCaptcha selectByPhoneAndCode(@Param("phone")String phone,@Param("smsCode") String smsCode);
}