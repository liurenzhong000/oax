package com.oax.admin.controller.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.DiceBetQtyWinForm;
import com.oax.admin.form.ListDiceBetQtyWinForm;
import com.oax.admin.service.activity.DiceBetQtyWinService;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.DiceBetQtyWin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 14:48
 * @Description:
 */
@RestController
@RequestMapping("/activity/diceBetQtyWin")
public class DiceBetQtyWinController{

    @Autowired
    private DiceBetQtyWinService diceBetQtyWinService;

    @GetMapping(path = "/list")
    public ResultResponse list(ListDiceBetQtyWinForm form){
        PageInfo pageInfo = diceBetQtyWinService.list(form);
        return new ResultResponse(pageInfo);
    }

    @PostMapping
    public ResultResponse saveOne(DiceBetQtyWinForm form){
        AssertHelper.notEmpty(form.getCoinId(), "币种id不能为空");
        AssertHelper.notEmpty(form.getMinBetQty(), "最小投注个数不能为空");
        AssertHelper.notEmpty(form.getMaxBetQty(), "最大投注个数不能为空");
        AssertHelper.notEmpty(form.getBackWin(), "控制概率不能为空");
        AssertHelper.notEmpty(form.getStatus(), "开启状态不能为空");
        AssertHelper.isTrue(form.getBackWin() >=0 && form.getBackWin()<=100, "概率在0-100之间");
        DiceBetQtyWin entity = BeanHepler.copy(form, DiceBetQtyWin.class);
        diceBetQtyWinService.saveOne(entity);
        return new ResultResponse(true, "新增成功");
    }

    @PutMapping("/{id}")
    public ResultResponse updateOne(@PathVariable Integer id, DiceBetQtyWinForm form){
        DiceBetQtyWin entity = BeanHepler.copy(form, DiceBetQtyWin.class);
        entity.setId(id);
        diceBetQtyWinService.updateOne(entity);
        return new ResultResponse(true, "更新成功");
    }
    @DeleteMapping("/{id}")
    public ResultResponse deleteOne(@PathVariable Integer id){
        diceBetQtyWinService.deleteOne(id);
        return new ResultResponse(true, "删除成功");
    }

}
