package com.oax.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oax.common.ResultResponse;
import com.oax.service.AppService;
import com.oax.vo.AppVO;

/** 
* @ClassName:：AppController 
* @Description： app更新入口
* @author ：xiangwh  
* @date ：2018年6月12日 上午11:49:52 
*  
*/
@Controller
@RequestMapping("app")
public class AppController {
	@Autowired
	private AppService appService;
	
	@PostMapping("checkVersion")
	@ResponseBody
	public ResultResponse checkVersion(@RequestBody AppVO vo) {
		return appService.checkVersion(vo);
	}
	@RequestMapping("downloadUrl")
	public String getDownloadUrl(HttpServletRequest request){
		String agent = request.getHeader("user-agent");
		Integer type = this.getDeviceType(agent);
		String url = appService.getLastVersionUrl(type);
		return "redirect:"+url;
	}
	
	public Integer getDeviceType(String userAgent) {
		int type = 2;
		if (userAgent.contains("iPhone") || userAgent.contains("iPod") || userAgent.contains("iPad")) {
			type = 1;
		}
		return type;
	}
}
