package com.oax.controller;

import com.github.pagehelper.PageInfo;
import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.enums.CtcOrderType;
import com.oax.entity.front.vo.CtcBuyVo;
import com.oax.entity.front.vo.CtcOrderDetailVo;
import com.oax.entity.front.vo.CtcOrderForMerchantVo;
import com.oax.entity.front.vo.CtcOrderForUserVo;
import com.oax.service.ctc.CtcOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 商户订单相关接口
 */
@RestController
@RequestMapping("/ctcOrder")
public class CtcOrderController {

    @Autowired
    private CtcOrderService ctcOrderService;



    /**
     * 用户买入
     * @param ctcAdvertId 广告id
     * @param qty 购买数量
     * @return
     */
    @PostMapping("/buy")
    public ResultResponse buy(Integer ctcAdvertId, BigDecimal qty){
        CtcOrderDetailVo detailVo = null;
        try{
            detailVo = ctcOrderService.buy(ctcAdvertId, qty);
        }catch (Exception e){
            return new ResultResponse(false,e.getMessage());
        }

        return new ResultResponse(true, "订单创建成功", detailVo);
    }

    /**
     * 获取订单详情数据
     */
    @GetMapping("/{id}")
    public ResultResponse getDetailVOById(@PathVariable Long id){
        CtcOrderDetailVo data = ctcOrderService.getDetailVoById(id);
        return new ResultResponse(true, data);
    }







    /**
     * 取消订单
     * @param id 订单id
     * @return
     */
    @DeleteMapping("/{id}/cancel")
    public ResultResponse cancel(@PathVariable Long id) {
        ctcOrderService.cancel(id);
        return new ResultResponse(true, "取消成功");
    }

    /**
     * 银行卡转账后，付款方点击已付款
     * @param id 订单id
     * @return
     */
    @PutMapping("/{id}/payed")
    public ResultResponse payed(@PathVariable Long id) {
        ctcOrderService.payed(id);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 用户卖出
     * @param ctcAdvertId 广告id
     * @param qty 卖出数量
     * @return
     */
    @PostMapping("/sale")
    public ResultResponse sale(Integer ctcAdvertId, BigDecimal qty, String smsCode, String emailCode) {
        ctcOrderService.sale(ctcAdvertId, qty, smsCode, emailCode);
        return new ResultResponse(true, "卖出成功");
    }



    /**
     * 用户申诉
     * @param id 订单id
     * @param appealDesc 申诉说明
     * @return
     */
    @PutMapping("/{id}/appeal")
    public ResultResponse appeal(@PathVariable Long id, @RequestParam(required = true) String appealDesc) {
        ctcOrderService.appeal(id, appealDesc);
        return new ResultResponse(true, "提交申诉成功");
    }

    /**
     * 用户撤销申诉
     * @param id 订单id
     * @return
     */
    @DeleteMapping("/{id}/appeal")
    public ResultResponse cancelAppeal(@PathVariable Long id) {
        ctcOrderService.cancelAppeal(id);
        return new ResultResponse(true, "撤销申诉成功");
    }

    /**
     * 收款方放币操作
     * @param id 订单id
     * @return
     */
    @PutMapping("/{id}/finish")
    public ResultResponse finish(@PathVariable Long id) {
        ctcOrderService.finish(id);
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 用户交易记录
     */
    @GetMapping("/pageForUserWeb")
    public ResultResponse pageForUserWeb(PageParam pageParam, CtcOrderType type) {
        PageInfo<CtcOrderForUserVo> page = ctcOrderService.pageForUserWeb(pageParam, type);
        return new ResultResponse(true, page);
    }

    /**
     * 商户交易记录
     */
    @GetMapping("/pageForMerchantWeb")
    public ResultResponse pageForMerchantWeb(PageParam pageParam, CtcOrderType type) {
        PageInfo<CtcOrderForMerchantVo> pageInfo = ctcOrderService.pageForMerchantWeb(pageParam, type);
        return new ResultResponse(true, pageInfo);
    }

    /**
     * 商户订单管理
     */
    @GetMapping("/pageForMerchantManage")
    public ResultResponse pageForMerchantManage(PageParam pageParam, CtcOrderType type) {
        PageInfo<CtcOrderForMerchantVo> pageInfo = ctcOrderService.pageForMerchantManage(pageParam, type);
        return new ResultResponse(true, pageInfo);
    }
}
