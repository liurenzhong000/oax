package com.oax.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.Constant;
import com.oax.common.GoogleAuthenticator;
import com.oax.common.ResultResponse;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.entity.front.Member;
import com.oax.entity.front.WithdrawAddress;
import com.oax.exception.VoException;
import com.oax.exception.VoVailder;
import com.oax.service.EmailCaptchaService;
import com.oax.service.I18nMessageService;
import com.oax.service.IWithdrawAddressService;
import com.oax.service.SmsCaptchaService;
import com.oax.service.UserCoinService;
import com.oax.service.UserService;
import com.oax.vo.WithdrawAddressListVO;

@RestController
@RequestMapping("/coinAddress")
public class CoinAddressController {

    @Autowired
    private I18nMessageService I18nMessageService;
    @Autowired
    private IWithdrawAddressService withdrawAddressService;
    
    @Autowired
	private UserService userService;
    
	@Autowired
	private SmsCaptchaService smsService;
	
	@Autowired
	private EmailCaptchaService emailService;
	
	@Autowired
	private UserCoinService userCoinService;
    
    /**
     * 提币管理地址: 地址列表 
     * @param
     * @throws VoException
     */
    @PostMapping("/list")
    public ResultResponse list(@RequestBody WithdrawAddressListVO vo,HttpServletRequest request) throws VoException{
        String userId=request.getHeader(Constant.api_header_userId);
        
        vo.setUserId(Integer.parseInt(userId));
        PageInfo<WithdrawAddress> data = withdrawAddressService.list(vo);
        return new ResultResponse(true, data);
    }
    

    /**
     * 提币管理地址: 添加按钮 保存操作
     * @param vo
     * @throws VoException
     */
    @PostMapping("/add")
    public ResultResponse add(@RequestBody @Valid WithdrawAddress vo, BindingResult result, HttpServletRequest request) throws VoException{
    	String reg="^0x([a-z]|[A-Z]|[0-9]){40}";
        VoVailder.valid(result);
        
        String lang=request.getHeader(Constant.api_header_lang);
        String userId=request.getHeader(Constant.api_header_userId);
        
        String msg="";
        boolean success=false;
        
        Member member =userService.selectById(userId);
        CoinWithBLOBs coin =userCoinService.selectCoinById(vo.getCoinId());
        
        if(coin.getType()==2) {
        	if(vo.getAddress().length()<26&&vo.getAddress().length()>34) {
        		return new ResultResponse(false, I18nMessageService.getMsg(10023, lang));
        	}
        }else if(coin.getType()==1||coin.getType()==3) {
        	if(!vo.getAddress().matches(reg)) {
        		return new ResultResponse(false, I18nMessageService.getMsg(10023, lang));
        	}
        }
        
        Date now=new Date();
		if(member.getPhoneStatus()==1&&now.getTime()-member.getRegisterTime().getTime()>180000) {
			if("".equals(vo.getSmsCode())) {
				return new ResultResponse("10031",false, I18nMessageService.getMsg(10031, lang));
			}else {
				boolean b=smsService.checkSms( request,member.getPhone(),vo.getSmsCode(),lang);
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
		
        vo.setUserId(Integer.parseInt(userId));
        vo.setCreateTime(new Date());
        vo.setUpdateTime(new Date());
        int count= withdrawAddressService.save(vo);

       if(count>0) {
    	   msg = I18nMessageService.getMsg(10005, lang);
    	   success=true;
       }else {
    	   msg = I18nMessageService.getMsg(10101, lang);
       }
        return new ResultResponse(success, msg);
    }
    
    /**
     * 提币管理地址: 删除地址
     * @param
     * @throws VoException
     */
    @RequestMapping("/deleteById/{id}")
    public ResultResponse deleteById(@PathVariable("id") Integer id,HttpServletRequest request) throws VoException{
    	String lang=request.getHeader(Constant.api_header_lang);
    	String msg="";
    	boolean success=false;

    	int count= withdrawAddressService.deleteById(id);    	
    	if(count>0) {
    		msg = I18nMessageService.getMsg(10016, lang);
    		success=true;
    	}else {
    		msg = I18nMessageService.getMsg(10017, lang);
    	}
    	return new ResultResponse(success, msg);
    }
    
    /**
     * 提币管理地址: 修改提币地址
     * @param vo
     * @throws VoException
     */
    @PostMapping("/updateById")
    public ResultResponse updateById(@RequestBody @Valid WithdrawAddress vo, BindingResult result, HttpServletRequest request) throws VoException{
        VoVailder.valid(result);
        String lang=request.getHeader(Constant.api_header_lang);
        String userId=request.getHeader(Constant.api_header_userId);
        String msg="";
        boolean success=false;
        
        Member member =userService.selectById(userId);
        Date now=new Date();
		if(member.getPhoneStatus()==1&&now.getTime()-member.getRegisterTime().getTime()>180000) {
			if("".equals(vo.getSmsCode())) {
				return new ResultResponse(false, I18nMessageService.getMsg(10031, lang));
			}else {
				boolean b=smsService.checkSms(request,vo.getSmsCode(), member.getPhone(),lang);
				if(!b) {
					return new ResultResponse(false, I18nMessageService.getMsg(10007, lang));
				}
			}			
		}
		
		if(member.getEmailStatus()==1) {
			if("".equals(vo.getEmailCode())) {
				return new ResultResponse(false, I18nMessageService.getMsg(10018, lang));
			}else {
				boolean b=emailService.checkEmailCode(vo.getEmailCode(),member.getEmail());
				if(!b) {
					return new ResultResponse(false, I18nMessageService.getMsg(10019, lang));
				}
			}
			
		}
		
		if(member.getGoogleStatus()==1) {
			if(vo.getGoogleCode()==null) {
				return new ResultResponse(false, I18nMessageService.getMsg(10032, lang));
			}else {
				long time = System.currentTimeMillis();  
			    GoogleAuthenticator ga = new GoogleAuthenticator();  
			    ga.setWindowSize(5);  	
			    boolean b=ga.checkCode(member.getGoogleKey(),vo.getGoogleCode(),time);
				if(!b) {
					return new ResultResponse(false, I18nMessageService.getMsg(10104, lang));
				}
			}
		}
		
        vo.setUserId(Integer.parseInt(userId));
        vo.setUpdateTime(new Date());
        int count= withdrawAddressService.updateById(vo);

       if(count>0) {
    	   msg = I18nMessageService.getMsg(10005, lang);
    	   success=true;
       }else {
    	   msg = I18nMessageService.getMsg(10017, lang);
       }
        return new ResultResponse(success, msg);
    }
    
}
