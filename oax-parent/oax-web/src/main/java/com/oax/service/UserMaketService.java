/**
 * 
 */
package com.oax.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oax.common.ResultResponse;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.UserMaket;

/** 
* @ClassName:：UserMaketService 
* @Description： 用户自选市场service接口
* @author ：xiangwh  
* @date ：2018年6月5日 下午7:28:19 
*  
*/
public interface UserMaketService {
	/**
	 * @param request 
	 * 
	* @Title：save 
	* @Description：用户收藏市场
	* @param ：@param userMaket
	* @param ：@return 
	* @return ：Integer 
	* @throws
	 */
	ResultResponse save(UserMaket userMaket,HttpServletRequest request);
	/**
	 * 
	* @Title：findListByUser 
	* @Description：根据userId 和币种id模糊查询用户自选的交易对信息
	* @param ：@param coinId
	* @param ：@param userId
	* @param ：@return 
	* @return ：List<UserMaket> 
	* @throws
	 */
	List<MarketInfo> findListByUser(Integer userId);
	/**
	 * 
	* @Title：delete 
	* @Description：取消关注市场
	* @throws
	 */
	boolean delete(Integer marketId,Integer userId);
}
