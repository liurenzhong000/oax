package com.oax.controller;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.oax.common.AliOSSUtil;
import com.oax.context.HttpContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.common.SMSUtil;
import com.oax.entity.front.EmailCaptcha;
import com.oax.entity.front.Member;
import com.oax.exception.VoException;
import com.oax.exception.VoVailder;
import com.oax.service.EmailCaptchaService;
import com.oax.service.I18nMessageService;
import com.oax.service.UserService;
import com.oax.vo.CheckEmailVO;

@RestController
@RequestMapping("/email")
public class EmailController {
	private final static Logger logger = Logger.getLogger(EmailController.class);

	@Value("${register.checkUrl}")
	private String registerCheckUrl;

	@Autowired
	private  EmailCaptchaService emailService;
	@Autowired
	private  UserService userService;
	@Autowired
	private I18nMessageService I18nMessageService;

	/**
	 * 发送注册激活邮件
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/sendRegisterUrl")
	public ResultResponse sendRegisterUrl(@RequestBody String jsonString,HttpServletRequest request) {
		String lang = request.getHeader(Constant.api_header_lang);
		if(StringUtils.isBlank(jsonString)) {
			return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
		}
		String email=JSON.parseObject(jsonString).getString("email");
		try {
			if (StringUtils.isBlank(email)) {
				return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
			}
			String emailCode = UUID.randomUUID().toString().replace("-", "");
			emailService.sendRegisterUrl(email,emailCode, lang);	
			
			EmailCaptcha emailEntity=new EmailCaptcha();
			emailEntity.setToEmail(email);
			emailEntity.setCode(emailCode);
			Date date=new Date();
			emailEntity.setCreateTime(date);
			emailEntity.setExpireTime(new Date(date.getTime() + Constant.CODE_OUT_LIMIT2));
			emailService.add(emailEntity);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

		return new ResultResponse(true, I18nMessageService.getMsg(10010, lang));
	}
	
	
	/**
	 * 发送忘记密码邮件
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/sendForgetPasswordUrl")
	public ResultResponse sendForgetPasswordUrl(@RequestBody String jsonString,HttpServletRequest request) {
		String lang = request.getHeader(Constant.api_header_lang);
		if(StringUtils.isBlank(jsonString)) {
			return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
		}
		String email=JSON.parseObject(jsonString).getString("email");
		try {
			if (StringUtils.isBlank(email)) {
				return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
			}
			String emailCode = UUID.randomUUID().toString().replace("-", "");
			emailService.sendForgetPasswordUrl(email,emailCode, lang);	
			
			EmailCaptcha emailEntity=new EmailCaptcha();
			emailEntity.setToEmail(email);
			emailEntity.setCode(emailCode);
			Date date=new Date();
			emailEntity.setCreateTime(date);
			emailEntity.setExpireTime(new Date(date.getTime() + Constant.CODE_OUT_LIMIT2));
			emailService.add(emailEntity);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

		return new ResultResponse(true, I18nMessageService.getMsg(10010, lang));
	}
	

	/**
	 * 注册激活校验
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkRegisterUrl/{checkCode}")
	public ResultResponse checkRegisterUrl(@PathVariable("checkCode") String checkCode,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		boolean success=true;
		String msg="";
		try {			
			if (StringUtils.isBlank(checkCode)) {
				return new ResultResponse(false, I18nMessageService.getMsg(10012, lang));
			}
			boolean flag=emailService.checkEmail(checkCode);
			if(flag) {
				msg=I18nMessageService.getMsg(10011, lang);
			}else {
				success=false;
				msg=I18nMessageService.getMsg(10045, lang);
			}
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		
		return new ResultResponse(success, msg);
	}
	
	
	/**
	 * 忘记密码激活校验
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkForgetPasswordUrl/{checkCode}")
	public ResultResponse checkForgetPasswordUrl(@PathVariable("checkCode") String checkCode,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		boolean success=true;
		String msg="";
		try {			
			if (StringUtils.isBlank(checkCode)) {
				return new ResultResponse(false, I18nMessageService.getMsg(10012, lang));
			}
			String email=emailService.checkForgetPasswordUrl(checkCode);
			if(StringUtils.isNoneBlank(email)) {
				msg=I18nMessageService.getMsg(10011, lang);
			}else {
				success=false;
				msg=I18nMessageService.getMsg(10045, lang);
			}
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		
		return new ResultResponse(success, msg);
	}
	
	
	/**
	 * 发送邮箱验证码
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/sendEmailCode")
	public ResultResponse sendEmailCode(@RequestBody String jsonString,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		if(StringUtils.isBlank(jsonString)) {
			return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
		}
		logger.info("-------------------------------------------------sendEmailCode----------------------------------------");
		logger.info("-------------------------------------------------接收到请求----------------------------------------");

		String email=JSON.parseObject(jsonString).getString("email");
		
		if(StringUtils.isNotBlank(userId)) {
			Member member=userService.selectById(userId);
			if(member!=null&&StringUtils.isNotBlank(member.getEmail())) {
				email=member.getEmail();
			}
		}
		
		try {
			if (StringUtils.isBlank(email)) {
				return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
			}
			String emailCode = SMSUtil.getSmsCode(6);		
			String str=emailService.sendEmailCode(email,emailCode,lang);
			//String str=EmailUtil.sendTextMail(email, "邮箱验证","亲爱的OAX会员!您的邮箱验证码是:"+emailCode+",请妥善保管勿告诉他人。", true);
			if(StringUtils.isNotBlank(str)) {
				EmailCaptcha emailEntity=new EmailCaptcha();
				emailEntity.setToEmail(email);
				emailEntity.setCode(emailCode);
				Date date=new Date();
				emailEntity.setCreateTime(date);
				emailEntity.setExpireTime(new Date(date.getTime() + Constant.CODE_OUT_LIMIT1));
				emailService.add(emailEntity);
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10022, lang));
			}
		
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		
		return new ResultResponse(true, I18nMessageService.getMsg(10010, lang));
	}


	/**
	 * 校验邮箱验证码
	 * @param request
	 * @return
	 * @throws VoException 
	 */
	@RequestMapping("/checkEmailCode")
	public ResultResponse checkSms(@RequestBody @Valid CheckEmailVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);

		VoVailder.valid(result);
		boolean success = true;
		String msg = "";
		try {
			boolean b=emailService.checkEmailCode(vo.getCode(), vo.getEmail());
			if(b) {							
				msg="ok!";
			}else {
				success=false;
				msg=I18nMessageService.getMsg(10019, lang);
			}
	
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

		return new ResultResponse(success, msg);
	}
}
