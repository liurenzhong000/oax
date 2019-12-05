package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListSnatchActivityForm;
import com.oax.admin.service.activity.SnatchActivityService;
import com.oax.common.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 19:00
 * @Description:
 */
@RestController
@RequestMapping("/activity/snatchActivity")
public class SnatchActivityController {

    @Autowired
    private SnatchActivityService snatchActivityService;

    @GetMapping("/list")
    public ResultResponse page(ListSnatchActivityForm form){
        PageInfo pageInfo = snatchActivityService.list(form);
        return new ResultResponse(pageInfo);
    }

    @GetMapping("/aggregationDetail")
    public ResultResponse page(Integer coinId){
        if (coinId == null) coinId = 54;
        Map<String, Object> data = snatchActivityService.aggregationDetail(coinId);
        return new ResultResponse(data);
    }
}
