package com.oax.service;

import java.util.List;

import com.oax.entity.front.BannerInfo;

/** 
* @ClassName:：BannerService 
* @Description： 首页获取图片
* @author ：xiangwh  
* @date ：2018年6月8日 下午6:41:56 
*  
*/
public interface BannerService {
	/**
	 * @param lang 
	 * 
	* @Title：findList 
	* @Description：获取所有的banner
	* @param ：@param status
	* @param ：@return 
	* @return ：List<Banner> 
	* @throws
	 */
	List<BannerInfo> findList(int status, String lang);
}
