package com.oax.admin.service;

import java.util.List;

import com.oax.entity.front.SysConfig;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/5
 * Time: 12:00
 */
public interface SysConfigService {

    /**
     * 根据 系统配置信息name获取 配置项
     *
     * @param name
     * @return
     */
    SysConfig selectByName(String name);

    int update(SysConfig sysConfig);

    SysConfig selectById(Integer id);

    List<SysConfig> selectAll();

    int insert(SysConfig sysConfig);

}
