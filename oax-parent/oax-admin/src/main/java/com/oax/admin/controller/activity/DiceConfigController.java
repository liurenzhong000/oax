package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.DiceConfigForm;
import com.oax.admin.service.activity.DiceConfigService;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.DiceConfig;
import com.oax.entity.admin.param.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 17:22
 * @Description: DICE币种配置
 */
@RestController
@RequestMapping("/activity/diceConfig")
public class DiceConfigController{

    @Autowired
    private DiceConfigService diceConfigService;

    @GetMapping(path = "/list")
    public ResultResponse list(PageParam pageParam){
        PageInfo pageInfo = diceConfigService.list(pageParam);
        return new ResultResponse(pageInfo);
    }

    @PostMapping
    public ResultResponse saveOne(DiceConfigForm form){
        AssertHelper.notEmpty(form.getCoinId(), "币种id不能为空");
        AssertHelper.notEmpty(form.getMinBetQty(), "最小投注数不能为空");
        AssertHelper.notEmpty(form.getMaxBetQty(), "最大投注数不能为空");
        AssertHelper.notEmpty(form.getDecimals(), "精度不能为空");
        DiceConfig entity = BeanHepler.copy(form, DiceConfig.class);
        diceConfigService.saveOne(entity);
        return new ResultResponse(true, "新增成功");
    }

    @PutMapping("/{id}")
    public ResultResponse updateOne(@PathVariable Integer id, DiceConfigForm form){
        DiceConfig entity = BeanHepler.copy(form, DiceConfig.class);
        entity.setId(id);
        diceConfigService.updateOne(entity);
        return new ResultResponse(true, "更新成功");
    }

    @DeleteMapping("/{id}")
    public ResultResponse deleteOne(@PathVariable Integer id){
        diceConfigService.deleteOne(id);
        return new ResultResponse(true, "删除成功");
    }
}
