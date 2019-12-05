package com.oax.controller;

import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.service.ApiThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/23 14:44
 * @Description: 提供给第三方合作的api - 爆点游戏暴露的api
 */
@RestController
@RequestMapping("/api/third")
public class ApiThirdController {

    @Autowired
    private ApiThirdService apiThirdService;

    @GetMapping("/checkByToken")
    public ResultResponse checkByToken(Integer userId, String accessToken){
        AssertHelper.notEmpty(userId, "用户id不能为空");
        AssertHelper.notEmpty(accessToken, "accessToken不能为空");
        Map<String, Object> data = apiThirdService.checkByToken(userId, accessToken);
        return new ResultResponse(true, data);
    }

    @GetMapping("/getBalanceByCoinId")
    public ResultResponse getBalanceByCoinId(Integer userId, String accessToken){
        AssertHelper.notEmpty(userId, "用户id不能为空");
        Map<String, Object> data = apiThirdService.getBalanceByCoinId(userId, 54, accessToken);
        return new ResultResponse(true, data);
    }

    @PostMapping("/chargeBalance")
    public ResultResponse chargeBalance(Integer userId, BigDecimal qty, String targetId, String accessToken){
        AssertHelper.notEmpty(userId, "用户id不能为空");
        AssertHelper.notEmpty(qty, "余额变更数不能为空");
        Map<String, Object> data = apiThirdService.chargeBalance(userId, 54, qty, targetId, accessToken);
        return new ResultResponse(true, data);
    }

}
