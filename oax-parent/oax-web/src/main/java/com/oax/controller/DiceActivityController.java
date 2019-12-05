package com.oax.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oax.common.AccessLimit;
import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.vo.DiceActivityVo;
import com.oax.entity.front.vo.DiceConfigVo;
import com.oax.form.DiceBetForm;
import com.oax.service.activity.DiceActivityService;
import com.oax.service.activity.DiceConfigService;
import com.oax.vo.DiceIndexVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 09:40
 * @Description: dice游戏活动
 */
@RestController
@RequestMapping("/activity/dice")
@Api(tags = { "Activity API" }, description = "活动相关的API")
public class DiceActivityController {

    @Autowired
    private DiceActivityService diceActivityService;

    @Autowired
    private DiceConfigService diceConfigService;

    //获取可参与抽奖的币种，已经相关说明
    @GetMapping("/diceConfigList/anon")
    @ApiOperation("获取DICE游戏首页相关数据")
    public ResultResponse diceConfigList(){
        List<DiceConfigVo> diceConfigVos = diceConfigService.diceOpenCoinList();
        return new ResultResponse(diceConfigVos);
    }

    //这个页面无需登录
    @GetMapping("/index/anon")
    @ApiOperation("获取DICE游戏首页相关数据")
    public ResultResponse index(@RequestParam(required = false) Integer coinId){
        //根据用户的分享码来获取用户的id和对应期数活动数据
        Integer userId = HttpContext.getUserId();
        DiceIndexVo indexVo = diceActivityService.index(userId, coinId);
        return new ResultResponse(indexVo);
    }

    @PostMapping("/bet")
    @ApiOperation("投注进行开奖")
    @AccessLimit(limit = 1, sec = 1)//1s最多请求一次
    public ResultResponse bet(DiceBetForm form){
        Integer userId = HttpContext.getUserId();
        AssertHelper.notEmpty(form.getRollUnder(), "下注数值不能为空");
        AssertHelper.notEmpty(form.getBetQty(), "投注金额不能为空");
        AssertHelper.isTrue(form.getRollUnder()>=2 && form.getRollUnder()<=96, "投注数字错误，请选择2-96");
        DiceActivityVo diceActivityVo = diceActivityService.bet(userId, form.getCoinId(), form.getRollUnder(), form.getBetQty());
        return new ResultResponse(true, "投注成功", diceActivityVo);
    }

    @PostMapping("/signIn")
    @ApiOperation("签到")
    public ResultResponse signIn() throws IllegalAccessException {
        return new ResultResponse(false, "该活动已下架");
//        Integer userId = HttpContext.getUserId();
//        Integer continueDays = diceActivityService.signIn(userId);
//        return new ResultResponse(true, "签到成功", Collections.singletonMap("continueDays", continueDays));
    }

    @GetMapping("/diceList")
    @ApiOperation("用户dice参与记录")
    public ResultResponse diceList(PageParam pageParam, @RequestParam(required = false) Integer coinId) {
        Integer userId = HttpContext.getUserId();
        Page<DiceActivityVo> diceActivityPage =  diceActivityService.diceList(pageParam, userId, coinId);
        return new ResultResponse(diceActivityPage);
    }
}
