package com.oax.mapper.front;

import java.util.List;

import com.oax.entity.front.CountryCode;

public interface CountryCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CountryCode record);

    int insertSelective(CountryCode record);

    CountryCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CountryCode record);

    int updateByPrimaryKey(CountryCode record);

    List<CountryCode> list();
}