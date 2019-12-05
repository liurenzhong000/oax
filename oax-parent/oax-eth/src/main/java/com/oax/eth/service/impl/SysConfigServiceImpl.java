package com.oax.eth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.entity.front.SysConfig;
import com.oax.eth.service.SysConfigService;
import com.oax.mapper.front.SysConfigMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 20:27
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public SysConfig selectByKey(String name) {
        return sysConfigMapper.selectByName(name);
    }

    @Override
    public int update(SysConfig sysConfig) {
        return sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
    }
}
