package com.oax.admin.controller;


import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ctc.MerchantService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.MerchantParam;
import com.oax.entity.admin.vo.MerchantVo;
import com.oax.entity.ctc.Merchant;
import com.oax.entity.enums.MerchantStatus;
import com.oax.entity.enums.PaymentWay;
import com.oax.entity.front.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 商户管理
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    /**
     * 获取商户信息列表
     */
    @GetMapping("/page")
    public ResultResponse page(MerchantParam param) {
        PageInfo<MerchantVo> page = merchantService.pageForAdmin(param);
        return new ResultResponse(true, page);
    }

    /**
     * 添加商户
     */
    @PostMapping
    public ResultResponse saveOne(Integer userId) {
        Merchant merchant = new Merchant();
        merchant.setUserId(userId);
        merchant.setCreateDate(new Date());
        merchant.setPaymentWay(PaymentWay.BANK_CARD);
        merchant.setStatus(MerchantStatus.NORMAL);
        merchantService.saveOne(merchant);
        return new ResultResponse(true, "添加成功");
    }

    /**
     * 删除商户
     */
    @DeleteMapping("/{id}")
    public ResultResponse deleteOne(@PathVariable Integer id) {
        merchantService.deleteOne(id);
        return new ResultResponse(true, "删除成功");
    }

}
