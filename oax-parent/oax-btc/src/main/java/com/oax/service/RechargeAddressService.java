package com.oax.service;

import java.util.List;

import com.oax.entity.front.RechargeAddress;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/11
 * Time: 16:40
 * 用户转账地址
 */
public interface RechargeAddressService {
    List<RechargeAddress> selectByCoinType(Integer type);

    List<RechargeAddress> selectByUsdtAddress();
}
