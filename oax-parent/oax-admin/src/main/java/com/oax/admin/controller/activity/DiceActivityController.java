package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.DiceBackendWinForm;
import com.oax.admin.service.activity.DiceActivityService;
import com.oax.admin.service.activity.DiceIncomeWinService;
import com.oax.admin.vo.DiceStatisticsData;
import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.DiceIncomeWin;
import com.oax.entity.admin.param.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2018/12/27 13:48
 * @Description:
 */
@RestController
@RequestMapping("/activity/dice")
public class DiceActivityController {

    @Autowired
    private DiceActivityService diceActivityService;

    //各用户获利详情
    @GetMapping(path = "/statistics")
    public ResultResponse getPayoutData(Integer userId, Integer coinId, PageParam pageParam){
        DiceStatisticsData data = diceActivityService.statistics(userId, coinId, pageParam);
        return new ResultResponse(data);
    }

    //投注记录
    @GetMapping(path = "/list")
    public ResultResponse list(Integer userId, Integer coinId, PageParam pageParam){
        PageInfo pageInfo = diceActivityService.list(userId, coinId, pageParam);
        return new ResultResponse(pageInfo);
    }

}
