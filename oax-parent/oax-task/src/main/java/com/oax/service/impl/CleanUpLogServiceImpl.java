package com.oax.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.mapper.front.EmailCaptchaMapper;
import com.oax.mapper.front.ErrorPasswordLogMapper;
import com.oax.mapper.front.SmsCaptchaMapper;
import com.oax.mapper.front.UserLoginLogMapper;
import com.oax.service.CleanUpLogService;

@Service
public class CleanUpLogServiceImpl implements CleanUpLogService {
    @Autowired
    private ErrorPasswordLogMapper errorPasswordLogMapper;

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;
    
    @Autowired
    private EmailCaptchaMapper emailCaptchaMapper;
    
    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

	@Override
	@Transactional
	public void cleanUpLog(String beginTime,String endTime) {
		//将要清除的log转到history库中
		errorPasswordLogMapper.moveToHistory(beginTime,endTime);
		emailCaptchaMapper.moveToHistory(beginTime,endTime);
		smsCaptchaMapper.moveToHistory(beginTime,endTime);
		userLoginLogMapper.moveToHistory(5);
		
		//清除上个月的log
		errorPasswordLogMapper.deleteByTime(beginTime,endTime);		
		emailCaptchaMapper.deleteByTime(beginTime,endTime);
		smsCaptchaMapper.deleteByTime(beginTime,endTime);
		//清除用户登录log,每个用户最多剩下5条，小于5条则不清除
		userLoginLogMapper.deleteByCounts(5);				
	}
}
