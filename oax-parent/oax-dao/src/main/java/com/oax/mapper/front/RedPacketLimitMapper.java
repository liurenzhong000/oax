package com.oax.mapper.front;

import java.util.List;

import com.oax.entity.front.RedPacketLimit;

public interface RedPacketLimitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedPacketLimit record);

    int insertSelective(RedPacketLimit record);

    RedPacketLimit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedPacketLimit record);

    int updateByPrimaryKey(RedPacketLimit record);

    List<RedPacketLimit> selectAll();

    RedPacketLimit selectById(Integer id);

    int insertIgnore(RedPacketLimit record);
}