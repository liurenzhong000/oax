package com.oax.service;

import com.oax.OaxApiApplicationTest;
import com.oax.exception.VoException;
import com.oax.mapper.front.OrdersMapper;
import com.oax.vo.CancelOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OrdersServiceTest extends OaxApiApplicationTest {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Test
    public void cancelOrder(){
        List<Map<String, Object>> cancelOrderVOList = ordersMapper.listBhbUsdtSallOrder();
        cancelOrderVOList.forEach(item->{
            CancelOrderVO cancelOrderVO = new CancelOrderVO();
            cancelOrderVO.setId((Integer) item.get("id"));
            cancelOrderVO.setUserId((Integer) item.get("userId"));
            try {
                boolean flag = ordersService.cancelOrder(cancelOrderVO);
                log.info("orderId:{} - userId:{} - flag:{}", cancelOrderVO.getId(), cancelOrderVO.getUserId(), flag);
            } catch (VoException e) {
                e.printStackTrace();
            }
        });

    }
}
