package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.DiceBetQtyWinForm;
import com.oax.admin.form.ListDiceBetQtyWinForm;
import com.oax.admin.form.ListDiceRandomRateForm;
import com.oax.admin.service.activity.DiceBetQtyWinService;
import com.oax.admin.service.activity.DiceRandomRateService;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.DiceBetQtyWin;
import com.oax.entity.activity.DiceRandomRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 14:48
 * @Description:
 */
@RestController
@RequestMapping("/activity/diceRandomRate")
public class DiceRandomRateController{

    @Autowired
    private DiceRandomRateService diceRandomRateService;

    @GetMapping(path = "/list")
    public ResultResponse list(ListDiceRandomRateForm form){
        PageInfo pageInfo = diceRandomRateService.list(form);
        return new ResultResponse(pageInfo);
    }

    @PostMapping
    public ResultResponse saveOne(Integer coinId, Integer randomRate){
        AssertHelper.notEmpty(coinId, "币种id不能为空");
        AssertHelper.notEmpty(randomRate, "倍率不能为空");
        AssertHelper.isTrue(randomRate >=0 && randomRate<=400, "概率在0-400之间");
        DiceRandomRate entity = new DiceRandomRate();
        entity.setCoinId(coinId);
        entity.setRandomRate(randomRate);
        diceRandomRateService.saveOne(entity);
        return new ResultResponse(true, "新增成功");
    }

    @PutMapping("/{id}")
    public ResultResponse updateOne(@PathVariable Integer id,
                                    @RequestParam(required = false) Integer coinId, @RequestParam(required = false)Integer randomRate){
        DiceRandomRate entity = new DiceRandomRate();
        entity.setId(id);
        if (coinId != null) entity.setCoinId(coinId);
        if (coinId != null) entity.setRandomRate(randomRate);
        diceRandomRateService.updateOne(entity);
        return new ResultResponse(true, "更新成功");
    }

    @DeleteMapping("/{id}")
    public ResultResponse deleteOne(@PathVariable Integer id){
        diceRandomRateService.deleteOne(id);
        return new ResultResponse(true, "删除成功");
    }
}