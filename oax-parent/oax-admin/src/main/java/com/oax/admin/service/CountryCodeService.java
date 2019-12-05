package com.oax.admin.service;

import java.util.List;

import com.oax.entity.front.CountryCode;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/10
 * Time: 14:18
 * 国家code service
 */
public interface CountryCodeService {
    List<CountryCode> selectAll();

    int insert(CountryCode countryCode);

    int update(CountryCode countryCode);

    int delete(Integer countryId);
}
