package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.SysConfig;
import com.oax.mapper.front.SysConfigMapper;
import com.oax.service.SysConfigService;

@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Autowired
    private SysConfigMapper mapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<SysConfig> getSysConfigList() {
        List<SysConfig> list = redisUtil.getList(RedisKeyEnum.SYSCONFIG_LIST.getKey(), SysConfig.class);
        if (list==null) {
            list = mapper.selectAll();
            redisUtil.setList(RedisKeyEnum.SYSCONFIG_LIST.getKey(), list);
        }
        return list;
    }

    @Override
    public SysConfig getSysConfig(List<SysConfig>list,String name) {
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
