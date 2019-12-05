package com.oax.admin.service;

import java.util.List;

import com.oax.entity.front.RedPacketLimit;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 17:31
 *
 *
 */
public interface RedPacketLimitService {
    List<RedPacketLimit> selectAll();

    int insert(RedPacketLimit redPacketLimit);

    RedPacketLimit selectById(Integer id);

    int update(RedPacketLimit dbredPacketLimit);

    int delete(Integer packetLimitId);
}
