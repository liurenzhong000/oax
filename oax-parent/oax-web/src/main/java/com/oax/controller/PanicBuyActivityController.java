package com.oax.controller;

import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.service.activity.PanicBuyActivityService;
import com.oax.vo.PanicBuyActivityDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BHB抢购页面
 */
@RestController
@RequestMapping("/panicBuy")
@Api(tags = { "PanicBuy API" }, description = "BHB抢购活动的API")
public class PanicBuyActivityController {

    @Autowired
    private PanicBuyActivityService panicBuyActivityService;

    @GetMapping("/index/anon")
    @ApiOperation("进入活动首页，获取相关数据")
    public ResultResponse activityIndex(){
        Integer userId = HttpContext.getUserId();
        PanicBuyActivityDataVo dataVo = panicBuyActivityService.activityIndex(userId);
        return new ResultResponse(true, dataVo);
    }

    @PostMapping("/participate")
    @ApiOperation("参与当前的BHB抢购活动")
    public ResultResponse participate(Integer activityId){
        Integer userId = HttpContext.getUserId();
        panicBuyActivityService.participate(activityId, userId);
        return new ResultResponse(true, "参与成功");
    }

}
