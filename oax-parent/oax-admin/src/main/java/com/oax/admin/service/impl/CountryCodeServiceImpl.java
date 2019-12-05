package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.CountryCodeService;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.CountryCode;
import com.oax.mapper.front.CountryCodeMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/10
 * Time: 14:18
 */
@Service
public class CountryCodeServiceImpl implements CountryCodeService {

    @Autowired
    private CountryCodeMapper countryCodeMapper;

    @Autowired
    private RedisUtil redisUtil;
    @Override
    public List<CountryCode> selectAll() {
        return countryCodeMapper.list();
    }

    @Override
    public int insert(CountryCode countryCode) {
        int i = countryCodeMapper.insertSelective(countryCode);
        redisUtil.delete(RedisKeyEnum.COUNTRY_LIST.getKey());
        return i;
    }

    @Override
    public int update(CountryCode countryCode) {
        int i = countryCodeMapper.updateByPrimaryKeySelective(countryCode);
        redisUtil.delete(RedisKeyEnum.COUNTRY_LIST.getKey());
        return i;
    }

    @Override
    public int delete(Integer countryId) {
        int i = countryCodeMapper.deleteByPrimaryKey(countryId);
        redisUtil.delete(RedisKeyEnum.COUNTRY_LIST.getKey());
        return i;
    }
}
