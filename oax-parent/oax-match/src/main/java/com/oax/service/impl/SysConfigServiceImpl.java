package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.common.enums.SysConfigEnum;
import com.oax.entity.front.SysConfig;
import com.oax.mapper.front.SysConfigMapper;
import com.oax.service.SysConfigService;

/** 
* @ClassName:：SysConfigServiceImpl 
* @Description： 获取手续费率业务
* @author ：xiangwh  
* @date ：2018年6月27日 下午3:02:08 
*  
*/
@Service
public class SysConfigServiceImpl implements SysConfigService {
	@Autowired
	private SysConfigMapper sysConfigMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public SysConfig marketFeeRate() {
		// 添加先从缓存中取的操作
		List<SysConfig> list = redisUtil.getList(RedisKeyEnum.SYSCONFIG_LIST.getKey(), SysConfig.class);
		SysConfig sysConfig = getSysConfigFromList(list,SysConfigEnum.MARKET_FEE_RATE.getName());
		if (sysConfig==null) {
			list = sysConfigMapper.selectAll();
			sysConfig = getSysConfigFromList(list, SysConfigEnum.MARKET_FEE_RATE.getName());
			redisUtil.setList(RedisKeyEnum.SYSCONFIG_LIST.getKey(), list);
		}
		return sysConfig;
	}
	
	public SysConfig getSysConfigFromList(List<SysConfig> list,String name) {
		SysConfig sysConfig = null;
		if (list!=null && list.size()!=0) {
			for (SysConfig config : list) {
				if (name.equals(config.getName())) {
					sysConfig = config;
					break;
				}
			}
		}
		return sysConfig;
	}
	

}
