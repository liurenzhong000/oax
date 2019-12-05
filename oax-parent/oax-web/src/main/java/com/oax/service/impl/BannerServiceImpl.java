package com.oax.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.BannerInfo;
import com.oax.mapper.front.BannerMapper;
import com.oax.service.BannerService;

/**
* @ClassName:：BannerServiceImpl 
* @Description： banner业务层实现类
* @author ：xiangwh  
* @date ：2018年6月8日 下午6:44:10 
*  
*/
@Service
public class BannerServiceImpl implements BannerService {
	@Autowired
	private BannerMapper mapper;
	@Autowired
	private RedisUtil redisUtil;
	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<BannerInfo> findList(int type,String lang) {
		List<BannerInfo> list = redisUtil.getList(RedisKeyEnum.BANNER_LIST.getKey()+type+"."+lang, BannerInfo.class);
		if (list==null || list.size()==0) {
			Map<String, Object> map = new HashMap<>();
			map.put("lang", lang);
			map.put("type", type);
			list = mapper.findList(map);
			redisUtil.setList(RedisKeyEnum.BANNER_LIST.getKey()+type+"."+lang, list,-1);
		}
		return list;
	}

}
