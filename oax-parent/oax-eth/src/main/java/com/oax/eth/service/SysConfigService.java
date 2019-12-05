package com.oax.eth.service;

import com.oax.entity.front.SysConfig;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 20:26
 * 系统值service
 */
public interface SysConfigService {
    SysConfig selectByKey(String name);

    int update(SysConfig sysConfig);
}
