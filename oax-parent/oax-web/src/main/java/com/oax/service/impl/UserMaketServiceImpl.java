/**
 * 
 */
package com.oax.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.Constant;
import com.oax.common.RedisUtil;
import com.oax.common.ResultResponse;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.UserMaket;
import com.oax.mapper.front.UserMaketMapper;
import com.oax.service.I18nMessageService;
import com.oax.service.UserMaketService;

/** 
* @ClassName:：UserMaketServiceImpl 
* @Description： 用户自选市场的service业务层
* @author ：xiangwh  
* @date ：2018年6月5日 下午7:36:42 
*  
*/
@Service
public class UserMaketServiceImpl implements UserMaketService {
	
	@Autowired
	private UserMaketMapper mapper;
	@Autowired
    private I18nMessageService msgService;
	@Autowired
	private RedisUtil redisUtil;
	
	//用户添加收藏市场交易对
	@Transactional
	@DataSource(DataSourceType.MASTER)
	@Override
	public ResultResponse save(UserMaket userMaket,HttpServletRequest request) {
		String lang = request.getHeader(Constant.api_header_lang);
		String msg = null;
		Integer count = mapper.getCountsByMaketId(userMaket);
		if (count!=null && count>0) {
			msg = msgService.getMsg(10047, lang);
			return new ResultResponse(false, msg);
		}
		Integer rowNum = mapper.insert(userMaket);
		boolean flag = false;
		if (rowNum!=0&&rowNum>0) {		
			msg = msgService.getMsg(10005, lang);
			flag = true;
		}else {
			msg = msgService.getMsg(10100, lang);
		}	
		return  new ResultResponse(flag, msg);
	}

	//用户自选查询收藏的交易对信息
	@DataSource(DataSourceType.SLAVE)
	@Override
	public List<MarketInfo> findListByUser(Integer userId) {
		List<Integer> list = mapper.getTradeCoinListByUser(userId);
		List<MarketInfo> allMarketInfoList = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
		List<MarketInfo> userMatketList = new ArrayList<>();
		if (list!=null && list.size()!=0) {
			for (MarketInfo marketInfo : allMarketInfoList) {
				for (Integer marketId : list) {
					if (marketId==marketInfo.getMarketId()) {
						marketInfo.setIsCollection(1);
						userMatketList.add(marketInfo);
						break;
					}			
				}
			}
		}	
		return userMatketList;
	}
	
	@Transactional
	@DataSource(DataSourceType.MASTER)
	@Override
	public boolean delete(Integer marketId,Integer userId) {
		Integer deleteRows = mapper.delete(marketId,userId);
		return  deleteRows>0;
	}
	
	

}
