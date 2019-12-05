package com.oax.eth.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.RechargeAddress;
import com.oax.eth.service.RechargeAddressService;
import com.oax.mapper.front.RechargeAddressMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/11
 * Time: 17:35
 */
@Service
public class RechargeAddressServiceImpl implements RechargeAddressService {
    @Autowired
    private RechargeAddressMapper rechargeAddressMapper;
    @Override
    public List<RechargeAddress> selectAllETHAdress() {
        return rechargeAddressMapper.selectByCoinType(CoinTypeEnum.ETH.getType());
    }

    @Override
    public List<RechargeAddress> selectByAddress(String toAddress) {
        return rechargeAddressMapper.selectByAddress(toAddress);
    }

    @Override
    public RechargeAddress selectByAddressAndParentCoinId(String toAddress, Integer parentId) {
        return rechargeAddressMapper.selectByAddressAndParentCoinId(toAddress,parentId);
    }
}
