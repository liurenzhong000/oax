package com.oax.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.CoinLockConfig;
import com.oax.mapper.front.CoinLockConfigMapper;
import com.oax.service.CoinLockConfigService;

@Service
public class CoinLockConfigServiceImpl implements CoinLockConfigService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CoinLockConfigMapper coinLockConfigMapper;

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Map<String,Object> getCoinLockConfig() {
        Map<String,Object> map = new HashMap<>();
        //***** 后台系统开发修改配置时需要删除缓存
        List<CoinLockConfig> coinLockConfigList = redisUtil.getList(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey(), CoinLockConfig.class);
        if (coinLockConfigList==null){
            coinLockConfigList = coinLockConfigMapper.getCoinLockConfig();
            redisUtil.setList(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey(), coinLockConfigList,-1);
        }
        //第二天0点0时0分执行计息分红
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        String date = sdf.format(cal.getTime());
        map.put("date", date);
        map.put("coinLockConfigList", coinLockConfigList);
        return map;
    }

    @Override
    public List<CoinLockConfig> selectAll() {
        List<CoinLockConfig> coinLockConfigList = redisUtil.getList(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey(), CoinLockConfig.class);
        if (coinLockConfigList==null){
            coinLockConfigList = coinLockConfigMapper.getCoinLockConfig();
            redisUtil.setList(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey(), coinLockConfigList,-1);
        }
        return coinLockConfigList;
    }
}
