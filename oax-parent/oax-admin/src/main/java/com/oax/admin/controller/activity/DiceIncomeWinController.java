package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.activity.DiceIncomeWinService;
import com.oax.common.AssertHelper;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.DiceIncomeWin;
import com.oax.entity.admin.param.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 15:43
 * @Description:
 */
@RestController
@RequestMapping("/activity/dice/incomeWin")
public class DiceIncomeWinController {

    @Autowired
    private DiceIncomeWinService diceIncomeWinService;

    //后台根据用户收益来调控
    @GetMapping(path = "/list")
    public ResultResponse incomeWinList(Integer coinId, PageParam pageParam){
        PageInfo pageInfo = diceIncomeWinService.list(coinId, pageParam);
        return new ResultResponse(pageInfo);
    }

    @PostMapping
    public ResultResponse saveIncome(DiceIncomeWin incomeWin){
        AssertHelper.notEmpty(incomeWin.getBackWin(), "概率不能为空");
        AssertHelper.notEmpty(incomeWin.getMinIncome(), "收益下限不能为空");
        AssertHelper.notEmpty(incomeWin.getMaxIncome(), "收益上限不能为空");
        AssertHelper.notEmpty(incomeWin.getStatus(), "是否开启不能为空");
        diceIncomeWinService.saveOne(incomeWin);
        return new ResultResponse(true, "新增成功");
    }

    @PutMapping
    public ResultResponse updateIncome(DiceIncomeWin incomeWin){
        AssertHelper.notEmpty(incomeWin.getId(), "id不能为空");
        diceIncomeWinService.updateOne(incomeWin);
        return new ResultResponse(true, "更新成功");
    }

    @DeleteMapping("/{id}")
    public ResultResponse deleteOne(@PathVariable Integer id){
        diceIncomeWinService.deleteOne(id);
        return new ResultResponse(true, "删除成功");
    }

}
