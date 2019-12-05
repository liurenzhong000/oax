package com.oax.eth.scheduled;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.eth.service.CoinService;
import com.oax.eth.utils.EthRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/29
 * Time: 15:48
 * gasPrice and gaslimit 更新
 */
@Component
@Slf4j
public class GasPriceAndGasLimitScheduled {


    @Autowired
    private CoinService coinService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;
  public void updateGasPriceAndGasLimit(){


        try {
            int ethGasPrice = ethRPCApiUtils.ethGasPrice();

            int gasPrice = (int)Math.round(ethGasPrice * 1.5);

            int update = coinService.updateAllEHTAndTokenGasPrice(gasPrice);

            if (update>0){
                log.info("-----更新gasPrice价格{},成功-----",gasPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----更新gasPrice价格,失败-----",e);
        }


        List<Coin> coinList = coinService.selectByType(CoinTypeEnum.ETH.getType());
        List<Coin> tokenCoinList = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());

        coinList.addAll(tokenCoinList);


        for (Coin coin : coinList) {
            String from = coin.getMainAddress();
            Integer gasPrice = coin.getGasPrice();
            String to = coin.getColdAddress();
            Integer gasLimit = coin.getGasLimit();
            BigDecimal qty = coin.getOutQtyToColdAddress();
            String ethHexQty = EthRPCApiUtils.dbNum2EthNum(qty,coin.getDecimals());

            String gasPriceETHhex = EthRPCApiUtils.dbGasPrice2EthGasPrice(gasPrice);
//        log.info("gasPriceETHhex::{}",gasPriceETHhex);
            String gasLimitETHhex = EthRPCApiUtils.toHexString(new BigInteger(gasLimit+""));
//        log.info("gasLimitETHhex::{}",gasLimitETHhex);
            try {
                String estimateGas = ethRPCApiUtils.getGasLimit(
                        coin.getContractAddress(),
                        coin.getMethodId(),
                        from,
                        to,
                        ethHexQty,
                        gasLimitETHhex,
                        gasPriceETHhex);

                BigInteger bigInteger = EthRPCApiUtils.toNum(estimateGas);
                BigInteger multiply = bigInteger.multiply(new BigInteger("2"));
                coin.setGasLimit(multiply.intValue());



                coinService.update(coin);
                log.info("更新GasLimit币种:{},GasLimit为{}",coin.getShortName(),multiply);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("----更新币种:{},GasLimit失败----",coin.getShortName(),e);
            }
//            coin.getType()
        }

    }
}
