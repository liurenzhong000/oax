package com.oax.scheduled.omni;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.common.SMSUtil;
import com.oax.entity.front.Coin;
import com.oax.service.CoinService;
import com.oax.utils.UsdtRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/13
 * Time: 14:47
 *
 * 余额不足提醒
 */
@Component
@Slf4j
public class OmniScanWaningQtyScheduled {
    @Autowired
    private CoinService coinService;

    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;

    @Value("${send.warning.phone1}")
    private String phone1;

    @Value("${send.warning.phone2}")
    private String phone2;

    /**
     * 每三十分钟执行
     */
    @Scheduled(cron="0 0/30 * * * ?")
    @Async
    public void scanWaningQty(){


        log.error("-------------Omni-执行scanWaningQty------------");
        List<Coin> usdtTokenCoin = coinService.selectUsdtCoin();
        for (Coin tokenCoin : usdtTokenCoin) {
            try {
                BigDecimal balance = usdtRPCApiUtils.getbalance(tokenCoin.getMainAddress(),tokenCoin.getPropertyid());

                BigDecimal warningQty = tokenCoin.getWarningQty();

                if (warningQty.compareTo(balance)>0){
                    SMSUtil.sendCode(phone1,"尊敬的用户，您的"+tokenCoin.getShortName()+"余额为"+balance+"！");
                    SMSUtil.sendCode(phone2,"尊敬的用户，您的"+tokenCoin.getShortName()+"余额为"+balance+"！");
                }
            } catch (Exception e) {
                log.error("scanWaningQty出错.",e);
            }
        }

        log.error("-------------Omni-执行scanWaningQty结束------------");

    }
}
