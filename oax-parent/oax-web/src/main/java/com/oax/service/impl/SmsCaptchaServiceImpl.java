package com.oax.service.impl;

import java.util.Calendar;
import java.util.Date;

import com.oax.Constant;
import com.oax.common.SysUtil;
import com.oax.entity.front.ErrorPasswordLog;
import com.oax.entity.front.Member;
import com.oax.mapper.front.ErrorPasswordLogMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.service.I18nMessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oax.common.HttpRequestUtil;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.SmsCaptcha;
import com.oax.exception.VoException;
import com.oax.mapper.front.SmsCaptchaMapper;
import com.oax.service.SmsCaptchaService;

import javax.servlet.http.HttpServletRequest;

@Service
public class SmsCaptchaServiceImpl implements SmsCaptchaService{
	
	@Autowired
	private SmsCaptchaMapper smsCaptchaDao;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private ErrorPasswordLogMapper errorPasswordLogMapper;
	@Autowired
	private com.oax.service.I18nMessageService I18nMessageService;
	
	@Override
	@DataSource(DataSourceType.MASTER)
	public void add(SmsCaptcha smsEntity) {
		smsCaptchaDao.insertSelective(smsEntity);
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean checkSms(HttpServletRequest request,String phone,String sms,String lang) throws VoException  {
		boolean b=false;
		String ip= SysUtil.getRemoteIp(request);
		if (StringUtils.isNotBlank(ip) && StringUtils.contains(ip, ",")) {//加了防护好像ip会变成115.199.127.106, 185.254.242.41
			ip = StringUtils.substringBefore(ip, ",");
		}
		Member user=memberMapper.selectByPhoneOrEmail(phone);
		//查询当天IP错误次数
		Integer errorCounts=errorPasswordLogMapper.selectByIpInDay(ip,3);
		if(errorCounts>= Constant.LONGIN_COUNT_LIMIT) {
			throw new VoException(I18nMessageService.getMsg(10054, lang));
		}
		try {
			SmsCaptcha  smsCaptcha =smsCaptchaDao.selectByPhone(phone);
			if(smsCaptcha!=null) {
				if(sms.equals(smsCaptcha.getCode())) {
					SmsCaptcha smscap=new SmsCaptcha();

					Calendar now = Calendar.getInstance();
					now.add(Calendar.MINUTE, 1);// 1分钟之后的时间
					Date expireTime = now.getTime();

					smscap.setId(smsCaptcha.getId());
					smscap.setExpireTime(expireTime);
					smsCaptchaDao.updateByPrimaryKeySelective(smscap);
					b=true;
				}else {
					ErrorPasswordLog record=new ErrorPasswordLog();
					record.setUserId(user.getId());
					record.setCreateTime(new Date());
					record.setUpdateTime(new Date());
					record.setType(3);
					record.setIp(ip);
					errorPasswordLogMapper.insertSelective(record);
				}
			}else {
				ErrorPasswordLog record=new ErrorPasswordLog();
				record.setType(3);
				record.setIp(ip);
				record.setCreateTime(new Date());
				record.setUpdateTime(new Date());
				errorPasswordLogMapper.insertSelective(record);
			}
		} catch (Exception e) {
			throw new VoException("smsCode is wrong");
		}
		return b;
	}

	/**
	 * 验证后，验证码立马过期
	 */
	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean checkSmsOnce(String phone,String sms) throws VoException  {
		boolean b=false;
		try {
			SmsCaptcha  smsCaptcha =smsCaptchaDao.selectByPhone(phone);
			if(smsCaptcha!=null) {
				if(sms.equals(smsCaptcha.getCode())) {
					SmsCaptcha smscap=new SmsCaptcha();
					Date expireTime = new Date();
					smscap.setId(smsCaptcha.getId());
					smscap.setExpireTime(expireTime);
					smsCaptchaDao.updateByPrimaryKeySelective(smscap);
					b=true;
				}
			}
		} catch (Exception e) {
			throw new VoException("smsCode is wrong");
		}
		return b;
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean checkNewlyCode(HttpServletRequest request,String phone, String sms, String lang) throws VoException  {
		String ip= SysUtil.getRemoteIp(request);
		if (StringUtils.isNotBlank(ip) && StringUtils.contains(ip, ",")) {//加了防护好像ip会变成115.199.127.106, 185.254.242.41
			ip = StringUtils.substringBefore(ip, ",");
		}
		boolean b=false;
		Member user=memberMapper.selectByPhoneOrEmail(phone);
		//查询当天IP错误次数
		Integer errorCounts=errorPasswordLogMapper.selectByIpInDay(ip,3);
		if(errorCounts>= Constant.LONGIN_COUNT_LIMIT) {
			throw new VoException(I18nMessageService.getMsg(10054, lang));
		}
		try {
			SmsCaptcha  smsCaptcha =smsCaptchaDao.selectByPhoneAndCode(phone, sms);
			if(smsCaptcha!=null) {
				b=true;
			}else {
				ErrorPasswordLog record=new ErrorPasswordLog();
				record.setUserId(user.getId());
				record.setType(3);
				record.setCreateTime(new Date());
				record.setUpdateTime(new Date());
				errorPasswordLogMapper.insertSelective(record);
			}
		} catch (Exception e) {
			throw new VoException("smsCode is wrong");
		}
		return b;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public void resetExpireTime(String phone, String smsCode) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 3);// 3分钟之后的时间
		Date expireTime = now.getTime();
		   
		SmsCaptcha  smsCaptcha =smsCaptchaDao.selectByPhoneAndCode(phone,smsCode);
		SmsCaptcha smscap=new SmsCaptcha();
		smscap.setId(smsCaptcha.getId());
		smscap.setExpireTime(expireTime);
		smsCaptchaDao.updateByPrimaryKeySelective(smscap);
	}

	@Async
	@Override
	public void sendPwd(String phone, String pwd) {
//		String templateCode = "SMS_142000012";
		JSONObject json = new JSONObject();
		json.put("phone",phone.substring(4));
		json.put("password",pwd);
		String  url = "http://api.sms.cn/sms/";
		String  param = "ac=send&uid=oax2018&pwd=3120a283bab1d155b76fdf19bd46c0da&mobile="+phone.substring(4)+"&template=470313&content="+json.toJSONString();
		//		SMSUtil.sendCode(phone, templateCode,json.toJSONString());
		String response = HttpRequestUtil.sendGet(url, param);
		if(response!=null){
			String code = JSON.parseObject(response).getString("stat");
			if (!"100".equals(code)){
				System.out.println("短信发送账号&密码的请求异常==="+response);
			}
		}else{
			System.out.println("短信发送账号&密码的请求结果为null");
		}
	}
}
