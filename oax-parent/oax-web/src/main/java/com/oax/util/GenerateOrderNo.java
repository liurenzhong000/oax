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
        String newEquipmentNo = "0000001";
        //创建时间
        String type=""+movesayMoneyActive.getType()+movesayMoneyActive.getId()+movesayMoneyActive.getMarketId();
        if (!StringUtils.isEmpty(no)) {
            String operMaxId=no.replaceFirst(type,"");
            Long i = Long.parseLong(operMaxId)+1;
            return  String.format(type + "%07d", i);
        } else {
            return String.format(type + newEquipmentNo);
        }

    }

    public String getOperateId(String no,Integer userId) {
        String newEquipmentNo = "0000001";

        if (!StringUtils.isEmpty(no)) {
            String operMaxId=no.replaceFirst(String.valueOf(userId),"");
            Long i = Long.parseLong(operMaxId)+1;
            return  String.format(userId + "%07d", i);
        } else {
            return String.format(userId + newEquipmentNo);
        }

    }
}

