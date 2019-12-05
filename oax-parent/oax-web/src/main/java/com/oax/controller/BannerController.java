package com.oax.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.BannerInfo;
import com.oax.service.BannerService;

/** 
* @ClassName:：BannerController 
* @Description： banner入口
* @author ：xiangwh  
* @date ：2018年6月8日 下午6:53:53 
*  
*/
@RestController
@RequestMapping("banner")
public class BannerController {
	
	@Autowired
	private BannerService service;
	
	@GetMapping("list/{type}")
	public ResultResponse findList(@PathVariable(name="type") int type, @RequestHeader String lang) {
		List<BannerInfo> list = service.findList(type,lang);
		return new ResultResponse(true, list);
	}
}
