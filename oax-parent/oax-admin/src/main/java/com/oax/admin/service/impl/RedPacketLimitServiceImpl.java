package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.RedPacketLimitService;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.RedPacketLimit;
import com.oax.mapper.front.RedPacketLimitMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 17:31
 */
@Service
public class RedPacketLimitServiceImpl implements RedPacketLimitService {


    @Autowired
    private RedPacketLimitMapper redPacketLimitMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<RedPacketLimit> selectAll() {
        return redPacketLimitMapper.selectAll();
    }

    @Override
    public int insert(RedPacketLimit redPacketLimit) {
        int i = redPacketLimitMapper.insertIgnore(redPacketLimit);
        redisUtil.delete(RedisKeyEnum.REDPACKET_LIMIT.getKey());
        return i;
    }

    @Override
    public RedPacketLimit selectById(Integer id) {
        return redPacketLimitMapper.selectById(id);
    }

    @Override
    public int update(RedPacketLimit dbredPacketLimit) {
        dbredPacketLimit.setUpdateTime(null);
        int i = redPacketLimitMapper.updateByPrimaryKeySelective(dbredPacketLimit);
        redisUtil.delete(RedisKeyEnum.REDPACKET_LIMIT.getKey());
        return i;
    }

    @Override
    public int delete(Integer packetLimitId) {
        int i = redPacketLimitMapper.deleteByPrimaryKey(packetLimitId);
        redisUtil.delete(RedisKeyEnum.REDPACKET_LIMIT.getKey());
        return i;
    }
}
