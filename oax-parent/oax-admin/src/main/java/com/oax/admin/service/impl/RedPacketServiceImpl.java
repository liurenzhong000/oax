package com.oax.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.RedPacketService;
import com.oax.entity.admin.dto.CountRedPacketDto;
import com.oax.entity.admin.param.RedPacketPageParam;
import com.oax.entity.admin.vo.RedPacketVo;
import com.oax.mapper.front.RedPacketMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 15:42
 */
@Service
public class RedPacketServiceImpl implements RedPacketService {

    @Autowired
    private RedPacketMapper redPacketMapper;
    @Override
    public PageInfo<RedPacketVo> selectRedPacketByPageParam(RedPacketPageParam redPacketPageParam) {
        PageHelper.startPage(redPacketPageParam.getPageNum(),redPacketPageParam.getPageSize());
        List<RedPacketVo> redPacketList = redPacketMapper.selectRedPacketByPageParam(redPacketPageParam);
        return new PageInfo<>(redPacketList);
    }

    @Override
    public RedPacketVo selectById(Integer redPacketId) {
        return redPacketMapper.selectRedPacketVoById(redPacketId);
    }

    @Override
    public CountRedPacketDto countRedPacketByParam(RedPacketPageParam redPacketPageParam) {
        return redPacketMapper.countRedPacketByParam(redPacketPageParam);
    }
}
