package com.oax.util;

import com.oax.common.DateHelper;
import com.oax.mapper.ctc.CtcOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Transactional
public class CtcOrderIdFactory {

    @Autowired
    private CtcOrderMapper ctcOrderMapper;

    /**
     * 1000 1108 1
     * 商户id 不足4位，后面补0 + 月日 + 该商户第几单
     * //ps 可以用redis实现对应商户的单数计数
     */
    public Long generateOrderId(Integer merchantId) {
        String merchantIdStr = merchantId >= 10000 ? merchantId.toString() : new StringBuffer(String.format("%05d", merchantId)).reverse().toString();
        Integer leftLen = merchantIdStr.length();
        String mdStr = DateHelper.format(new Date(), DateTimeFormatter.ofPattern("MMdd"));
        Long maxOrderId = ctcOrderMapper.getMaxOrderIdByMerchantId(Integer.parseInt(merchantIdStr), leftLen);
        if (maxOrderId != null) {
            return maxOrderId + 1;
        }
        return Long.parseLong(merchantIdStr + mdStr + 1);
    }

    public static void main(String[] args) {
        Integer merchantId = 111000;
        String merchantIdStr = merchantId >= 10000 ? merchantId.toString() : new StringBuffer(String.format("%05d", merchantId)).reverse().toString();
        System.out.println(merchantIdStr);
    }
}

