package com.oax.admin.controller;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ctc.CtcOrderService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.CtcOrderAdminParam;
import com.oax.entity.admin.param.CtcOrderParam;
import com.oax.entity.admin.vo.CtcOrderVo;
import com.oax.entity.front.vo.CtcOrderAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CTC 订单
 */
@RestController
@RequestMapping("/ctcOrder")
public class CtcOrderController {

    @Autowired
    private CtcOrderService ctcOrderService;
    /**
     * 买入记录
     */
    @GetMapping("/page/buy")
    public ResultResponse pageForBuy(CtcOrderParam param) {
        PageInfo<CtcOrderVo> page = ctcOrderService.pageForBuy(param);
        return new ResultResponse(true, page);
    }

    /**
     * 卖出记录
     */
    @GetMapping("/page/sale")
    public ResultResponse pageForSale(CtcOrderParam param) {
        PageInfo<CtcOrderVo> page = ctcOrderService.pageForSale(param);
        return new ResultResponse(true, page);
    }

    /**
     * 处理申诉中-->处理中PROCESSING， 表示平台正在处理
     */
    @PutMapping("/{id}/processing")
    public ResultResponse processing(@PathVariable Long id) {
        ctcOrderService.processing(id);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 确定到账：平台处理申诉，处理中 --> 交易完成        代替收款方进行放币操作
     */
    @PutMapping("/{id}/finish")
    public ResultResponse finish(@PathVariable Long id) {
        ctcOrderService.finish(id);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 关闭交易
     */
    @PutMapping("/{id}/close")
    public ResultResponse close(@PathVariable Long id) {
        ctcOrderService.close(id);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 关闭申诉
     */
    @PutMapping("/{id}/closeAppeal")
    public ResultResponse closeAppeal(@PathVariable Long id) {
        ctcOrderService.closeAppeal(id);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 获取用户法币买入购买详情
     */
    @PostMapping("/listFinish")
    public ResultResponse listFinishByUserId(@RequestBody CtcOrderAdminParam param) {
        PageInfo<CtcOrderAdminVo> ctcOrderAdminVoPageInfo = ctcOrderService.listFinishByUserId(param);
        return new ResultResponse(true, ctcOrderAdminVoPageInfo);
    }

}
