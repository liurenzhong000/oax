package com.oax.admin.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.entity.admin.vo.RedPacketVo;
import com.oax.mapper.front.RedPacketMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/22
 * Time: 16:53
 */
@Component
public class RedPacketServiceImplTest extends OaxApiApplicationTest {

    @Autowired
    private RedPacketMapper redPacketMapper;
    @Test
    public void selectById() {
        RedPacketVo redPacketVo = redPacketMapper.selectRedPacketVoById(365);
        assert redPacketVo!=null;
    }
}