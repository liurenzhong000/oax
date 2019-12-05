package com.oax.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.entity.activity.PanicBuyOrder;
import com.oax.vo.PanicBuyOrderPostVo;

public interface PanicBuyOrderService {

    PanicBuyOrderPostVo buyPageData(Integer userId, Integer activity);

    PanicBuyOrderPostVo buy(Integer userId, Integer orderId, String transactionPassword);

    PageInfo<PanicBuyOrder> page(Integer userId, Integer pageNum, Integer pageSize);

}
