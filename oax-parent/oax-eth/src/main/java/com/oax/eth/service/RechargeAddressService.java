package com.oax.eth.service;

import java.util.List;

import com.oax.entity.front.RechargeAddress;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/11
 * Time: 17:34
 * 用户充值地址
 */
public interface RechargeAddressService {
    List<RechargeAddress> selectAllETHAdress();

    List<RechargeAddress> selectByAddress(String toAddress);

    RechargeAddress selectByAddressAndParentCoinId(String toAddress, Integer parentId);
}
