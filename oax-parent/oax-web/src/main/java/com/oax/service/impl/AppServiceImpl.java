package com.oax.service.impl;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.ResultResponse;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.App;
import com.oax.mapper.front.AppMapper;
import com.oax.service.AppService;
import com.oax.vo.AppVO;

/** 
* @ClassName:：AppServiceImpl 
* @Description： app接口检测更新实现类
* @author ：xiangwh  
* @date ：2018年6月12日 上午11:40:41 
*  
*/
@Service
public class AppServiceImpl implements AppService {
	
	@Autowired
	private AppMapper mapper;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public ResultResponse checkVersion(AppVO vo) {
		App uApp = mapper.checkVersion(vo.getType());
//		if (uApp==null) {
//			uApp = mapper.checkVersion(vo.getType());
//			redisUtil.setObject(RedisKeyEnum.APP_LAST_VERSION.getKey()+vo.getType(), uApp);
//		}
		if (uApp!=null && StringUtils.isNotBlank(uApp.getVersion())) {
			//如果不是最新版本
			if (!uApp.getVersion().equals(vo.getVersion())) {
				return new ResultResponse(true, uApp);
			}
		}
		return new ResultResponse(false,"当前版本为最新版本");
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public String getLastVersionUrl(Integer type) {
		App app = redisUtil.getObject(RedisKeyEnum.APP_LAST_VERSION.getKey()+type, App.class);
		if (app==null) {
			return mapper.getLastVersionUrl(type);
		}
		return app.getDownloadUrl();
	}

}
