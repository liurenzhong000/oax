package com.oax.service;

import com.oax.common.ResultResponse;
import com.oax.vo.AppVO;

/** 
* @ClassName:：AppService 
* @Description： app检测更新
* @author ：xiangwh  
* @date ：2018年6月12日 上午11:38:05 
*  
*/
public interface AppService {
	/**
	 * 
	* @Title：checkVersion 
	* @Description：查看当前系统的最新版本的app信息 对比是否是最新版本
	* @param ：@param type ios|android
	* @param ：@return 
	* @return ：ResultResponse 
	* @throws
	 */
	ResultResponse checkVersion(AppVO vo);

	/** 
	* @Title：getLastVersionUrl 
	* @Description：获取最新的下载地址
	* @throws 
	*/
	String getLastVersionUrl(Integer type);
}
