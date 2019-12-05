package com.oax.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oax.common.AccessLimit;
import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.entity.front.vo.SnatchCoinVo;
import com.oax.entity.front.vo.SnatchConfigTypeVo;
import com.oax.entity.front.vo.SnatchDetailAggregateVo;
import com.oax.entity.front.vo.SnatchDetailVo;
import com.oax.form.SnatchAggregateForm;
import com.oax.service.activity.SnatchActivityService;
import com.oax.service.activity.SnatchConfigService;
import com.oax.service.activity.SnatchDetailService;
import com.oax.vo.SnatchActivityHomeVo;
import com.oax.vo.SnatchBetNumberVo;
import com.oax.vo.SnatchDetailPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/17 17:22
 * @Description: 一元夺宝
 */
@RestController
@RequestMapping("/activity/snatch")
@Api(tags = { "Activity API" }, description = "活动相关的API")
public class SnatchActivityController {

    @Autowired
    private SnatchActivityService snatchActivityService;

    @Autowired
    private SnatchConfigService snatchConfigService;

    @Autowired
    private SnatchDetailService snatchDetailService;

    //获取可参与抽奖的币种，相关说明
    @GetMapping("/index/anon")
    @ApiOperation("获取一元夺宝游戏首页相关数据")
    public ResultResponse index(Integer coinId){
        Integer userId = HttpContext.getUserId();
        SnatchActivityHomeVo homeVo = snatchActivityService.index(coinId, userId);
        return new ResultResponse(homeVo);
    }

    @PostMapping("/bet")
    @ApiOperation("投注")
    @AccessLimit(limit = 10, sec = 10)
    public ResultResponse bet(Integer id, BigDecimal betQty){
        AssertHelper.notEmpty(id, "id不能为空");
        AssertHelper.notEmpty(betQty, "投注金额不能为空");
        Integer userId = HttpContext.getUserId();
        Map<String, Object> data = snatchActivityService.bet(id, userId, betQty, false);
        return new ResultResponse(true, "投注成功", data);
    }

    @GetMapping("/lotteryMsg/anon")
    @ApiOperation("获取开奖播报数据")
    public ResultResponse listNewlyLottery(){
        List<String> data = snatchActivityService.listNewlyLottery();
        return new ResultResponse(true, data);
    }

    @GetMapping("/detailPage")
    @ApiOperation("进入投注详情的页面")
    public ResultResponse detailPage(){
        Integer userId = HttpContext.getUserId();
        SnatchDetailPageVo data = snatchActivityService.detailPage(userId);
        return new ResultResponse(data);
    }

    @GetMapping("/listAllCoin")
    @ApiOperation("获取所有币种列表")
    public ResultResponse listAllCoin(){
        List<SnatchCoinVo> list = snatchConfigService.listAllConfigCoin();
        return new ResultResponse(list);
    }

    @GetMapping("/listConfigType")
    @ApiOperation("获取某种币种的所有奖池数据")
    public ResultResponse listConfigType(Integer coinId){
        AssertHelper.notEmpty(coinId, "币种id不能为空");
        List<SnatchConfigTypeVo> list = snatchConfigService.listConfigTypeByCoinId(coinId);
        return new ResultResponse(list);
    }

    @GetMapping("/listAggregate")
    @ApiOperation("获取投注聚合统计数据列表")
    public ResultResponse listAggregate(SnatchAggregateForm form){
        AssertHelper.notEmpty(form.getConfigId(), "奖池类别不能为空");
        Integer userId = HttpContext.getUserId();
        Page<SnatchDetailAggregateVo> pageData = snatchDetailService.listAggregateVoByUserIdAndConfigId(userId, form);
        return new ResultResponse(pageData);
    }

    @GetMapping("/listMyDetail")
    @ApiOperation("获取我的某期投注记录")
    public ResultResponse listMyDetail(Integer activityId){
        AssertHelper.notEmpty(activityId, "奖池期号");
        Integer userId = HttpContext.getUserId();
        SnatchBetNumberVo data = snatchActivityService.listMyDetail(userId, activityId);
        return new ResultResponse(data);
    }

    @GetMapping("/listWin")
    @ApiOperation("获取某期的开奖编号记录")
    public ResultResponse listWin(Integer activityId){
        AssertHelper.notEmpty(activityId, "奖池期号");
        Integer userId = HttpContext.getUserId();
        List<SnatchDetailVo> list = snatchDetailService.listWinVo(userId, activityId);
        return new ResultResponse(list);
    }

}
