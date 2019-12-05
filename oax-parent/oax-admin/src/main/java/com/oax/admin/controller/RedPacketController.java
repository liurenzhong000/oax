package com.oax.admin.controller;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.RedPacketService;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.dto.CountRedPacketDto;
import com.oax.entity.admin.param.RedPacketPageParam;
import com.oax.entity.admin.vo.RedPacketVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 15:15
 * 红包 Controller
 */
@RestController
@RequestMapping("redpackets")
public class RedPacketController {

    @Autowired
    RedPacketService redPacketService;

    @PostMapping("/page")
    public ResultResponse allRedPacket(@RequestBody RedPacketPageParam redPacketPageParam){


        PageInfo<RedPacketVo> redPacketPageInfo = redPacketService.selectRedPacketByPageParam(redPacketPageParam);

        PageResultResponse<RedPacketVo> redPacketPageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(redPacketPageInfo,redPacketPageResultResponse);
        redPacketPageResultResponse.setParam(redPacketPageParam);

        return new ResultResponse(true,redPacketPageResultResponse);
    }

    @PostMapping("/count")
    public ResultResponse countRedPacket(@RequestBody RedPacketPageParam redPacketPageParam){
        if (redPacketPageParam.getCoinId()==null){
            return new ResultResponse(true,new CountRedPacketDto());
        }
        CountRedPacketDto countRedPacketDto = redPacketService.countRedPacketByParam(redPacketPageParam);
        if (countRedPacketDto==null){
            countRedPacketDto = new CountRedPacketDto();
        }
        return new ResultResponse(true,countRedPacketDto);
    }

    @GetMapping("/{redPacketId}")
    public ResultResponse redPacketDetail(@PathVariable("redPacketId")Integer redPacketId){
        RedPacketVo redPacketVo = redPacketService.selectById(redPacketId);
        redPacketVo.setGrabCny(redPacketVo.getGrabCoinQty().multiply(redPacketVo.getTotalCny().divide(redPacketVo.getTotalCoinQty(),2, BigDecimal.ROUND_CEILING)));
        return new ResultResponse(true,redPacketVo);
    }

}
