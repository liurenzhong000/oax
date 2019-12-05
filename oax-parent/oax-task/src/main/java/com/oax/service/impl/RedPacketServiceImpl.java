package com.oax.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.mapper.front.RedPacketMapper;
import com.oax.service.RedPacketService;

@Service
public class RedPacketServiceImpl implements RedPacketService {
    @Autowired
    private RedPacketMapper redPacketMapper;
    @Override
    public List<Map<String,Object>> findOverdueRedPacket() {
        return redPacketMapper.findOverdueRedPacket();
    }

    @Override
    @Transactional
    public boolean upUserCoinAndInsertRecharge(Map<String, Object > params) throws Exception {
        Integer userCoinRows = redPacketMapper.addUserCoinFromRedPacket(params);
        //如果修改的数据是正常的条数
        if (userCoinRows==null||userCoinRows==0){
            throw new Exception("退回红包修改userCoin数据异常,params="+ params);
        }
        Integer rechargeRows = redPacketMapper.saveRecharge(params);
        if (rechargeRows==null||rechargeRows==0){
            throw new Exception("退回红包插入recharge数据异常，params="+params);
        }
        Integer isRefundRows = redPacketMapper.updateRedPacketIsRefund(params);
        if (isRefundRows==null||isRefundRows==0){
            throw new Exception("修改redPacket表中红包是否已退回数据异常，params="+params);
        }
        return true;
    }
}
