package com.oax.controller;

import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.exception.VoException;
import com.oax.form.PanicBuyHelpForm;
import com.oax.service.activity.PanicBuyHelpService;
import com.oax.vo.PanicBuyShareVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * BHB抢购好友助力页面
 */
@RestController
@RequestMapping("/panicBuy/share")
@Api(tags = { "PanicBuy API" }, description = "BHB抢购活动的API")
public class PanicBuyHelpController {

    @Autowired
    private PanicBuyHelpService panicBuyHelpService;

    //这个页面无需登录
    @GetMapping("/index/anon")
    @ApiOperation("获取对应助力页的数据")
    public ResultResponse index(String shareCode){
        AssertHelper.notEmpty(shareCode, "shareCode为空");
        //根据用户的分享码来获取用户的id和对应期数活动数据
        PanicBuyShareVo data = panicBuyHelpService.getIndexData(shareCode);
        return new ResultResponse(true, data);
    }

    //这个页面无需登录
    @PostMapping("/help/anon")
    @ApiOperation("验证助力码，帮助好友助力")
    public ResultResponse saveHelp(PanicBuyHelpForm form, HttpServletRequest request) throws VoException {
        AssertHelper.notEmpty(form.getPhone(), "手机号不能为空");
        AssertHelper.notEmpty(form.getShareCode(), "shareCode不能为空");
        AssertHelper.notEmpty(form.getHelpCode(), "助力码不能为空");

        PanicBuyShareVo data = panicBuyHelpService.saveHelp(request,form);
        return new ResultResponse(true, data);
    }
}
