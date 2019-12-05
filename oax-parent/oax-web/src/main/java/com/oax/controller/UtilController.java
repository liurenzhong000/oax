package com.oax.controller;

import com.oax.common.BankCardUtil;
import com.oax.common.PlaceUtil;
import com.oax.common.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/util")
public class UtilController {

    /**
     * 获取全部的省份
     */
    @GetMapping("/city/getAllProvince")
    public ResultResponse getAllProvince(){
        List<String> provinces = PlaceUtil.getAllProvince();
        return new ResultResponse(true, provinces);
    }

    /**
     * 通过省份获取市
     */
    @GetMapping("/city/getCityByProvince")
    public ResultResponse getAllProvince(String province){
        List<String> citys = PlaceUtil.getCityByProvince(province);
        return new ResultResponse(true, citys);
    }

    /**
     * 获取银行卡下拉框内容
     */
    @GetMapping("/bank/getAllBanks")
    public ResultResponse getAllBank(){
        List<String> banks = BankCardUtil.getAllBank();
        return new ResultResponse(true, banks);
    }

}
