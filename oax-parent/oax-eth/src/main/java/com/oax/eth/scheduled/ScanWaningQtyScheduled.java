package com.oax.eth.scheduled;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.common.SMSUtil;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.eth.service.CoinService;
import com.oax.eth.utils.EthRPCApiUtils;

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

    @Value("${send.warning.phone1}")
    private String phone1;

    @Value("${send.warning.phone2}")
    private String phone2;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;


    /**
     * 每三十分钟执行
     */
//    @Async
//    @Scheduled(cron="0 0/30 * * * ?")
    public void scanWaningQty(){

        log.error("--------------执行scanWaningQty---------------");



        List<Coin> ethCoinList = coinService.selectByType(CoinTypeEnum.ETH.getType());

        for (Coin coin : ethCoinList) {
            try {
                String ethBalance = ethRPCApiUtils.getETHBalance(coin.getMainAddress());
                BigDecimal balance = EthRPCApiUtils.stringToDBNum(ethBalance);
                BigDecimal warningQty = coin.getWarningQty();
                if (warningQty.compareTo(balance)>0){
                    SMSUtil.sendCode(phone1,"尊敬的用户，您的"+coin.getShortName()+"余额为"+balance+"！");
                    SMSUtil.sendCode(phone2,"尊敬的用户，您的"+coin.getShortName()+"余额为"+balance+"！");
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        List<Coin> tokenList = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());

        for (Coin coin : tokenList) {
            try {
                String ethBalance = ethRPCApiUtils.getTokenBalance(coin.getMainAddress(),coin.getContractAddress());
                BigDecimal balance = EthRPCApiUtils.stringToDBNum(ethBalance);
                BigDecimal warningQty = coin.getWarningQty();
                if (warningQty.compareTo(balance)>0){
                	SMSUtil.sendCode(phone1,"尊敬的用户，您的"+coin.getShortName()+"余额为"+balance+"！");
                    SMSUtil.sendCode(phone2,"尊敬的用户，您的"+coin.getShortName()+"余额为"+balance+"！");
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        log.error("--------------执行scanWaningQty结束---------------");
    }

}
