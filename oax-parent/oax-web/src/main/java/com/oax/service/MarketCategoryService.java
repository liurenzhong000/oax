/**
 * 
 */
package com.oax.service;

import java.util.List;

import com.oax.entity.front.MarketCategory;

/** 
* @ClassName:：MarketCategoryService 
* @Description： 查询市场分区信息
* @author ：xiangwh  
* @date ：2018年6月5日 下午6:10:21 
*  
*/
public interface MarketCategoryService {
	/**
	 * 
	* @Title：findAllBySort 
	* @Description：查询市场分区信息(未下架的分区)
	* @param ：@return 
	* @return ：List<MarketCategory> 
	* @throws
	 */
	List<MarketCategory> findAllBySort();
	/**
	 * 
	* @Title：findAllBySortToAll 
	* @Description：查询市场分区信息(左右分区)
	* @throws
	 */
	List<MarketCategory> findAllBySortToAll();
}
