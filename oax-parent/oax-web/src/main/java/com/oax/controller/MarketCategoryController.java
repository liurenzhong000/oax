/**
 * 
 */
package com.oax.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.MarketCategory;
import com.oax.service.MarketCategoryService;

/** 
* @ClassName:：MarketCategoryController 
* @Description： 查询市场分区的rest入口
* @author ：xiangwh  
* @date ：2018年6月5日 下午6:18:02 
*  
*/
@RestController
@RequestMapping("marketCategory")
public class MarketCategoryController {
	@Autowired
	private MarketCategoryService marketCategoryService;
	
	@GetMapping("selectAllBySort")
	public ResultResponse selectAllBySort() {
		List<MarketCategory> list = marketCategoryService.findAllBySort();
		return new ResultResponse(true, list);
	}
}
