package com.oax.admin.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.GrabRedPacketLogService;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.front.GrabRedPacketLog;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 16:20
 * 领取红包记录 controller
 */
@RestController
@RequestMapping("grabRedPacketLogs")
public class GrabRedPacketLogController {

    @Autowired
    private GrabRedPacketLogService grabRedPacketLogService;

    @GetMapping("/{packetId}/{pageNum}/{pageSize}")
    public ResultResponse getGrabRedPacketLogsBypacketId(@PathVariable("packetId") Integer packetId,
                                                         @PathVariable("pageNum") Integer pageNum,
                                                         @PathVariable("pageSize") Integer pageSize) {

        PageInfo<GrabRedPacketLog> pageInfo = grabRedPacketLogService.selectPageByPacketId(packetId,pageNum,pageSize);
        PageResultResponse<GrabRedPacketLog> logPageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(pageInfo,logPageResultResponse);
        return new ResultResponse(true,logPageResultResponse);
    }


}
