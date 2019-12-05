package com.oax.controller;

import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.common.SMSUtil;
import com.oax.context.HttpContext;
import com.oax.entity.front.EmailCaptcha;
import com.oax.entity.front.Member;
import com.oax.entity.front.SmsCaptcha;
import com.oax.service.EmailCaptchaService;
import com.oax.service.SmsCaptchaService;
import com.oax.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送验证码
 */
@RestController
@RequestMapping("/verificationCode")
public class VerificationCodeController {
    @Autowired
    private SmsCaptchaService smsService;
    @Autowired
    private UserService userService;
    @Autowired
    private com.oax.service.I18nMessageService I18nMessageService;
    @Autowired
    private EmailCaptchaService emailService;

    /**
     * 发送短信验证码给当前用户
     */
    @RequestMapping("/sms")
    public ResultResponse sendSms() {
        Integer userId = HttpContext.getUserId();
        Member user = userService.selectById(userId+"");
        String phoneTemplate = user.getPhone();
        String smsCode= SMSUtil.getSmsCode(6);
        String templateParam = "您的验证码是 " + smsCode;
        if(phoneTemplate.startsWith("0086")) {
            phoneTemplate = phoneTemplate.substring(4);
        }
        String code = SMSUtil.sendCode(phoneTemplate, templateParam);
        if(("Sucess").equals(code)) {
            SmsCaptcha smsEntity=new SmsCaptcha();
            smsEntity.setPhone(user.getPhone());
            smsEntity.setCode(smsCode);
            Date date=new Date();
            smsEntity.setCreateTime(date);
            smsEntity.setExpireTime(new Date(date.getTime() + + Constant.CODE_OUT_LIMIT1));
            smsService.add(smsEntity);

            return new ResultResponse(true, "发送成功");
        }else {
            return new ResultResponse(false, "发送失败");
        }
    }

    /**
     * 发送邮箱验证码
     */
    @RequestMapping("/email")
    public ResultResponse sendCodeByUserId() {
        String userId = HttpContext.getUserId() + "";
        String lang = HttpContext.getLang();
        Member user = userService.selectById(userId);
        if (user.getEmailStatus() != 1) {
            return new ResultResponse(false, "未验证邮箱");
        }
        try {
            String emailCode = SMSUtil.getSmsCode(6);
            String str = emailService.sendEmailCode(user.getEmail(),emailCode,lang);
            if(StringUtils.isNotBlank(str)) {
                EmailCaptcha emailEntity=new EmailCaptcha();
                emailEntity.setToEmail(user.getEmail());
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
}
