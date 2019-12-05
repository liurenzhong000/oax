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
import com.oax.admin.service.IRechargeService;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.RechargeParam;
import com.oax.entity.admin.vo.CountCoinRechargeQtyVo;
import com.oax.entity.admin.vo.RechargePageVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:33
 * 用户充值(虚拟币转入) 控制层
 */
@RestController
@RequestMapping("/recharges")
public class RechargeController {


    @Autowired
    private IRechargeService rechargeService;


    @PostMapping("/page")
    public ResultResponse allRecharge(@RequestBody RechargeParam rechargeParam) {
        PageInfo<RechargePageVo> rechargePageInfo = rechargeService.getByRechargeParam(rechargeParam);
        PageResultResponse<RechargePageVo> pageResultResponse = new PageResultResponse<>();
        BeanUtils.copyProperties(rechargePageInfo, pageResultResponse);
        pageResultResponse.setParam(rechargeParam);
        return new ResultResponse(true, pageResultResponse);
    }

    @GetMapping("/{coinId}/countQty")
    public ResultResponse countTotalQtyByCoinId(@PathVariable(name = "coinId") int coinId) {

        BigDecimal total = rechargeService.countTotalQtyByCoinId(coinId);

        CountCoinRechargeQtyVo countCoinRechargeQtyVo = new CountCoinRechargeQtyVo();

        countCoinRechargeQtyVo.setCoinId(coinId);
        countCoinRechargeQtyVo.setTotalQty(total.toString());
        return new ResultResponse(true, countCoinRechargeQtyVo);
    }
}
