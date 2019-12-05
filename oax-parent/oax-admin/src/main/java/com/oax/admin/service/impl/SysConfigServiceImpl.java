package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.SysConfigService;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.SysConfig;
import com.oax.mapper.front.SysConfigMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/5
 * Time: 12:00
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public SysConfig selectByName(String name) {
        return sysConfigMapper.selectByName(name);
    }

    @Override
    public int update(SysConfig sysConfig) {
        int i = sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
        redisUtil.delete(RedisKeyEnum.SYSCONFIG_LIST.getKey());
        return i;
    }

    @Override
    public SysConfig selectById(Integer id) {
        return sysConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysConfig> selectAll() {
        return sysConfigMapper.selectAll();
    }

    @Override
    public int insert(SysConfig sysConfig) {
        int i = sysConfigMapper.insertSelective(sysConfig);
        redisUtil.delete(RedisKeyEnum.SYSCONFIG_LIST.getKey());
        return i;
    }
}
