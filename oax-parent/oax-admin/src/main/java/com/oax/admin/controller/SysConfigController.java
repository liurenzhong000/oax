package com.oax.admin.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.admin.service.SysConfigService;
import com.oax.common.ResultResponse;
import com.oax.common.enums.SysConfigEnum;
import com.oax.entity.front.SysConfig;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:31
 */
@RestController
@RequestMapping("/sysConfigs")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;


    @GetMapping("/marketFeeRate")
    public ResultResponse getMarketFeeRate() {

        SysConfig sysConfig = sysConfigService.selectByName(SysConfigEnum.MARKET_FEE_RATE.getName());
        String value = sysConfig.getValue();
        BigDecimal bigDecimal = new BigDecimal(value);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        sysConfig.setValue(multiply.toString());
        return new ResultResponse(true, sysConfig);
    }

    @PutMapping("/marketFeeRate")
    public ResultResponse updateMarketFeeRate(@RequestBody SysConfig sysConfig) {

        if (!StringUtils.equals(sysConfig.getName(), SysConfigEnum.MARKET_FEE_RATE.getName())) {
            return new ResultResponse(false, "name名称错误");
        }


        if (sysConfig.getId() == null || sysConfig.getId() <= 0) {
            return new ResultResponse(false, "id不能为null");
        }


        try {
            SysConfig dbSysConfig = sysConfigService.selectById(sysConfig.getId());

            if (dbSysConfig == null ||
                    !StringUtils.equals(dbSysConfig.getName(), SysConfigEnum.MARKET_FEE_RATE.getName())) {
                return new ResultResponse(false, "id不正确");
            }

            BigDecimal bigDecimal = new BigDecimal(sysConfig.getValue());
            BigDecimal divide = bigDecimal.divide(new BigDecimal(100));
            sysConfig.setValue(divide.toString());
            sysConfigService.update(sysConfig);

            return new ResultResponse(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "更新失败");
        }

    }

    @GetMapping
    public ResultResponse getAll(){

        List<SysConfig> configList = sysConfigService.selectAll();

        return new ResultResponse(true,configList);

    }

    @PutMapping
    public ResultResponse update(@RequestBody SysConfig sysConfig){

        if (sysConfig.getId()==null){
            return new ResultResponse(false,"id不能为null");
        }
        sysConfigService.update(sysConfig);

        return new ResultResponse(true,"更新成功");
    }

    @PostMapping
    public ResultResponse add(@RequestBody @Valid SysConfig sysConfig, BindingResult result){

        if (result.hasErrors()) {
            return new ResultResponse(false,result.getFieldError().getDefaultMessage());
        }

        SysConfig dbSysConfig = sysConfigService.selectByName(sysConfig.getName());

        if (dbSysConfig!=null){
            return new ResultResponse(false,"数据库存在");
        }

        sysConfigService.insert(sysConfig);

        return new ResultResponse(true,"添加成功");
    }
}
