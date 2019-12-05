package com.oax.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.ShareBonusInfo;
import com.oax.service.ShareBonusService;

@RestController
@RequestMapping("shareBonus")
public class ShareBonusController {
    @Autowired
    private ShareBonusService service;

    @RequestMapping("index")
    public ResultResponse index(){
        Map<String,Object> result = new HashMap<>();
        List<ShareBonusInfo> shareBonusInfoList = service.getShareBonusInfoList();
        List<Map<String, Object>> marketShareBonusInfoList = service.getMarketShareBonusInfoList();
        result.put("shareBonusList", shareBonusInfoList);
        result.put("marketShareBonusList", marketShareBonusInfoList);
        return new ResultResponse(true, result);
    }
}
