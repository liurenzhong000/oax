package com.oax.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.front.GrabRedPacketLog;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 16:21
 * 领取红包记录 service
 */
public interface GrabRedPacketLogService {
    PageInfo<GrabRedPacketLog> selectPageByPacketId(Integer packetId, Integer pageNum, Integer pageSize);

    PageInfo<GrabRedPacketLog> selectPageByUserId(Integer userId, Integer pageNum, Integer pageSize);
}
