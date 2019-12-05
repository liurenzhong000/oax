package com.oax.scheduled;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.SMSUtil;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.service.CoinService;
import com.oax.utils.BtcRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/29
 * Time: 18:07
 * 扫描 余额是否超过预警值
 */
@Component
@Slf4j
public class ScanWaningQtyScheduled {


    @Autowired
    private CoinService coinService;

    @Autowired
    private BtcRPCApiUtils btcRPCApiUtils;

    @Value("${send.warning.phone1}")
    private String phone1;

    @Value("${send.warning.phone2}")
    private String phone2;

    @Value("${send.warning.smsTemplate}")
    private String smsTemplate;

    /**
     * 每三十分钟执行
     */
    @Scheduled(cron="0 0/30 * * * ?")
    @Async
    public void scanWaningQty(){

        log.error("-------------执行scanWaningQty------------");
        List<Coin> btcCoins = coinService.selectByType(CoinTypeEnum.BTC.getType());

        for (Coin btcCoin : btcCoins) {
            BigDecimal warningQty = btcCoin.getWarningQty();

            try {
                String getbalance = btcRPCApiUtils.getbalance(null);
                if (warningQty.compareTo(new BigDecimal(getbalance))>0){
                    SMSUtil.sendCode(phone1,"尊敬的用户，您的"+btcCoin.getShortName()+"余额为"+getbalance+"！");
                    SMSUtil.sendCode(phone2,"尊敬的用户，您的"+btcCoin.getShortName()+"余额为"+getbalance+"！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        log.error("-------------执行scanWaningQty结束------------");
    }
}
