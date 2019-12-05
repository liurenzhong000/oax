package com.oax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.I18nMessage;
import com.oax.mapper.front.I18nMessageMapper;

@Service
public class I18nMessageService {

	@Autowired
	private I18nMessageMapper i18nMessageMapper;
	@Autowired
	private RedisUtil redisUtil;

	@DataSource(DataSourceType.SLAVE)
	public String getMsg(Integer code, String lang) {
		String msgStr = null;

		String key="i180n_msg";
		//List<I18nMessage> I18nMessageList = redisUtil.getList(key, new TypeReference<ArrayList<I18nMessage>>() {});
		List<I18nMessage> I18nMessageList = redisUtil.getList(key, I18nMessage.class);
		if(I18nMessageList==null) {
			
			I18nMessageList=i18nMessageMapper.findList();
			redisUtil.setList(key, I18nMessageList);
		}

		for (I18nMessage i18nMessage : I18nMessageList) {

			if (code.intValue() == i18nMessage.getCode().intValue()) {

				if ("cn".equals(lang)) {
					msgStr = i18nMessage.getCn();
				} else{
					msgStr = i18nMessage.getEn();
				}

				break;
			}

		}

		return msgStr;
	}

}
