package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.DiceRollUnderWinForm;
import com.oax.admin.form.ListDiceRollUnderWinForm;
import com.oax.admin.service.activity.DiceRollUnderWinService;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.DiceRollUnderWin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 14:58
 * @Description:
 */
@RestController
@RequestMapping("/activity/diceRollUnderWin")
public class DiceRollUnderWinController {

    @Autowired
    private DiceRollUnderWinService diceRollUnderWinService;

    @GetMapping(path = "/list")
    public ResultResponse list(ListDiceRollUnderWinForm form){
        PageInfo pageInfo = diceRollUnderWinService.list(form);
        return new ResultResponse(pageInfo);
    }

    @PostMapping
    public ResultResponse saveOne(DiceRollUnderWinForm form){
        AssertHelper.notEmpty(form.getCoinId(), "币种id不能为空");
        AssertHelper.notEmpty(form.getMinRollUnder(), "最小投注数不能为空");
        AssertHelper.notEmpty(form.getMaxRollUnder(), "最大投注数不能为空");
        AssertHelper.notEmpty(form.getBackWin(), "控制概率不能为空");
        AssertHelper.notEmpty(form.getStatus(), "开启状态不能为空");
        AssertHelper.isTrue(form.getBackWin() >=0 && form.getBackWin()<=100, "概率在0-100之间");
        DiceRollUnderWin entity = BeanHepler.copy(form, DiceRollUnderWin.class);
        diceRollUnderWinService.saveOne(entity);
        return new ResultResponse(true, "新增成功");
    }

    @PutMapping("/{id}")
    public ResultResponse updateOne(@PathVariable Integer id, DiceRollUnderWinForm form){
        DiceRollUnderWin entity = BeanHepler.copy(form, DiceRollUnderWin.class);
        entity.setId(id);
        diceRollUnderWinService.updateOne(entity);
        return new ResultResponse(true, "更新成功");
    }
    @DeleteMapping("/{id}")
    public ResultResponse deleteOne(@PathVariable Integer id){
        diceRollUnderWinService.deleteOne(id);
        return new ResultResponse(true, "删除成功");
    }
}
