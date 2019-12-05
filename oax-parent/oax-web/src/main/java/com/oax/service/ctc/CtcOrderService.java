package com.oax.service.ctc;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.enums.CtcOrderType;
import com.oax.entity.front.vo.CtcBuyVo;
import com.oax.entity.front.vo.CtcOrderDetailVo;
import com.oax.entity.front.vo.CtcOrderForMerchantVo;
import com.oax.entity.front.vo.CtcOrderForUserVo;

import java.math.BigDecimal;

public interface CtcOrderService {

    CtcOrderDetailVo buy(Integer ctcAdvertId, BigDecimal qty);

    void sale(Integer ctcAdvertId, BigDecimal qty, String smsCode, String emailCode);

    void cancel(Long id);

    void payed(Long id);

    void appeal(Long id, String appealDesc);

    void finish(Long id);

    CtcOrderDetailVo getDetailVoById(Long id);

    PageInfo<CtcOrderForUserVo> pageForUserWeb(PageParam pageParam, CtcOrderType type);

    PageInfo<CtcOrderForMerchantVo> pageForMerchantWeb(PageParam pageParam, CtcOrderType type);

    PageInfo<CtcOrderForMerchantVo> pageForMerchantManage(PageParam pageParam, CtcOrderType type);

    void cancelAppeal(Long id);

    //订单超时，取消订单
    void timeoutCancel(Long id);
}
