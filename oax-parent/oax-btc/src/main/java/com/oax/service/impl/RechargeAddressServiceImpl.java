package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.RechargeAddress;
import com.oax.mapper.front.RechargeAddressMapper;
import com.oax.service.RechargeAddressService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/11
 * Time: 16:41
 */
@Service
public class RechargeAddressServiceImpl implements RechargeAddressService {
    @Autowired
    private RechargeAddressMapper rechargeAddressMapper;

    @Override
    public List<RechargeAddress> selectByCoinType(Integer type) {
        return rechargeAddressMapper.selectByCoinType(type);
    }

    @Override
    public List<RechargeAddress> selectByUsdtAddress() {
        return rechargeAddressMapper.selectByCoinType(CoinTypeEnum.BTC.getType());
    }

}
