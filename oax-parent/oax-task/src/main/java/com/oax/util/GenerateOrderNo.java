package com.oax.util;

import com.alibaba.druid.util.StringUtils;
import com.oax.entity.front.MovesayMoneyActive;
import com.oax.service.MovesayMoneyActiveListService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateOrderNo {


    @Autowired
    private MovesayMoneyActiveListService movesayMoneyActiveListService;

    public String getId(MovesayMoneyActive movesayMoneyActive, String no) {
        Long orderId = 0L;

        if (!StringUtils.isEmpty(no)) {
            Long i = Long.parseLong(no);
            orderId = i + 1;
            return "" + orderId;
        } else {
            //获取活动类型
            Integer type = movesayMoneyActive.getType();

            //获取币种
            Integer market = movesayMoneyActive.getMarketId();

            //获取活动id
            Integer activeCreateId = movesayMoneyActive.getId();

            //创建时间
            Date createtime = movesayMoneyActive.getCreateTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String time = sdf.format(createtime);
            orderId = 1L;
            return "" + type + activeCreateId + market + time + orderId;
        }

    }

    public String getOperateId(String createTime, String no,Integer userId) {
        Long orderId = 0L;

        if (!StringUtils.isEmpty(no)) {
            Long i = Long.parseLong(no);
            orderId = i + 1;
            return "" + orderId;
        } else {
            orderId = 1L;
            return ""+userId +createTime + orderId;
        }

    }
}

