package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.GrabRedPacketLogService;
import com.oax.entity.front.GrabRedPacketLog;
import com.oax.mapper.front.GrabRedPacketLogMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 16:22
 */
@Service
public class GrabRedPacketLogServiceImpl implements GrabRedPacketLogService {
    @Autowired
    private GrabRedPacketLogMapper grabRedPacketLogMapper;

    @Override
    public PageInfo<GrabRedPacketLog> selectPageByPacketId(Integer packetId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<GrabRedPacketLog> grabRedPacketLogs = grabRedPacketLogMapper.selectByPacketId(packetId);
        return new PageInfo<>(grabRedPacketLogs);
    }
}
