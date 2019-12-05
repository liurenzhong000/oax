package com.oax.mapper.front;

import java.util.List;

import com.oax.entity.front.GrabRedPacketLog;

public interface GrabRedPacketLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GrabRedPacketLog record);

    int insertSelective(GrabRedPacketLog record);

    GrabRedPacketLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GrabRedPacketLog record);

    int updateByPrimaryKey(GrabRedPacketLog record);

    List<GrabRedPacketLog> selectByPacketId(Integer packetId);

    List<GrabRedPacketLog> selectPageByUserId(Integer userId);
}