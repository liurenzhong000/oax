package com.oax.controller;

import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.entity.ctc.BankCard;
import com.oax.entity.front.vo.BankCardVo;
import com.oax.form.BankCardForm;
import com.oax.service.ctc.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 用户银行卡设置
 */
@RestController
@RequestMapping("/bankCard")
public class BankCardController {

    @Autowired
    private BankCardService bankCardService;

    @PostMapping
    public ResultResponse saveOne(BankCardForm form) {
        AssertHelper.notEmpty(form.getCardNo(), "银行卡号不能为空");
        AssertHelper.notEmpty(form.getBankBranch(), "开户支行");
        AssertHelper.notEmpty(form.getBankName(), "开户银行名称");
        AssertHelper.notEmpty(form.getCity(), "开户省市");
        AssertHelper.notEmpty(form.getRealName(), "开户用户名称");
        BankCard bankCard = BeanHepler.copy(form, BankCard.class);
        bankCard.setCreateDate(new Date());
        bankCard.setUserId(HttpContext.getUserId());
        bankCardService.saveOne(bankCard);
        return new ResultResponse(true, "添加成功");
    }

    @GetMapping("/list")
    public ResultResponse list(){
        Integer userId = HttpContext.getUserId();
        List<BankCardVo> bankCardVoList = bankCardService.listByUserId(userId);
        return new ResultResponse(true, bankCardVoList);
    }

    /**
     * 设置默认银行卡
     */
    @PutMapping("/{id}/setDefault")
    public ResultResponse setDefault(@PathVariable("id") Integer id){
        Integer userId = HttpContext.getUserId();
        bankCardService.setDefault(id, userId);
        return new ResultResponse(true, "设置成功");
    }

    /**
     * 设置默认银行卡
     */
    @DeleteMapping("/{id}")
    public ResultResponse deleteOne(@PathVariable("id") Integer id){
        Integer userId = HttpContext.getUserId();
        bankCardService.deleteOne(id, userId);
        return new ResultResponse(true, "删除成功");
    }

}
