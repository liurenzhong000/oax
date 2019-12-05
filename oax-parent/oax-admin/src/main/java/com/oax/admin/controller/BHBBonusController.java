package com.oax.admin.controller;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.BonusExcelForm;
import com.oax.admin.form.UserCoinSnapshootLightForm;
import com.oax.admin.service.activity.UserCoinSnapshootLightService;
import com.oax.admin.service.bonus.BHBBonusService;
import com.oax.admin.service.bonus.BonusLogService;
import com.oax.admin.vo.BonusDataVo;
import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台手动BHB分红
 */
@RequestMapping("/BHBBonus")
@RestController
public class BHBBonusController {

    @Autowired
    private BHBBonusService bonusService;

    @Autowired
    private BonusLogService bonusLogService;

    @Autowired
    private UserCoinSnapshootLightService userCoinSnapshootLightService;

    @GetMapping("/start/test")
    public ResultResponse startTest(Integer day,Double bhbUSDTratio,Double oneLevelBonus,
                                    Double twoLevelBonus,Double threeLevelBonus,Double selfLevelBonus) {
        bonusService.BHBBonus("", day,bhbUSDTratio,oneLevelBonus,twoLevelBonus,threeLevelBonus,selfLevelBonus);
        return new ResultResponse(true, null);
    }

    @GetMapping("/start")
    public ResultResponse start(String password, Integer day,Double bhbUSDTratio,Double oneLevelBonus,
                                Double twoLevelBonus,Double threeLevelBonus,Double selfLevelBonus) {
        bonusService.BHBBonus(password, day,bhbUSDTratio,oneLevelBonus,twoLevelBonus,threeLevelBonus,selfLevelBonus);
        return new ResultResponse(true, null);
    }

    @GetMapping("/excel")
    public void excel(BonusExcelForm form) {
        AssertHelper.notEmpty(form.getBhbUsdtRatio(), "bhb / usdt 比例");
        AssertHelper.notEmpty(form.getAllBonus(), "总分红量");
        AssertHelper.notEmpty(form.getReachCount(), "达标人数");
        AssertHelper.notEmpty(form.getStartTime(), "分红操作开始时间");
        AssertHelper.notEmpty(form.getEndTime(), "分红操作结束时间");
        bonusLogService.exportExcel(form.getBhbUsdtRatio(), form.getAllBonus(), form.getReachCount(), form.getStartTime(), form.getEndTime());
    }

    @GetMapping("/bonusLog")
    public ResultResponse bonusLog(PageParam pageParam){
        PageInfo pageInfo = bonusLogService.pageByRedis(pageParam);
        return new ResultResponse(pageInfo);
    }

    @GetMapping("/userCoinSnapshootLight")
    public ResultResponse userCoinSnapshootLight(UserCoinSnapshootLightForm form){
        AssertHelper.notEmpty(form.getUserId(), "用户id不能为空");
        AssertHelper.notEmpty(form.getStartTime(), "开始时间不能为空");
        AssertHelper.notEmpty(form.getEndTime(), "结束时间不能为空");
        PageInfo pageInfo = userCoinSnapshootLightService.pageForAdmin(form);
        return new ResultResponse(pageInfo);
    }

}
