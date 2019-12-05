package com.oax.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.oax.common.RedisUtil;
import com.oax.context.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.common.SMSUtil;
import com.oax.entity.front.Member;
import com.oax.entity.front.SmsCaptcha;
import com.oax.exception.VoException;
import com.oax.exception.VoVailder;
import com.oax.service.I18nMessageService;
import com.oax.service.SmsCaptchaService;
import com.oax.service.UserService;
import com.oax.vo.CheckSmsVO;
import com.oax.vo.SendSmsVO;
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {
	@Autowired
	private  SmsCaptchaService smsService;
	@Autowired
	private  UserService userService;
	@Autowired
	private I18nMessageService I18nMessageService;
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 发送短信验证码
	 * @param request
	 * @return
	 * @throws VoException 
	 */
	@RequestMapping("/sendSms")
	public ResultResponse sendSms(@RequestBody @Valid SendSmsVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);
				
		String phone=vo.getPhone();
		Integer type=vo.getType();
		
		if(StringUtils.isNotBlank(userId)&&vo.getType()!=4) {
			Member member=userService.selectById(userId);
			if(member!=null&&StringUtils.isNotBlank(member.getPhone())) {
				phone=member.getPhone();
			}
		}

		boolean success = true;
		String msg = "";
		String day ="";
		int count = 0;
		day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if(redisUtil.getString("MSMmin"+day + phone) != null){
			success=false;
			msg=I18nMessageService.getMsg(10114, lang);  //一分钟只能发送一次
			return new ResultResponse(success, msg);
		}

		String  phoneCount = redisUtil.getString("MSMday"+day + phone);
		if (phoneCount != null) {
			count = Integer.parseInt(phoneCount);
		}
		if (count >= 30) {
			success=false;
			msg=I18nMessageService.getMsg(10115, lang);  //一天只能发送30次
			return new ResultResponse(success, msg);
		}


		try {
			String smsCode=SMSUtil.getSmsCode(6);
			String templateParam="";
			String phoneTemplate="";
	
			if(vo.getPhone().startsWith("0086")) {
				phoneTemplate=vo.getPhone().substring(4);
					if (type == 1) {
						//templateCode="SMS_138925098";
						templateParam = "您正在进行注册操作，您的验证码是 " + smsCode;
					} else if (type == 2) {
						//templateCode="SMS_138925077";
						templateParam = "您正在修改登陆密码，您的验证码是 " + smsCode;
					} else if (type == 3) {
						//templateCode="SMS_138925080";
						templateParam = "您正在修改交易密码，您的验证码是 " + smsCode;
					} else if (type == 4) {
						//templateCode="SMS_138079738";
						templateParam = "您正在修改手机号码，您的验证码是 " + smsCode;
					} else if (type == 5) {
						//templateCode="SMS_138079747";
						templateParam = "您正在申请找回手机密码，您的验证码是 " + smsCode;
					} else if (type == 6) {
						//templateCode="SMS_138079748";
						templateParam = "您正在进行安全验证，您的验证码是 " + smsCode;
					} else if (type == 7) {
						//templateCode="SMS_138079748";
						templateParam = "助力码" + smsCode + "，您正在为好友助力，如非您本人操作，请忽略。";
					}else {
					//templateCode="SMS_138073415";
					templateParam = "您的验证码是 " + smsCode;
				}
			} else {
              // 走国际短信通道
				phoneTemplate=vo.getPhone();
				if (type == 1) {
					//templateCode="SMS_138925098";
					templateParam = "您正在进行注册操作，您的验证码是 " + smsCode;
				} else if (type == 2) {
					//templateCode="SMS_138925077";
					templateParam = "您正在修改登陆密码，您的验证码是 " + smsCode;
				} else if (type == 3) {
					//templateCode="SMS_138925080";
					templateParam = "您正在修改交易密码，您的验证码是 " + smsCode;
				} else if (type == 4) {
					//templateCode="SMS_138079738";
					templateParam = "您正在修改手机号码，您的验证码是 " + smsCode;
				} else if (type == 5) {
					//templateCode="SMS_138079747";
					templateParam = "您正在申请找回手机密码，您的验证码是 " + smsCode;
				} else if (type == 6) {
					//templateCode="SMS_138079748";
					templateParam = "您正在进行安全验证，您的验证码是 " + smsCode;
				} else if (type == 7) {
					//templateCode="SMS_138079748";
					templateParam = "助力码" + smsCode + "，您正在为好友助力，如非您本人操作，请忽略。";
				}else {
					//templateCode="SMS_138073415";
					templateParam = "您的验证码是 " + smsCode;
				}
			}
			log.info("手机验证码是33333333333333："+phoneTemplate);
			//短信验证码限制
			String code=SMSUtil.sendCode(phoneTemplate, templateParam);
			if (("Sucess").equals(code)) {
				SmsCaptcha smsEntity=new SmsCaptcha();
				smsEntity.setPhone(phone);
				smsEntity.setCode(smsCode);
				Date date=new Date();
				smsEntity.setCreateTime(date);
				smsEntity.setExpireTime(new Date(date.getTime() + + Constant.CODE_OUT_LIMIT1));
				smsService.add(smsEntity);

				msg=I18nMessageService.getMsg(10010, lang);
			}else {
				success=false;
				msg=I18nMessageService.getMsg(10101, lang);
			}

		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

		redisUtil.setString("MSMmin"+day+phone,"",60);
		int sumCount = count + 1;
		String sumCounts = String.valueOf(sumCount);
		redisUtil.setString("MSMday"+day+phone,sumCounts,3600*24);

		return new ResultResponse(success, msg);
	}
	
	
	/**
	 * 校验短信验证码
	 * @param request
	 * @return
	 * @throws VoException 
	 */
	@RequestMapping("/checkSms")
	public ResultResponse checkSms(@RequestBody @Valid CheckSmsVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		
		VoVailder.valid(result);
		boolean success = true;
		String msg = "";
		try {
			boolean b=smsService.checkSms(request,vo.getPhone(),vo.getCode(),lang);
			if(b) {							
				msg="校验成功!";
			}else {
				success=false;
				msg=I18nMessageService.getMsg(10007, lang);
			}
	
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

		return new ResultResponse(success, msg);
	}
}
