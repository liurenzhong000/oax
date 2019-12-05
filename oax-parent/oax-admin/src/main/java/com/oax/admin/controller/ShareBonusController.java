package com.oax.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ShareBonusService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.ShareBonusParam;

@RestController
@RequestMapping("shareBonus")
public class ShareBonusController {

    @Autowired
    private ShareBonusService shareBonusService;

    @RequestMapping("benchmarksIndex")
    public ResultResponse benchmarksIndex(@RequestBody ShareBonusParam param){
        Map<String, Object> shareBonusInfos = shareBonusService.getShareBonusInfos(param);
        return new ResultResponse(true, shareBonusInfos);
    }

    @RequestMapping("noBenchmarksIndex")
    public ResultResponse noBenchmarksIndex(@RequestBody ShareBonusParam param){
        PageInfo<?> page = shareBonusService.getPage(param);
        return new ResultResponse(true, page);
    }
}
