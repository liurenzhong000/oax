package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.dto.CountRedPacketDto;
import com.oax.entity.admin.param.RedPacketPageParam;
import com.oax.entity.admin.vo.RedPacketVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 15:41
 * RedPacket service
 */
public interface RedPacketService {


    PageInfo<RedPacketVo> selectRedPacketByPageParam(RedPacketPageParam redPacketPageParam);

    RedPacketVo selectById(Integer redPacketId);

    CountRedPacketDto countRedPacketByParam(RedPacketPageParam redPacketPageParam);
}
