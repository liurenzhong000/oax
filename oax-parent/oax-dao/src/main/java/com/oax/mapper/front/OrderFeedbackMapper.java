package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.vo.FeedBackVo;
import com.oax.entity.front.OrderFeedback;
import com.oax.entity.front.Orders;
import com.oax.entity.front.TradeSnapshot;

@Mapper
public interface OrderFeedbackMapper {
    List<TradeSnapshot> getTotalOrderFeedback(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<Orders> getCompensationOrdersList(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("marketId") Integer marketId);

    BigDecimal getTotalWaitTradeQty(@Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("marketId") Integer marketId);

    Integer insertOrderFeedback(OrderFeedback orderFeedback);

    Integer updateOrdersIsFeedback(Integer id);

    List<Map<String,Object>> selectAll(FeedBackVo vo);
}
