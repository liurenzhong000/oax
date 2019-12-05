package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.CountryCode;
import com.oax.mapper.front.CountryCodeMapper;
import com.oax.service.CountryCodeService;

@Service
public class CountryCodeServiceImpl implements CountryCodeService {
	
	@Autowired
	private CountryCodeMapper countryCodeDao;
	@Autowired
	private RedisUtil redisUtil;
	

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<CountryCode> list() {
		
		String key="countryList";
		List<CountryCode> list = redisUtil.getList(key, CountryCode.class);
		if(list==null) {			
			list=countryCodeDao.list();
			redisUtil.setList(key, list,-1);
		}
		return list;
	}
}
