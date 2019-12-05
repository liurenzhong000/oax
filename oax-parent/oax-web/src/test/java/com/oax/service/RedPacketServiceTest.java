package com.oax.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.oax.OaxApiApplicationTest;
import com.oax.entity.admin.vo.RedPacketVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/7
 * Time: 17:05
 */
@Component
public class RedPacketServiceTest extends OaxApiApplicationTest {

    @Autowired
    private RedPacketService redPacketService;


    @Test
    public void selectById() {

        RedPacketVo redPacketVo = redPacketService.selectById(4);

        assert redPacketVo !=null;
    }

    @Test
    public void selectByUserId() {

        PageInfo<RedPacketVo> redPacketVo = redPacketService.selectByUserId(219164,1,5);

        assert redPacketVo.getList().size()>0;
    }
}