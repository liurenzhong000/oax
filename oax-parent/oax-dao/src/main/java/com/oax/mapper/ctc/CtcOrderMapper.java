package com.oax.mapper.ctc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.admin.param.CtcOrderParam;
import com.oax.entity.admin.vo.CtcOrderVo;
import com.oax.entity.ctc.CtcOrder;
import com.oax.entity.enums.CtcOrderType;
import com.oax.entity.front.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CtcOrderMapper extends BaseMapper<CtcOrder> {

    CtcBuyVo getCtcBuyVo(Long ctcOrderId);

    List<CtcOrderVo> pageForBuy(CtcOrderParam param);

    List<CtcOrderVo> pageForSale(CtcOrderParam param);

    CtcOrderDetailVo getDetailVOById(Long id);

    List<CtcOrderForUserVo> pageForUserWeb(@Param("userId") Integer userId, @Param("type") CtcOrderType type);

    List<CtcOrderForMerchantVo> pageForMerchantWeb(@Param("userId") Integer userId, @Param("type") CtcOrderType type);

    List<CtcOrderForMerchantVo> pageForMerchantManage(@Param("userId") Integer userId, @Param("type") CtcOrderType type);

    Long getMaxOrderIdByMerchantId(@Param("merchantId") Integer merchantId, @Param("leftLen") Integer leftLen);

    Integer getLastUserBuyOrderMerchantId();

    Integer getLastUserSaleOrderMerchantId();

    Integer getLastUserBuyOrderAdvertId();

    Integer getLastUserSaleOrderAdvertId();

    int countNewOrder(@Param("userId") Integer userId, @Param("lastRefreshDate") Date lastRefreshDate);

    boolean hasNoFinishOrder(Integer userId);

    List<CtcOrderAdminVo> listFinishByUserId(Integer userId);
}
