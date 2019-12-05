package com.oax.admin.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.admin.service.RedPacketLimitService;
import com.oax.common.ResultResponse;
import com.oax.entity.front.RedPacketLimit;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 17:31
 */
@RestController
@RequestMapping("redPacketLimit")
public class RedPacketLimitController {

    @Autowired
    private RedPacketLimitService redPacketLimitService;

    @GetMapping
    public ResultResponse getAll(){

        List<RedPacketLimit> redPacketLimitList = redPacketLimitService.selectAll();

        return new ResultResponse(true,redPacketLimitList);
    }

    @GetMapping("/{packetLimitId}")
    public ResultResponse getRedPacketLimit(@PathVariable("packetLimitId")Integer packetLimitId){
        RedPacketLimit redPacketLimit = redPacketLimitService.selectById(packetLimitId);
        return new ResultResponse(true,redPacketLimit);
    }

    @PostMapping
    public ResultResponse add(@RequestBody @Valid RedPacketLimit redPacketLimit, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false,result.getFieldError().getDefaultMessage());
        }
        int insert = redPacketLimitService.insert(redPacketLimit);
        if (insert>0){
            return new ResultResponse(true,"添加成功");

        }else if (insert==0){
            return new ResultResponse(false,"有相同币种与类型的红包限制");
        }else {
            return new ResultResponse(false,"添加失败");
        }

    }

    @PutMapping
    public ResultResponse update(@RequestBody @Valid RedPacketLimit redPacketLimit,BindingResult result){

        if (result.hasErrors()) {
            return new ResultResponse(false,result.getFieldError().getDefaultMessage());
        }
        if (redPacketLimit.getId()==null){
            return new ResultResponse(false,"id不能为null");
        }

        RedPacketLimit dbredPacketLimit = redPacketLimitService.selectById(redPacketLimit.getId());

        if (dbredPacketLimit==null){
            return new ResultResponse(false,"id:"+redPacketLimit.getId()+"不存在");
        }

        dbredPacketLimit.setLimitCoinQty(redPacketLimit.getLimitCoinQty());
        dbredPacketLimit.setLimitPacketQty(redPacketLimit.getLimitPacketQty());

        redPacketLimitService.update(dbredPacketLimit);
        return new ResultResponse(true,"更新成功");
    }

    @DeleteMapping("/{packetLimitId}")
    public ResultResponse delete(@PathVariable("packetLimitId")Integer packetLimitId){

        redPacketLimitService.delete(packetLimitId);
        return new ResultResponse(true,"删除成功");
    }

}
