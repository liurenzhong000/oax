package com.oax.service.impl;

import java.util.Calendar;
import java.util.Date;

import com.oax.common.EmailAPI;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.oax.common.EmailUtil;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.EmailCaptcha;
import com.oax.entity.front.Member;
import com.oax.exception.VoException;
import com.oax.mapper.front.EmailCaptchaMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.service.EmailCaptchaService;
import com.oax.service.I18nMessageService;

@Service
public class EmailCaptchaServiceImpl implements EmailCaptchaService {
	private final static Logger logger = Logger.getLogger(EmailCaptchaServiceImpl.class);
	
	@Autowired
	private EmailCaptchaMapper emailCaptchaDao;
	
	@Autowired
	private MemberMapper memberDao;
	
	@Value("${register.checkUrl}")
	private String registerCheckUrl;
	
	@Value("${forgotPassword.checkUrl}")
	private String forgotPasswordCheckUrl;
	
	@Autowired
	private I18nMessageService I18nMessageService;
	
	
	@Override
	@DataSource(DataSourceType.MASTER)
	public void add(EmailCaptcha emailEntity) {
		emailCaptchaDao.insertSelective(emailEntity);
		
	}
	
	
	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean checkEmail(String emailCode) {
		String email=selectEmailByCode(emailCode);
		if(StringUtils.isBlank(email)) {
			return false;
		}else {
			Member member=memberDao.selectByPhoneOrEmail(email);
			if(member!=null&&member.getLevel()==0) {
				Member user=new Member();
				user.setId(member.getId());
				user.setLevel(1);
				int count=memberDao.updateByPrimaryKeySelective(user);
				if(count>0) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
			
		}
		
	}
	
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public String selectEmailByCode(String emailCode) {
		String email=emailCaptchaDao.selectEmailByCode(emailCode);
		return email;
	}

	
	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean checkEmailCode(String emailCode, String email)throws VoException{
		try {
			EmailCaptcha emailCaptcha=emailCaptchaDao.selectByEmail(email);
			if(emailCaptcha!=null) {
				String message=emailCaptcha.getCode();
				if(emailCode.equals(message)) {
					EmailCaptcha emailCap=new EmailCaptcha();
					
					Calendar now = Calendar.getInstance();
					now.add(Calendar.MINUTE, 1);// 1分钟之后的时间
					Date expireTime = now.getTime();
					
					emailCap.setId(emailCaptcha.getId());
					emailCap.setExpireTime(expireTime);
					emailCaptchaDao.updateByPrimaryKeySelective(emailCap);
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		} catch (Exception e) {
			throw new VoException("emailCode is wrong");
		}
		
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean checkEmailCodeOnce(String emailCode, String email)throws VoException{
		try {
			EmailCaptcha emailCaptcha=emailCaptchaDao.selectByEmail(email);
			if(emailCaptcha!=null) {
				String message=emailCaptcha.getCode();
				if(emailCode.equals(message)) {
					EmailCaptcha emailCap=new EmailCaptcha();
					Date expireTime = new Date();
					emailCap.setId(emailCaptcha.getId());
					emailCap.setExpireTime(expireTime);
					emailCaptchaDao.updateByPrimaryKeySelective(emailCap);
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		} catch (Exception e) {
			throw new VoException("emailCode is wrong");
		}

	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean checkNewlyCode(String emailCode, String email)throws VoException{
		try {
			EmailCaptcha emailCaptcha=emailCaptchaDao.selectByEmailAndCode(email, emailCode);
			if(emailCaptcha!=null) {
				String message=emailCaptcha.getCode();
				if(emailCode.equals(message)) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		} catch (Exception e) {
			throw new VoException("emailCode is wrong");
		}

	}

	@Override
	@Async
	public String sendRegisterUrl(String email,String emailCode, String lang) {
		String str0="";
		String str1="";
		String str2="";
		String str3="";
		String str4="";
		String str5="";
		String str6="";
		String str7="";
		String str8="";
		if("cn".equals(lang)) {
			str0="欢迎加入xbtc!";
			str1="亲爱的用户，你好！";
			str2="你的登陆邮箱是：";
			str3="请点击以下链接验证你的邮箱地址!";
			str4="如果以上链接无法访问，请将该网址复制并粘贴至其它浏览器窗口中。";
			str5="祝您生活愉快，工作顺利！";
			str6="xbtc团队";
			str7="系统邮件，请勿回复！";
			str8="XBTC注册";
			
		}else {
			str0="welcome to join xbtc!";
			str1="Dear user, hello!";
			str2="your login address is:";
			str3="please click the following link to verify your email address!";
			str4="if the above link cannot be accessed, please copy and paste the url into another browser";
			str5="wish you a happy life and a smooth work!";
			str6="xbtc team";
			str7="system mail, please do not reply!";
			str8="XBTC REGISTER";
			
		}
		
		StringBuilder content=new StringBuilder();
		
		content.append("<table align='center' cellpadding='0' cellspacing='0' style='border-collapse: collapse; border:1px solid #e5e5e5;box-shadow: 0 10px 15px rgba(0, 0, 0, 0.05);text-align: left; width:800px;'>");
		content.append("<tbody>");
			   content.append("<tr>");
			   content.append("<td style='padding:20px 0 10px 0;text-indent:25px;'>");
			   content.append("<img src='javascript:;' style='vertical-align: middle;'>");
			   content.append("</td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'><b>"+str0+"</b></td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;line-height: 10px;'>");
			   content.append("<p>"+str1+"</p>");
			   content.append("<p>"+str2+"<b><a href='mailto:"+email+"' target='_blank'>"+email+"</a></b></p>");
			   content.append("</td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'>"+str3+" </td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'><a style='color:#1e88e5;text-decoration: none;' href='"+registerCheckUrl+"?checkCode="+emailCode+"' target='_blank'>"+registerCheckUrl+"?checkCode="+emailCode+"</a></td>");
			   content.append("</tr>");
			   content.append("</br>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'>"+str4+"</td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0 20px 0;text-indent:25px;line-height: 10px;'>");
			   content.append("<p>"+str5+"</p>");
			   content.append("<p>"+str6+"</p>");
			   content.append("<p>"+str7+"</p>");
			   content.append("</td>");
			   content.append("</tr>");
			   content.append("</tbody>");
			   content.append("</table>");
		String str=EmailUtil.sendHtmlMail(email,str8,content.toString(), I18nMessageService.getMsg(10101, lang), true);
		logger.info("---------------------------------------------------"+str+"----------------------------------------");
		return str;
	}


	@Override
	@Async
	public String sendForgetPasswordUrl(String email,String emailCode, String lang) {
		String str0="";
		String str1="";
		String str2="";
		String str3="";
		String str4="";
		String str5="";
		String str6="";
		String str7="";
		String str8="";
		String str9="";
		if("cn".equals(lang)) {
			str0="重置密码";
			str1="亲爱的用户，你好！";
			str2="您正在对XBTC账户：";
			str3="发起重置密码的申请,如果您要设置一个新密码，请点击以下链接。";
			str4="如果以上链接无法访问，请将该网址复制并粘贴至其它浏览器窗口中。";
			str5="为保障账号安全性，该链接仅在30分钟内有效。";
			str6="祝您生活愉快，工作顺利！";
			str7="xbtc团队";
			str8="系统邮件，请勿回复！";
			str9="XBTC忘记密码";
			
		}else {
			str0="reset your password";
			str1="Dear user, hello!";
			str2="you are in the XBTC account:";
			str3="to initiate an application to reset your password, click the following url if you want to set a new password.";
			str4="if the above link cannot be accessed, please copy and paste the url into another browser";
			str5="to ensure account security, this link only works within 30 minutes.";
			str6="wish you a happy life and a smooth work!";
			str7="xbtc team";
			str8="system mail, please do not reply!";
			str9="XBTC FORGETPASSWORD";
			
		}
				
		StringBuilder content=new StringBuilder();			   			   
			   content.append("<table align='center' cellpadding='0' cellspacing='0' style='border-collapse: collapse; border:1px solid #e5e5e5;box-shadow: 0 10px 15px rgba(0, 0, 0, 0.05);text-align: left; width:900px;'>");
			   content.append("<tbody>");
			   content.append("<tr>");
			   content.append("<td style='padding:20px 0 10px 0;text-indent:25px;'>");
			   content.append("<img src='javascript:;' style='vertical-align: middle;'>");
			   content.append("</td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'><b>"+str0+"</b></td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;line-height: 10px;'>");
			   content.append("<p>"+str1+"</p>");
			   content.append("<p>"+str2+"<b><a href='mailto:"+email+"' target='_blank'>"+email+"</a></b></p>");
			   content.append("</td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'>"+str3+" </td>");
			   content.append("</tr>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'><a style='color:#1e88e5;text-decoration: none;' href='"+forgotPasswordCheckUrl+"?checkCode="+emailCode+"&email="+email+"' target='_blank'>"+forgotPasswordCheckUrl+"?checkCode="+emailCode+"&email="+email+"</a></td>");
			   content.append("</tr>");
			   content.append("</br>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0;text-indent:25px;'>"+str4+"</td>");
			   content.append("</tr>");
			   content.append("<td style='padding:10px 0 20px 0;text-indent:25px;line-height: 10px;'>"+str5+"</td>");
			   content.append("<tr>");
			   content.append("<td style='padding:10px 0 20px 0;text-indent:25px;line-height: 10px;'>");
			   content.append("<p>"+str6+"</p>");
			   content.append("<p>"+str7+"</p>");
			   content.append("<p>"+str8+"</p>");
			   content.append("</td>");
			   content.append("</tr>");
			   content.append("</tbody>");
			   content.append("</table>");
		String str=EmailUtil.sendHtmlMail(email,str9,content.toString(), I18nMessageService.getMsg(10101, lang), true);
        logger.info("---------------------------------------------------"+str+"----------------------------------------");
		return str;
	}
	
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public String checkForgetPasswordUrl(String checkCode) {
		String email=emailCaptchaDao.selectEmailByCode(checkCode);		
		return email;	
	}


	@Override
	public String sendIpChangeEmail(String email, String lang) {
		StringBuilder content=new StringBuilder();		
		content.append("<div style='padding:0 30px;background:#fff;box-shadow:0 0 5px #eee;'>");
		content.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		content.append("<tbody><tr>");
		content.append("<td style='border-bottom: 1px solid #e6e6e6;font-size:18px;padding:20px 0;'>");
		content.append("<table border='0' cellspacing='0' cellpadding='0' width='100%'>");
		content.append("<tbody><tr>");
		content.append("<td>登录IP改变</td>");
		content.append("<td></td></tr>");
		content.append("</tbody></table>");
		content.append("</td></tr>");
		content.append("<tr>");
		content.append("<td style='font-size:14px;line-height:30px;padding:20px 0;color:#666;'>您好：<br>系统检测到您的账号在从未使用过的IP地址登录。<br>账户：<strong><a href='mailto:"+email+"' target='_blank'>"+email+"<wbr>.com</a></strong></td>");
		content.append(" </tr>");
		content.append("<tr>");
		content.append("<td></td></tr>");
		content.append("<tr><td style='padding:20px 0 10px 0;line-height:26px;color:#666;'>如果不是您本人操作，请立即联系XBTC客服</td></tr>");
		content.append("<tr><td style='padding:30px 0 15px 0;font-size:12px;color:#999;line-height:20px;'>XBTC团队<br>系统邮件，请勿回复</td></tr>");
		content.append("</tbody></table>");
		content.append("</div>");
    
		String str=EmailUtil.sendHtmlMail(email,"登陆ip改变 ",content.toString(),I18nMessageService.getMsg(10101, lang),true);
		return str;
	}


	@Override
	public String sendEmailCode(String email, String emailCode, String lang) {
		/*String str1="";
		String str2="";
		String str3="";
		String str4="";
		String str5="";
		String str6="";
		String str7="";*/
		String text="";
		if("cn".equals(lang)) {
			/*str1="亲爱的用户，";
			str2="您好！";
			str3="您的邮箱验证码是:";
			str4=",请妥善保管";
			str5="xbtc团队";
			str6="系统邮件，请勿回复！";
			str7="邮箱验证码";*/
			text="亲爱的用户，你好！您的邮箱验证码是:"+emailCode+",请妥善保管";
		}else {
			/*str1="Dear user, ";
			str2="hello!";
			str3="your email verification code is:";
			str4=",please keep it safe";
			str5="xbtc team";
			str6="system mail, please do not reply!";
			str7="Mailbox verification code";*/
			text="Dear user，hello! your email verification code is:"+emailCode+",please keep it safe";
		}
		
		/*StringBuilder content=new StringBuilder();
		content.append("<div id='mailContentContainer' class='qmbox qm_con_body_content qqmail_webmail_only'>");
		content.append("<table width='700' border='0' align='center' cellspacing='0' style='width:700px;'>");
		content.append("<tbody><tr><td>");
		content.append("<div style='width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;'>");
		content.append("<table border='0' cellpadding='0' cellspacing='0' width='700' height='39' style='font:12px Tahoma, Arial, 宋体;'>");
		content.append("<tbody> <tr><td width='210'></td></tr></tbody></table></div>");
		content.append("<div style='width:680px;padding:0 10px;margin:0 auto;'>");
		content.append(" <div style='line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;'>");
		content.append("<strong style='display:block;margin-bottom:15px;'>"+str1+"<span style='color:#f60;font-size: 16px;'></span>"+str2+"</strong>");
		content.append("<strong style='display:block;margin-bottom:15px;'>"+str3+"<span style='color:#f60;font-size: 24px'>"+emailCode+"</span>"+str4+"</strong></div>");
		content.append("<div style='margin-bottom:30px;'></div></div>");
		content.append("<div style='width:700px;margin:0 auto;'>");
		content.append("<div style='padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;'>");
		content.append("<p>"+str5+"<br>"+str6+"</p>");
		content.append("</div>");
		content.append("</div>");
		content.append("</td> </tr></tbody></table>");
		content.append("<style type='text/css'>.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div>");*/
		        
		//String str=EmailUtil.sendHtmlMail(email,str7,content.toString(),I18nMessageService.getMsg(10101, lang),true);
		String str=EmailUtil.sendTextMail(email,"邮箱验证",text,true);
		return str;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public void resetExpireTime(String email,String emailCode) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 3);// 3分钟之后的时间
		Date expireTime = now.getTime();
		   
		EmailCaptcha emailCaptcha=emailCaptchaDao.selectByEmailAndCode(email,emailCode);
		EmailCaptcha emailCap=new EmailCaptcha();
		emailCap.setId(emailCaptcha.getId());
		emailCap.setExpireTime(expireTime);
		emailCaptchaDao.updateByPrimaryKeySelective(emailCap);	
	}

	@Async
	@Override
	public String sendEmailPwd(String email, String pwd) {
		String emailMsg = "您的账号："+email+",初始密码："+pwd+"</br>";
		emailMsg += "请登录大公牛App进行提现或发红包操作，APP下载链接：http://xbtcoin.xbtc.cx/#/app_download";
		String msg = EmailUtil.sendHtmlMail(email,"大公牛平台",emailMsg,"发送失败",true);
		return msg;
	}

	@Override
	public String sendEmailMsg(String email, String message, String lang) {
		String str1="";
		String str2="";
		String str3="";
		String str4="";
		String str5="";
		String str6="";
		String str7="";
		if("cn".equals(lang)) {
			str1="亲爱的用户，";
			str2="您好！";
			str5="xbtc团队";
			str6="系统邮件，请勿回复！";
			str7="邮箱验证码";
		}else {
			str1="Dear user, ";
			str2="hello!";
			str5="xbtc team";
			str6="system mail, please do not reply!";
			str7="Mailbox verification code";
		}

		StringBuilder content=new StringBuilder();
		content.append("<div id='mailContentContainer' class='qmbox qm_con_body_content qqmail_webmail_only'>");
		content.append("<table width='700' border='0' align='center' cellspacing='0' style='width:700px;'>");
		content.append("<tbody><tr><td>");
		content.append("<div style='width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;'>");
		content.append("<table border='0' cellpadding='0' cellspacing='0' width='700' height='39' style='font:12px Tahoma, Arial, 宋体;'>");
		content.append("<tbody> <tr><td width='210'></td></tr></tbody></table></div>");
		content.append("<div style='width:680px;padding:0 10px;margin:0 auto;'>");
		content.append(" <div style='line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;'>");
		content.append("<strong style='display:block;margin-bottom:15px;'>"+str1+"<span style='color:#f60;font-size: 16px;'></span>"+str2+"</strong>");
		content.append("<strong style='display:block;margin-bottom:15px;'>"+message+"</strong></div>");
		content.append("<div style='margin-bottom:30px;'></div></div>");
		content.append("<div style='width:700px;margin:0 auto;'>");
		content.append("<div style='padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;'>");
		content.append("<p>"+str5+"<br>"+str6+"</p>");
		content.append("</div>");
		content.append("</div>");
		content.append("</td> </tr></tbody></table>");
		content.append("<style type='text/css'>.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div>");

		String str=EmailUtil.sendHtmlMail(email,str7,content.toString(),I18nMessageService.getMsg(10101, lang),true);
		return str;
	}

}
