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

import com.oax.admin.service.CountryCodeService;
import com.oax.common.ResultResponse;
import com.oax.entity.front.CountryCode;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/10
 * Time: 14:07
 * 国家code管理
 */
@RestController
@RequestMapping("countryCodes")
public class CountryCodeController {


    @Autowired
    private CountryCodeService countryCodeService;


    @GetMapping
    public ResultResponse getAll(){

        List<CountryCode> countryCodeList = countryCodeService.selectAll();

        return new ResultResponse(true,countryCodeList);
    }

    @PostMapping
    public ResultResponse add(@RequestBody @Valid CountryCode countryCode, BindingResult result){

        if (result.hasErrors()) {
            return new ResultResponse(false,result.getFieldError().getDefaultMessage());
        }

        countryCodeService.insert(countryCode);

        return new ResultResponse(true,"添加成功");
    }


    @PutMapping
    public ResultResponse update(@RequestBody CountryCode countryCode){

        if (countryCode.getId()==null) {
            return new ResultResponse(false,"id不能为空");
        }

        countryCodeService.update(countryCode);
        return new ResultResponse(true,"更新成功");
    }

    @DeleteMapping("/{countryId}")
    public ResultResponse delete(@PathVariable("countryId")Integer countryId){

        countryCodeService.delete(countryId);
        return new ResultResponse(true,"删除成功");
    }
}
