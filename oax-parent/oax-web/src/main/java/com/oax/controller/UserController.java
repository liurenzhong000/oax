package com.oax.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.oax.Constant;
import com.oax.common.*;
import com.oax.common.util.thread.ThreadManager;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.admin.param.UserInviteParam;
import com.oax.entity.admin.vo.UserInviteVo;
import com.oax.entity.front.EmailCaptcha;
import com.oax.entity.front.Member;
import com.oax.entity.front.vo.InviteUserVo;
import com.oax.exception.VoException;
import com.oax.exception.VoVailder;
import com.oax.service.EmailCaptchaService;
import com.oax.service.I18nMessageService;
import com.oax.service.SmsCaptchaService;
import com.oax.service.UserService;
import com.oax.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/user")
public class UserController {
	@Value("${register.checkUrl}")
	private String registerCheckUrl;

	@Autowired
	private UserService userService;
	@Autowired
	private I18nMessageService I18nMessageService;

	@Autowired
	private AccessTokenManager tokenManager;

	@Autowired
	private SmsCaptchaService smsService;

	@Autowired
	private EmailCaptchaService emailService;

	/**
	 * 邮箱注册
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/emailRegister")
	@AccessLimit(limit = 50, sec = 7200)//2小时一个ip50次
	public ResultResponse emailRegister(@RequestBody @Valid EmailRegister vo, BindingResult result,HttpServletRequest request) throws VoException {
		checkEmail(vo.getEmail());
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);
		if (!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		boolean success = true;
		String msg = "";
		boolean flag = userService.checkExist(vo.getEmail());
		if (!flag) {
			String code=ShareCodeUtil.generateShortUuid();
			Member user = new Member();
			String email=vo.getEmail();
			user.setEmail(email);
			user.setPassword(MD5.encrypt(vo.getPassword()));
			user.setLockStatus(0);
			user.setCheckStatus(0);
			user.setNeedTransactionPassword(0);
			user.setCode(code);
			user.setLevel(0);
			user.setSource(vo.getSource());
			user.setRegisterTime(new Date());
			user.setUpdateTime(new Date());
			user.setEmailStatus(1);
			user.setPhoneStatus(0);
			user.setGoogleStatus(0);
			user.setRegisterType(2);
			user.setType(1);
			int count = userService.register(user,vo.getInvateCode());
			if (count > 0) {
				String emailCode = UUID.randomUUID().toString().replace("-", "");
				emailService.sendRegisterUrl(email,emailCode,lang);

				EmailCaptcha emailEntity=new EmailCaptcha();
				emailEntity.setToEmail(email);
				emailEntity.setCode(emailCode);
				Date date=new Date();
				emailEntity.setCreateTime(date);
				emailEntity.setExpireTime(new Date(date.getTime() + Constant.CODE_OUT_LIMIT1));
				emailService.add(emailEntity);
				msg = I18nMessageService.getMsg(10003, lang);
			} else {
				success = false;
				msg = I18nMessageService.getMsg(10101, lang);
//                msg = I18nMessageService.getMsg(10112,lang);
			}

		} else {
			success = false;
			msg = I18nMessageService.getMsg(10004, lang);
		}

		return new ResultResponse(success, msg);
	}

	private static void checkEmail(String email) {
		String[] mainCurrentEmail = {"qq.com","gmail.com","sina.com","sina.cn","tom.com","163.net",
				"163.com","hotmail.com","yahoo.com","sohu.com","msn.com","126.com","live.com"};
		String emailSuffix = StringUtils.substringAfter(email, "@");
		List<String> mainCurrentEmailList = Arrays.asList(mainCurrentEmail);
		AssertHelper.isTrue(mainCurrentEmailList.contains(emailSuffix), "发生异常，请联系平台："+ email);
	}

	/**
	 * 注册 app
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/registerApp")
	public ResultResponse registerApp(@RequestBody @Valid AppRegister vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);

		if (!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		boolean success = true;
		String msg = "";
		boolean flag = userService.checkExist(vo.getUsername());
		if (!flag) {
			String code=ShareCodeUtil.generateShortUuid();
			Member user = new Member();
			if(vo.getType()==1) {
				boolean trueSmsCode = smsService.checkNewlyCode(request,vo.getUsername(), vo.getCode(),lang);
				if (!trueSmsCode) {
					return new ResultResponse(false, I18nMessageService.getMsg(10034, lang));
				}
				user.setPhone(vo.getUsername());
				user.setEmailStatus(0);
				user.setPhoneStatus(1);
			}else if(vo.getType()==2) {
				boolean trueEmailCode = emailService.checkNewlyCode(vo.getCode(), vo.getUsername());
				if (!trueEmailCode) {
					return new ResultResponse(false, I18nMessageService.getMsg(10034, lang));
				}
				user.setEmail(vo.getUsername());
				user.setEmailStatus(1);
				user.setPhoneStatus(0);
			}
			user.setPassword(MD5.encrypt(vo.getPassword()));
			user.setLockStatus(0);
			user.setCheckStatus(0);
			user.setNeedTransactionPassword(0);
			user.setCode(code);
			user.setLevel(1);
			user.setSource(vo.getSource());
			user.setRegisterTime(new Date());
			user.setUpdateTime(new Date());
			user.setGoogleStatus(0);
			user.setRegisterType(vo.getType());
			user.setType(1);
			int count = userService.register(user,vo.getInvateCode());
			if (count > 0) {
				msg = I18nMessageService.getMsg(10003, lang);
			} else {
				success = false;
				msg = I18nMessageService.getMsg(10101, lang);
			}
		} else {
			success = false;
			msg = I18nMessageService.getMsg(10004, lang);
		}

		return new ResultResponse(success, msg);
	}


	/**
	 * 手机注册
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/phoneRegister")
	public ResultResponse phoneRegister(@RequestBody @Valid PhoneRegister vo, BindingResult result,HttpServletRequest request) throws VoException {
		VoVailder.valid(result);
		String lang = request.getHeader(Constant.api_header_lang);

		if (!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		boolean success = true;
		String msg = "";
		boolean flag = userService.checkExist(vo.getPhone());
		boolean isTrue=smsService.checkSms(request,vo.getPhone(),vo.getSmsCode(),lang);
		if (!flag) {
			if(isTrue) {
				String code=ShareCodeUtil.generateShortUuid();
				Member user = new Member();
				user.setPhone(vo.getPhone());
				user.setPassword(MD5.encrypt(vo.getPassword()));
				user.setLockStatus(0);
				user.setCheckStatus(0);
				user.setNeedTransactionPassword(0);
				user.setCode(code);
				user.setLevel(1);
				user.setSource(vo.getSource());
				user.setRegisterTime(new Date());
				user.setUpdateTime(new Date());
				user.setEmailStatus(0);
				user.setPhoneStatus(1);
				user.setGoogleStatus(0);
				user.setRegisterType(1);
				user.setType(1);
				int count = userService.register(user,vo.getInvateCode());
				if (count > 0) {
					msg = I18nMessageService.getMsg(10003, lang);
				} else {
					success = false;
					msg = I18nMessageService.getMsg(10101, lang);
//                    msg = I18nMessageService.getMsg(10112,lang);
				}
			}else {
				success = false;
				msg = I18nMessageService.getMsg(10007, lang);
			}
		} else {
			success = false;
			msg = I18nMessageService.getMsg(10004, lang);
		}

		return new ResultResponse(success, msg);
	}

	/**
	 * 判断邮箱是否已被注册
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/checkEmail")
	public ResultResponse checkEmail(@RequestBody String jsonString,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		if(StringUtils.isBlank(jsonString)) {
			return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
		}
		String email=JSON.parseObject(jsonString).getString("email");
		boolean flag = userService.checkExist(email);

		if(flag) {
			return new ResultResponse(false, I18nMessageService.getMsg(10004, lang));
		}else {
			return new ResultResponse(true, "");
		}
	}

	/**
	 * 判断手机是否已被注册
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/checkPhone/{phone}")
	public ResultResponse checkPhone(@PathVariable("phone") String phone,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);

		boolean flag = userService.checkExist(phone);
		if(flag) {
			return new ResultResponse(false, I18nMessageService.getMsg(10004, lang));
		}else {
			return new ResultResponse(true, "");
		}
	}

	/**
	 * 查询需要的验证类型
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/queryCheckType")
	public ResultResponse queryCheckType(@RequestBody String jsonString,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		if(StringUtils.isBlank(jsonString)) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		String username=JSON.parseObject(jsonString).getString("username");
		Map<String, Object> map = userService.queryCheckType(username,lang);

		return new ResultResponse(true, map);

	}


	/**
	 * 验证登陆密码
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/checkLoginPassword")
	public ResultResponse checkLoginPassword(@RequestBody @Valid CheckLoginPasswordVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		VoVailder.valid(result);
		String lang = request.getHeader(Constant.api_header_lang);
		// 验证密码
		userService.login(request,vo.getUsername(), vo.getPassword(),lang);
		Map<String, Object> map = userService.queryCheckType(vo.getUsername(),lang);

		return new ResultResponse(true, map);

	}

	/**
	 * 校验修改密码(登陆和交易密码)
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/checkPassword")
	public ResultResponse checkOldPassword(@RequestBody @Valid CheckPasswordVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		VoVailder.valid(result);
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);

		Member member =userService.selectById(userId);
		String oldPassWord=MD5.encrypt(vo.getOldPassword());
		String newPassWord=MD5.encrypt(vo.getNewPassword());
		// 验证密码
		if(vo.getType().intValue()==1){
			if(!oldPassWord.equals(member.getPassword())) {
				return new ResultResponse("10058",false, I18nMessageService.getMsg(10058, lang));
			}

			if(newPassWord.equals(member.getTransactionPassword())) {
				return new ResultResponse("10077",false, I18nMessageService.getMsg(10063, lang));
			}

		}else{
			if(!oldPassWord.equals(member.getTransactionPassword())) {
				return new ResultResponse("10058",false, I18nMessageService.getMsg(10058, lang));
			}

			if(newPassWord.equals(member.getPassword())) {
				return new ResultResponse("10063",false, I18nMessageService.getMsg(10063, lang));
			}
		}

		return new ResultResponse(true, "ok");
	}

	/**
	 * 校验忘记密码(登陆和交易密码)
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/checkForgetPassword")
	public ResultResponse checkForgetPassword(@RequestBody @Valid ForgetPasswordVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		VoVailder.valid(result);
		String lang = request.getHeader(Constant.api_header_lang);
		Member member =null;
		String passWord= MD5.encrypt(vo.getPassword());
		String repeatPassword=MD5.encrypt(vo.getRepeatPassword());

		if(!passWord.equals(repeatPassword)){
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}
		// 验证密码
		if(vo.getType().intValue()==1){
			member=userService.selectByEmail(vo.getUsername());
			if(passWord.equals(member.getTransactionPassword())) {
				return new ResultResponse("10077",false, I18nMessageService.getMsg(10063, lang));
			}

		}else{
			String userId = request.getHeader(Constant.api_header_userId);
			member =userService.selectById(userId);
			if(passWord.equals(member.getPassword())) {
				return new ResultResponse("10063",false, I18nMessageService.getMsg(10063, lang));
			}
		}

		return new ResultResponse(true, "ok");
	}

	/**
	 * 用户个人中心
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/userCenter")
	public ResultResponse userCenter(HttpServletRequest request)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);
		try {
			Map<String, Object> map=userService.userCenter(userId);
			return new ResultResponse(true, map);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}

	/**
	 * 获取我邀请的用户列表
	 * @return
	 * @throws VoException
	 */
	@GetMapping("/inviteList")
	public ResultResponse inviteList(HttpServletRequest request, PageParam pageParam)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		PageInfo<InviteUserVo> pageInfo = userService.inviteList(Integer.valueOf(userId), pageParam);
		return new ResultResponse(true, pageInfo);
	}
//	//详情: 记录
//	@PostMapping("getInviteUserList")
//	public ResultResponse getInviteUserList(@RequestBody UserInviteParam userInviteParam) {
//		PageInfo<UserInviteVo> userList = userService.inviteList(userInviteParam);
//		return new ResultResponse(true, userList);
//	}

	/**
	 * 我的邀请 APP
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/myInvate")
	public ResultResponse myInvate(HttpServletRequest request)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);
		try {
			Map<String, Object> map=userService.myInvate(userId);
			return new ResultResponse(true, map);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

	}

	/**
	 * 忘记密码手机
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/forgetPasswordPhone")
	public ResultResponse forgetPasswordPhone(@RequestBody @Valid ForgetPasswordPhoneVO vo, BindingResult result, HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);

		if(!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member user=userService.selectByPhone(vo.getPhone());

		if(user!=null) {
			String password= MD5.encrypt(vo.getPassword());
			if(password.equals(user.getTransactionPassword())) {
				return new ResultResponse(false, I18nMessageService.getMsg(10063, lang));
			}

			boolean b=smsService.checkSms(request,vo.getPhone(),vo.getCode(),lang);
			if(b) {
				int flag=userService.forgetPassword(user.getId(),vo.getPassword());
				if(flag>0) {
					return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
				}else {
					return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
				}
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10007, lang));
			}
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10062, lang));
		}
	}

	/**
	 * 忘记密码邮箱
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/forgetPasswordEmail")
	public ResultResponse forgetPasswordEmail(@RequestBody @Valid ForgetPasswordEmailVO vo, BindingResult result, HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);

		if(StringUtils.isBlank(vo.getEmailCode())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10018, lang));
		}

		if(!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}


		Member user=userService.selectByEmail(vo.getEmail());
		if(user!=null) {
			String password=MD5.encrypt(vo.getPassword());
			if(password.equals(user.getTransactionPassword())) {
				return new ResultResponse(false, I18nMessageService.getMsg(10063, lang));
			}

			boolean b=emailService.checkEmailCode(vo.getEmailCode(), vo.getEmail());
			if(b) {
				int flag=userService.forgetPassword(user.getId(),vo.getPassword());
				if(flag>0) {
					return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
				}else {
					return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
				}
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10019, lang));
			}

		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10062, lang));
		}
	}

	/**
	 * 忘记密码 APP
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/forgetPasswordApp")
	public ResultResponse forgetPasswordApp(@RequestBody @Valid ForgetPasswordAppVO vo, BindingResult result, HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);

		if(!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member user=userService.selectByPhone(vo.getUsername());
		if(user!=null) {
            if (vo.getType() == 1){//短信验证码
                boolean trueSmsCode = smsService.checkNewlyCode(request,user.getPhone(), vo.getCode(),lang);
                if (!trueSmsCode) {
                    return new ResultResponse(false, I18nMessageService.getMsg(10034, lang));
                }
            } else if (vo.getType() == 2) {//邮箱验证码
                boolean trueEmailCode = emailService.checkNewlyCode(vo.getCode(), user.getEmail());
                if (!trueEmailCode) {
                    return new ResultResponse(false, I18nMessageService.getMsg(10034, lang));
                }
            }

			String password=MD5.encrypt(vo.getPassword());
			if(password.equals(user.getTransactionPassword())) {
				return new ResultResponse(false, I18nMessageService.getMsg(10063, lang));
			}

			int flag=userService.forgetPassword(user.getId(),vo.getPassword());
			if(flag>0) {
				return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
			}
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10062, lang));
		}
	}

	/**
	 * 忘记密码 邮箱 跳转
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/forgetPasswordEmailUserUrl")
	public ResultResponse forgetPasswordEmailUserUrl(@RequestBody @Valid ForgetPasswordEmailVO vo, BindingResult result, HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);

		if(!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member user=userService.selectByPhone(vo.getEmail());
		if(user!=null) {
			String password= MD5.encrypt(vo.getPassword());
			if(password.equals(user.getTransactionPassword())) {
				return new ResultResponse(false, I18nMessageService.getMsg(10063, lang));
			}

			String email=emailService.checkForgetPasswordUrl(vo.getEmailCode());
			if(vo.getEmail().equals(email)) {
				int flag=userService.forgetPassword(user.getId(),vo.getPassword());
				if(flag>0) {
					return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
				}else {
					return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
				}
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10045, lang));
			}

		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10062, lang));
		}
	}


	/**
	 * 设置交易密码
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/setTransactionPassword")
	public ResultResponse setTransactionPassword(@RequestBody @Valid TransactionPasswordVO vo, BindingResult result, HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);

		if(!vo.getTransactionPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member member =userService.selectById(userId);
		String transactionPassword= MD5.encrypt(vo.getTransactionPassword());
		if(transactionPassword.equals(member.getPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10063, lang));
		}

		Member user =new Member();
		user.setId(Integer.parseInt(userId));
		user.setTransactionPassword(transactionPassword);

		int b=userService.setTransactionPassword(user);
		if(b>0) {
			return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

	}

	/**
	 * 忘记交易密码 手机
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/forgetTransactionPasswordPhone")
	public ResultResponse forgetTransactionPasswordPhone(@RequestBody @Valid ForgetTransactionPasswordPhoneVO vo, BindingResult result, HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);

		if(!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member member =userService.selectById(userId);
		if(member==null) {
			return new ResultResponse(false, I18nMessageService.getMsg(10062, lang));
		}else {
			String transactionPassword= MD5.encrypt(vo.getPassword());
			if(member.getPassword().equals(transactionPassword)) {
				return new ResultResponse(false, I18nMessageService.getMsg(10063, lang));
			}

			boolean flag=smsService.checkSms(request,member.getPhone(),vo.getCheckCode(),lang);
			if(flag) {
				Member user =new Member();
				user.setId(Integer.parseInt(userId));
				user.setTransactionPassword(transactionPassword);

				int b=userService.forgetTransactionPassword(user);
				if(b>0) {
					return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
				}else {
					return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
				}
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10007, lang));
			}
		}
	}


	/**
	 * 忘记交易密码 邮箱
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/forgetTransactionPasswordEmail")
	public ResultResponse forgetTransactionPasswordEmail(@RequestBody @Valid ForgetTransactionPasswordPhoneVO vo, BindingResult result, HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);

		if(!vo.getPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member member =userService.selectById(userId);
		if(member==null) {
			return new ResultResponse(false, I18nMessageService.getMsg(10062, lang));
		}else {
			String transactionPassword= MD5.encrypt(vo.getPassword());
			if(member.getPassword().equals(transactionPassword)) {
				return new ResultResponse(false, I18nMessageService.getMsg(10063, lang));
			}

			boolean flag=emailService.checkEmailCode(vo.getCheckCode(), member.getEmail());
			if(flag) {
				Member user =new Member();
				user.setId(Integer.parseInt(userId));
				user.setTransactionPassword(transactionPassword);

				int b=userService.forgetTransactionPassword(user);
				if(b>0) {
					return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
				}else {
					return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
				}
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10019, lang));
			}
		}
	}

	/**
	 * 修改交易密码
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/updateTransactionPassword")
	public ResultResponse updateTransactionPassword(@RequestBody @Valid UpdateTransactionPasswordVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);
		if(!vo.getNewPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member member =userService.selectById(userId);
		String newPassWord=MD5.encrypt(vo.getNewPassword());

		if(newPassWord.equals(member.getPassword())) {
			return new ResultResponse("10063",false, I18nMessageService.getMsg(10063, lang));
		}

		Date now=new Date();
		if(member.getPhoneStatus()==1&&now.getTime()-member.getRegisterTime().getTime()>180000) {
			if("".equals(vo.getSmsCode())) {
				return new ResultResponse("10031",false, I18nMessageService.getMsg(10031, lang));
			}else {
				boolean b=smsService.checkSms(request,member.getPhone(),vo.getSmsCode(),lang);
				if(!b) {
					return new ResultResponse("10007",false, I18nMessageService.getMsg(10007, lang));
				}
			}
		}

		if(member.getEmailStatus()==1) {
			if("".equals(vo.getEmailCode())) {
				return new ResultResponse("10018",false, I18nMessageService.getMsg(10018, lang));
			}else {
				boolean b=emailService.checkEmailCode(vo.getEmailCode(),member.getEmail());
				if(!b) {
					return new ResultResponse("10019",false, I18nMessageService.getMsg(10019, lang));
				}
			}

		}

		if(member.getGoogleStatus()==1) {
			if(vo.getGoogleCode()==null) {
				return new ResultResponse("10032",false, I18nMessageService.getMsg(10032, lang));
			}else {
				long time = System.currentTimeMillis();
				GoogleAuthenticator ga = new GoogleAuthenticator();
				ga.setWindowSize(5);
				boolean b=ga.checkCode(member.getGoogleKey(),vo.getGoogleCode(),time);
				if(!b) {
					return new ResultResponse("10104",false, I18nMessageService.getMsg(10104, lang));
				}
			}
		}

		Member user =new Member();
		user.setId(Integer.parseInt(userId));
		user.setTransactionPassword(newPassWord);
		int b=userService.updateTransactionPassword(user,vo.getOldPassword(),lang);
		if(b>0) {
			return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

	}

	/**
	 * 修改登陆密码
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/updateLoginPassword")
	public ResultResponse updateLoginPassword(@RequestBody @Valid UpdateLoginPasswordVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);
		if(!vo.getNewPassword().equals(vo.getRepeatPassword())) {
			return new ResultResponse(false, I18nMessageService.getMsg(10006, lang));
		}

		Member member =userService.selectById(userId);

		String newPassWord= MD5.encrypt(vo.getNewPassword());
		if(newPassWord.equals(member.getTransactionPassword())) {
			return new ResultResponse("10063",false, I18nMessageService.getMsg(10063, lang));
		}

		Date now=new Date();
		if(member.getPhoneStatus()==1&&now.getTime()-member.getRegisterTime().getTime()>180000) {
			if("".equals(vo.getSmsCode())) {
				return new ResultResponse("10031",false, I18nMessageService.getMsg(10031, lang));
			}else {
				boolean b=smsService.checkSms(request,member.getPhone(),vo.getSmsCode(),lang);
				if(!b) {
					return new ResultResponse("10007",false, I18nMessageService.getMsg(10007, lang));
				}
			}
		}

		if(member.getEmailStatus()==1) {
			if("".equals(vo.getEmailCode())) {
				return new ResultResponse("10018",false, I18nMessageService.getMsg(10018, lang));
			}else {
				boolean b=emailService.checkEmailCode(vo.getEmailCode(),member.getEmail());
				if(!b) {
					return new ResultResponse("10019",false, I18nMessageService.getMsg(10019, lang));
				}
			}

		}

		if(member.getGoogleStatus()==1) {
			if(vo.getGoogleCode()==null) {
				return new ResultResponse("10032",false, I18nMessageService.getMsg(10032, lang));
			}else {
				long time = System.currentTimeMillis();
				GoogleAuthenticator ga = new GoogleAuthenticator();
				ga.setWindowSize(5);
				boolean b=ga.checkCode(member.getGoogleKey(),vo.getGoogleCode(),time);
				if(!b) {
					return new ResultResponse("10104",false, I18nMessageService.getMsg(10104, lang));
				}
			}
		}

		Member user =new Member();
		user.setId(Integer.parseInt(userId));
		user.setPassword(newPassWord);
		int b=userService.updateLoginPassword(user,vo.getOldPassword(),lang);
		if(b>0) {
			return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}

	}


	/**
	 * 绑定邮箱
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/bindEmail")
	public ResultResponse bindEmail(@RequestBody @Valid BindEmailVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);

		boolean flag = userService.checkExist(vo.getEmail());
		if(!flag) {
			Member user =new Member();
			user.setId(Integer.parseInt(userId));
			user.setEmail(vo.getEmail());
			user.setEmailStatus(1);

			boolean b=userService.bindEmail(user,vo.getCode(),lang);
			if(b) {
				return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
			}
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10014, lang));
		}
	}


	/**
	 * 绑定手机
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/bindPhone")
	public ResultResponse bindPhone(@RequestBody @Valid BindPhoneVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);

		boolean flag = userService.checkExist(vo.getPhone());

		if(!flag) {
			Member user =new Member();
			user.setId(Integer.parseInt(userId));
			user.setPhone(vo.getPhone());
			user.setPhoneStatus(1);

			boolean b=userService.bindPhone(request,user,vo.getCode(),lang);
			if(b) {
				return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
			}

		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10015, lang));
		}
	}

	/**
	 * 修改手机
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/updatePhone")
	public ResultResponse updatePhone(@RequestBody @Valid BindPhoneVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);

		Member member =userService.selectById(userId);

		Date now=new Date();
		if(member.getPhoneStatus()==1&&now.getTime()-member.getRegisterTime().getTime()>180000) {
			if("".equals(vo.getCode())) {
				return new ResultResponse("10031",false, I18nMessageService.getMsg(10031, lang));
			}else {
				boolean b=smsService.checkSms(request,member.getPhone(),vo.getCode(),lang);
				if(!b) {
					return new ResultResponse("10007",false, I18nMessageService.getMsg(10007, lang));
				}
			}
		}

		if(member.getEmailStatus()==1) {
			if("".equals(vo.getEmailCode())) {
				return new ResultResponse("10018",false, I18nMessageService.getMsg(10018, lang));
			}else {
				boolean b=emailService.checkEmailCode(vo.getEmailCode(),member.getEmail());
				if(!b) {
					return new ResultResponse("10019",false, I18nMessageService.getMsg(10019, lang));
				}
			}

		}

		if(member.getGoogleStatus()==1) {
			if(vo.getGoogleCode()==null) {
				return new ResultResponse("10032",false, I18nMessageService.getMsg(10032, lang));
			}else {
				long time = System.currentTimeMillis();
				GoogleAuthenticator ga = new GoogleAuthenticator();
				ga.setWindowSize(5);
				boolean b=ga.checkCode(member.getGoogleKey(),vo.getGoogleCode(),time);
				if(!b) {
					return new ResultResponse("10104",false, I18nMessageService.getMsg(10104, lang));
				}
			}
		}

		boolean flag = userService.checkExist(vo.getPhone());

		if(!flag) {
			Member user =new Member();
			user.setId(Integer.parseInt(userId));
			user.setPhone(vo.getPhone());

			boolean b=userService.updatePhone(user,lang);
			if(b) {
				return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
			}else {
				return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
			}

		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10015, lang));
		}
	}

	/**
	 * 获取谷歌验证key和二维码
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/getGoogleQRBarcodeUrl")
	public ResultResponse bindGoogleAuthenticator(HttpServletRequest request)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);

		Member member=userService.selectById(userId);
		String username="";
		if(member==null) {
			return new ResultResponse(false, I18nMessageService.getMsg(10062, lang));
		}
		if(member.getRegisterType()==1) {
			if(member.getPhone().startsWith("0086")) {
				username=member.getPhone().replace("0086","");
			}
		}else if(member.getRegisterType()==2) {
			username=member.getEmail();
		}
		String secret=GoogleAuthenticator.generateSecretKey();
		String QRBarcodeUrl=GoogleAuthenticator.getQRBarcode(username,secret);
		Map<String, Object> map=new HashMap<>();
		map.put("secret",secret);
		map.put("QRBarcodeUrl", QRBarcodeUrl);

		return new ResultResponse(true, map);
	}

	/**
	 * 绑定或修改谷歌验证
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/bindGoogleCode")
	public ResultResponse bindGoogleCode(@RequestBody @Valid GoogleAuthenVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);
		boolean success = false;
		String msg = I18nMessageService.getMsg(10101, lang);

		VoVailder.valid(result);
		long time = System.currentTimeMillis();
		GoogleAuthenticator ga = new GoogleAuthenticator();
		ga.setWindowSize(5);
		boolean b=ga.checkCode(vo.getGoogleKey(),vo.getGoogleCode(),time);

		if(b) {
			Member user=new Member();
			user.setGoogleKey(vo.getGoogleKey());
			user.setId(Integer.parseInt(userId));
			user.setGoogleStatus(1);
			boolean flag=userService.bindGoogleCode(user);
			if(flag) {
				success=true;
				msg=I18nMessageService.getMsg(10103, lang);
			}

		}else {
			msg=I18nMessageService.getMsg(10104, lang);
		}
		return new ResultResponse(success, msg);
	}


	/**
	 * 身份认证
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/identityAuthen")
	public ResultResponse identityAuthen(@RequestBody @Valid IdentityAuthenVO vo, BindingResult result,HttpServletRequest request)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);
		VoVailder.valid(result);

		//判断身份证号是否已经存在
		boolean existByIdNo = userService.existByIdNo(vo.getCardNo());
		if (existByIdNo) {
			return new ResultResponse(false, I18nMessageService.getMsg(10113, lang));
		}

		Member user=new Member();
		user.setId(Integer.parseInt(userId));
		user.setCheckStatus(1);
		user.setIdName(vo.getIdName());
		user.setIdImageA(vo.getIdImageA());
		user.setIdImageB(vo.getIdImageB());
		user.setVerificationType(vo.getCardType());
		user.setCountry(vo.getCountry());
		user.setIdNo(vo.getCardNo());

		int count=userService.identityAuthen(user);
		if(count>0) {
			return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}


	/**
	 * 查询审核结果
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/identityResult")
	public ResultResponse identityResult(HttpServletRequest request)throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);

		Member user=userService.selectById(userId);
		Map<String, Object> map=new HashMap<>();

		if(user==null) {
			return new ResultResponse(false, I18nMessageService.getMsg(10013, lang));
		}else {
			map.put("checkStatus", user.getCheckStatus());
			if(user.getCheckStatus()==2) {
				map.put("country", user.getCountry());
				map.put("name", user.getIdName());
				map.put("cardType", user.getVerificationType());
				map.put("idNo", user.getIdNo());
			}else if(user.getCheckStatus()==-1) {
				String reason=userService.selectFailReasonByUserId(Integer.parseInt(userId));
				map.put("reason", reason);
			}
			return new ResultResponse(true, map);
		}
	}


	/**
	 * 开启或关闭交易密码
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/openOrCloseTradePassword")
	public ResultResponse openOrCloseTradePassword(@RequestBody OpenTradePasswordVO vo,HttpServletRequest request)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);

		if(vo.getFlag()==null) {
			return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
		}
		Member member =userService.selectById(userId);
		Date now=new Date();
		if(member.getPhoneStatus()==1&&now.getTime()-member.getRegisterTime().getTime()>180000) {
			if("".equals(vo.getSmsCode())) {
				return new ResultResponse("10031",false, I18nMessageService.getMsg(10031, lang));
			}else {
				boolean b=smsService.checkSms(request,member.getPhone(),vo.getSmsCode(),lang);
				if(!b) {
					return new ResultResponse("10007",false, I18nMessageService.getMsg(10007, lang));
				}
			}
		}

		if(member.getEmailStatus()==1) {
			if("".equals(vo.getEmailCode())) {
				return new ResultResponse("10018",false, I18nMessageService.getMsg(10018, lang));
			}else {
				boolean b=emailService.checkEmailCode(vo.getEmailCode(),member.getEmail());
				if(!b) {
					return new ResultResponse("10019",false, I18nMessageService.getMsg(10019, lang));
				}
			}

		}

		if(member.getGoogleStatus()==1) {
			if(vo.getGoogleCode()==null) {
				return new ResultResponse("10032",false, I18nMessageService.getMsg(10032, lang));
			}else {
				long time = System.currentTimeMillis();
				GoogleAuthenticator ga = new GoogleAuthenticator();
				ga.setWindowSize(5);
				boolean b=ga.checkCode(member.getGoogleKey(),vo.getGoogleCode(),time);
				if(!b) {
					return new ResultResponse("10104",false, I18nMessageService.getMsg(10104, lang));
				}
			}
		}

		Member user=new Member();
		user.setNeedTransactionPassword(vo.getFlag());
		user.setId(Integer.parseInt(userId));

		int count=userService.updateByPrimaryKeySelective(user);
		if(count>0) {
			return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}


	/**
	 * 切换安全验证
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/switchCheck")
	public ResultResponse switchCheck(@RequestBody @Valid CloseCheckVO vo,BindingResult result,HttpServletRequest request)throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);

		Member member =userService.selectById(userId);
		Date now=new Date();

		if(member.getPhoneStatus()==1&&now.getTime()-member.getRegisterTime().getTime()>180000) {
			if(StringUtils.isBlank(vo.getSmsCode())) {
				return new ResultResponse("10031",false, I18nMessageService.getMsg(10031, lang));
			}else {
				boolean b=smsService.checkSms(request,member.getPhone(),vo.getSmsCode(),lang);
				if(!b) {
					return new ResultResponse("10007",false, I18nMessageService.getMsg(10007, lang));
				}
			}
		}

		if(member.getEmailStatus()==1) {
			if(StringUtils.isBlank(vo.getEmailCode())) {
				return new ResultResponse("10018",false, I18nMessageService.getMsg(10018, lang));
			}else {
				boolean b=emailService.checkEmailCode(vo.getEmailCode(),member.getEmail());
				if(!b) {
					return new ResultResponse("10019",false, I18nMessageService.getMsg(10019, lang));
				}
			}

		}

		if(member.getGoogleStatus()==1) {
			if(vo.getGoogleCode()==null) {
				return new ResultResponse("10032",false, I18nMessageService.getMsg(10032, lang));
			}else {
				long time = System.currentTimeMillis();
				GoogleAuthenticator ga = new GoogleAuthenticator();
				ga.setWindowSize(5);
				boolean b=ga.checkCode(member.getGoogleKey(),vo.getGoogleCode(),time);
				if(!b) {
					return new ResultResponse("10104",false, I18nMessageService.getMsg(10104, lang));
				}
			}
		}

		Member user=new Member();
		user.setId(Integer.parseInt(userId));

		if(vo.getType()==1) {
			if(member.getEmailStatus()!=1&&member.getGoogleStatus()!=1&&vo.getStatus()==0) {
				return new ResultResponse(false, I18nMessageService.getMsg(10030, lang));
			}else {
				user.setPhoneStatus(vo.getStatus());
			}
		}else if(vo.getType()==2) {
			if(member.getPhoneStatus()!=1&&member.getGoogleStatus()!=1&&vo.getStatus()==0) {
				return new ResultResponse(false, I18nMessageService.getMsg(10030, lang));
			}else {
				user.setEmailStatus(vo.getStatus());
			}
		}else if(vo.getType()==3){
			if(member.getPhoneStatus()!=1&&member.getEmailStatus()!=1&&vo.getStatus()==0) {
				return new ResultResponse(false, I18nMessageService.getMsg(10030, lang));
			}else {
				if(StringUtils.isNotBlank(user.getGoogleKey())){//坑：用户没绑定谷歌，但app提交type=3 进行开关（导致用户没绑谷歌就开启了谷歌验证
					user.setGoogleStatus(vo.getStatus());
				} else{
					user.setGoogleStatus(0);
				}

			}
		}

		int count=userService.updateByPrimaryKeySelective(user);
		if(count>0) {
			return new ResultResponse(true, I18nMessageService.getMsg(10103, lang));
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
	}


	/**
	 * 登陆
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/login")
	public ResultResponse login(@RequestBody @Valid UserLoginVO vo, BindingResult result, HttpServletRequest request) throws VoException {
		VoVailder.valid(result);
		String lang = request.getHeader(Constant.api_header_lang);

		// 用户登录
		Member user=userService.login(request,vo.getUsername(), vo.getPassword(),lang);
		if(user.getLevel()==0) {
			return new ResultResponse("10020",false, I18nMessageService.getMsg(10020, lang));
		}

		if(user.getLockStatus()==1) {
			return new ResultResponse(false, I18nMessageService.getMsg(10021, lang));
		}

		Integer userId = user.getId();

		if(user.getPhoneStatus()==1&&vo.getType()==1&&user.getGoogleStatus()!=1) {
			if(StringUtils.isBlank(vo.getSmsCode())) {
				if(vo.getSource()==1) {
					Date now=new Date();
					if(now.getTime()-user.getRegisterTime().getTime()>180000) {
						return new ResultResponse(false, I18nMessageService.getMsg(10031, lang));
					}
				}else {
					Date now=new Date();
					if(now.getTime()-user.getRegisterTime().getTime()>180000) {
						return new ResultResponse("2","");
					}
				}
			}else {
				boolean b=smsService.checkSms(request,vo.getUsername(), vo.getSmsCode(),lang);
				if(!b) {
					return new ResultResponse(false, I18nMessageService.getMsg(10007, lang));
				}
			}
		}else if(user.getGoogleStatus()==1) {
			if(vo.getGoogleCode()==null) {
				if(vo.getSource()==1) {
					return new ResultResponse(false, I18nMessageService.getMsg(10032, lang));
				}else {
					return new ResultResponse("3","");
				}
			}else {
				long time = System.currentTimeMillis();
				GoogleAuthenticator ga = new GoogleAuthenticator();
				ga.setWindowSize(5);
				boolean b=ga.checkCode(user.getGoogleKey(),vo.getGoogleCode(),time);
				if(!b) {
					return new ResultResponse(false, I18nMessageService.getMsg(10104, lang));
				}
			}
		}

		// 生成token并存入redis
		String accessToken = tokenManager.create(userId,vo.getSource());
		if(StringUtils.isNotBlank(accessToken)) {
			ThreadManager.getThreadPollProxy().execute(()->userService.addLoginLog(request,user,vo.getSource(),lang));
		}

		Map<String, Object> map=new HashMap<>();
		Integer level=user.getLevel();
		String name=user.getIdName();
		Integer checkStatus=user.getCheckStatus();

		map.put("accessToken", accessToken);
		map.put("userId", userId);
		map.put("level", level);
		map.put("checkStatus", checkStatus);

		if(StringUtils.isNotBlank(name)&&checkStatus==2) {
			map.put("name", name);
		}else {
			if(user.getRegisterType()==1) {
				map.put("name", user.getPhone());
			}else {
				map.put("name", user.getEmail());
			}
		}
		map.put("phone", user.getPhone());
		map.put("email", user.getEmail());
		map.put("source", user.getSource());
		map.put("registerType", user.getRegisterType());
		map.put("merchant", user.getMerchant());
		return new ResultResponse(true, map);
	}

	/**
	 * 登陆 压测
	 * @return
	 * @throws VoException
	 */
	/*@RequestMapping("/login")
	public ResultResponse loginTest(@RequestBody @Valid UserLoginVO vo, BindingResult result, HttpServletRequest request) throws VoException {
		VoVailder.valid(result);
		String lang = request.getHeader(Constant.api_header_lang);

		// 用户登录
		Member user=userService.login(vo.getUsername(), vo.getPassword(),lang);
		if(user.getLevel()==0) {
			return new ResultResponse("10020",false, I18nMessageService.getMsg(10020, lang));
		}

		if(user.getLockStatus()==1) {
			return new ResultResponse(false, I18nMessageService.getMsg(10021, lang));
		}

		Integer userId = user.getId();

		// 生成token并存入redis
		String accessToken = tokenManager.createTest(userId);
		if(StringUtils.isNotBlank(accessToken)) {
			userService.addLoginLog(request,user,vo.getSource(),lang);
		}

		Map<String, Object> map=new HashMap<>();
		Integer level=user.getLevel();
		String name=user.getIdName();
		Integer checkStatus=user.getCheckStatus();

		map.put("accessToken", accessToken);
		map.put("userId", userId);
		map.put("level", level);
		map.put("checkStatus", checkStatus);

		if(StringUtils.isNotBlank(name)&&checkStatus==2) {
			map.put("name", name);
		}else {
			if(user.getRegisterType()==1) {
				map.put("name", user.getPhone());
			}else {
				map.put("name", user.getEmail());
			}
		}
		map.put("phone", user.getPhone());
		map.put("email", user.getEmail());
		map.put("source", user.getSource());
		map.put("registerType", user.getRegisterType());

		return new ResultResponse(true, map);
	}*/

	/**
	 * 注销
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/logout")
	public ResultResponse logout(HttpServletRequest request) {
		Integer userId = Integer.parseInt(request.getHeader(Constant.api_header_userId));
		String lang = request.getHeader(Constant.api_header_lang);

		userService.logout(userId);

		String msg = I18nMessageService.getMsg(10008, lang);
		return new ResultResponse(true, msg);
	}
}
