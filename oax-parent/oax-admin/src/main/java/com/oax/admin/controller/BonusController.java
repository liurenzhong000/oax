package com.oax.admin.controller;


import com.github.pagehelper.PageInfo;
import com.oax.admin.service.bonus.BHBBonusService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.BonusParam;
import com.oax.entity.front.Bonus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分红详情类别
 */
@RequestMapping("/bonus")
@RestController
public class BonusController {

    @Autowired
    private BHBBonusService bonusService;

    @PostMapping("/list")
    public ResultResponse list(@RequestBody BonusParam bonusParam) {
        PageInfo<Bonus> bonus = bonusService.pageForAdmin(bonusParam);
        return new ResultResponse(true, bonus);
    }
}
