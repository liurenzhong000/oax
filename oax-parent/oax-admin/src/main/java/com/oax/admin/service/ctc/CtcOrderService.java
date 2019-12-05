package com.oax.admin.service.ctc;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.CtcOrderAdminParam;
import com.oax.entity.admin.param.CtcOrderParam;
import com.oax.entity.admin.vo.CtcOrderVo;
import com.oax.entity.front.vo.CtcOrderAdminVo;

public interface CtcOrderService {
    PageInfo<CtcOrderVo> pageForBuy(CtcOrderParam param);

    PageInfo<CtcOrderVo> pageForSale(CtcOrderParam param);

    void processing(Long id);

    void finish(Long id);

    void close(Long id);

    //关闭申诉，返回原来的订单状态
    void closeAppeal(Long id);

    //获取用户法币买入购买详情
    PageInfo<CtcOrderAdminVo> listFinishByUserId(CtcOrderAdminParam param);

}
