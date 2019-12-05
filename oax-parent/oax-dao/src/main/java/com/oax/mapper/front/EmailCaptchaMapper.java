package com.oax.mapper.front;

import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.EmailCaptcha;

public interface EmailCaptchaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmailCaptcha record);

    int insertSelective(EmailCaptcha record);

    EmailCaptcha selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmailCaptcha record);

    int updateByPrimaryKey(EmailCaptcha record);

    EmailCaptcha selectByEmail(String email);

    String selectEmailByCode(String emailCode);

    int deleteByTime(@Param("beginTime")String beginTime,@Param("endTime") String endTime);

	void moveToHistory(@Param("beginTime")String beginTime,@Param("endTime") String endTime);

	EmailCaptcha selectByEmailAndCode(@Param("email")String email,@Param("emailCode") String emailCode);
}