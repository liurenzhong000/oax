package com.oax.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.oax.OaxApiApplicationTest;
import com.oax.entity.front.GrabRedPacketLog;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/7
 * Time: 17:02
 */
@Component
public class GrabRedPacketLogServiceTest extends OaxApiApplicationTest {


    @Autowired
    private GrabRedPacketLogService grabRedPacketLogService;

    @Test
    public void selectPageByPacketId() {
        PageInfo<GrabRedPacketLog> pageInfo = grabRedPacketLogService.selectPageByPacketId(1, 1, 5);

        assert pageInfo.getList().size() > 0;

    }

    @Test
    public void selectPageByUserId() {

        PageInfo<GrabRedPacketLog> pageInfo = grabRedPacketLogService.selectPageByUserId(100726, 1, 5);

        assert pageInfo.getList().size() > 0;
    }
}