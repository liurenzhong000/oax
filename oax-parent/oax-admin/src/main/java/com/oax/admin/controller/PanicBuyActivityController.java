package com.oax.admin.controller;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.PanicBuyActivityForm;
import com.oax.admin.service.activity.PanicBuyActivityService;
import com.oax.common.BeanHepler;
import com.oax.common.ResultResponse;
import com.oax.entity.activity.PanicBuyActivity;
import com.oax.entity.activity.PanicBuyDetail;
import com.oax.entity.activity.PanicBuyHelp;
import com.oax.entity.admin.param.PanicBuyActivityParam;
import com.oax.entity.admin.param.PanicBuyDetailParam;
import com.oax.entity.admin.param.PanicBuyHelpParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/panicBuy")
public class PanicBuyActivityController {

    @Autowired
    private PanicBuyActivityService panicBuyActivityService;

    //添加活动数据
    @PostMapping(path = "/activity")
    public ResultResponse saveOne(PanicBuyActivityForm form){
        PanicBuyActivity entity = BeanHepler.copy(form, PanicBuyActivity.class);
        panicBuyActivityService.saveOne(entity);
        return new ResultResponse(true, "添加成功");
    }

    @PutMapping(path = "/activity/{id}")
    public ResultResponse updateOne(@PathVariable Integer id, Integer participateBase, Integer reachBase){
        panicBuyActivityService.updateOne(id, participateBase, reachBase);
        return new ResultResponse(true, "修改成功");
    }

    //开始活动
    @PutMapping(path = "/activity/{id}/open")
    public ResultResponse open(@PathVariable Integer id) {
        panicBuyActivityService.open(id);
        return new ResultResponse(true, "开启成功");
    }

    // 结束活动，达标用户处理
    @PutMapping(path = "/activity/{id}/finish")
    public ResultResponse finish(@PathVariable Integer id) {
        panicBuyActivityService.finish(id);
        return new ResultResponse(true, "操作成功");
    }

    //更新未支付的订单的状态为失效
    @PutMapping(path = "/activity/{id}/orderClose")
    public ResultResponse orderFail(@PathVariable Integer id) {
        panicBuyActivityService.orderClose(id);
        return new ResultResponse(true, "操作成功");
    }

    @GetMapping(path = "/activity/list")
    public ResultResponse page(PanicBuyActivityParam param) {
        PageInfo<PanicBuyActivity> pageInfo = panicBuyActivityService.pageForAdmin(param);
        return new ResultResponse(pageInfo);
    }

    @GetMapping(path = "/detail/list")
    public ResultResponse detailPage(PanicBuyDetailParam param) {
        PageInfo<PanicBuyDetail> pageInfo = panicBuyActivityService.pageDetailForAdmin(param);
        return new ResultResponse(pageInfo);
    }

    @GetMapping(path = "/help/list")
    public ResultResponse helpPage(PanicBuyHelpParam param) {
        PageInfo<PanicBuyHelp> pageInfo = panicBuyActivityService.pageHelpForAdmin(param);
        return new ResultResponse(pageInfo);
    }

}
