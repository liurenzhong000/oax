package com.oax.admin.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.SumCoinService;
import com.oax.admin.service.UserCoinService;
import com.oax.common.PageResultResponse;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.SumCoinPageParam;
import com.oax.entity.admin.vo.SumCoinPageVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:41
 * 平台虚拟货币统计 控制层
 */
@RestController
@RequestMapping("/sumCoins")
public class SumCoinController {
    @Autowired
    private SumCoinService sumCoinService;

    @Autowired
    private UserCoinService userCoinService;

    @PostMapping("/page")
    public ResultResponse getSumCoinPage(@RequestBody SumCoinPageParam sumCoinPageParam) {

        PageInfo<SumCoinPageVo> pageInfo = sumCoinService.getSumCoinByPageGroupByCoinId(sumCoinPageParam);


        List<SumCoinPageVo> sumCoinPageVoList = pageInfo.getList();

        for (SumCoinPageVo sumCoinPageVo : sumCoinPageVoList) {
            BigDecimal coinUserBalance = userCoinService.countAllBanlanceByCoinId(sumCoinPageVo.getCoinId());
            sumCoinPageVo.setUserBalance(coinUserBalance);
        }

        PageResultResponse<SumCoinPageVo> pageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(pageInfo, pageResultResponse);
        pageResultResponse.setParam(sumCoinPageParam);

        return new ResultResponse(true, pageResultResponse);

    }


}
