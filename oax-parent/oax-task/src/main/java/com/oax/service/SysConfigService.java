package com.oax.service;

import java.util.List;

import com.oax.entity.front.SysConfig;

public interface SysConfigService {
    List<SysConfig> getSysConfigList();
    SysConfig getSysConfig(List<SysConfig>list,String name);
}
