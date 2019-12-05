package com.oax.eth.scheduled;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.common.Base64Utils;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.common.enums.PlatformTransferTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.PlatformTransfer;
import com.oax.eth.service.CoinService;
import com.oax.eth.service.PlatformTransferService;
import com.oax.eth.utils.EthRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/21
 * Time: 16:39
 * 平台转账- 转到冷钱包
 */
@Slf4j
@Component
public class TransferColdAddressScheduled {

    @Autowired
    private CoinService coinService;

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;

    /**
     * 每天凌晨2点
     */
    @Async
    @Scheduled(cron="0 0 2 * * ?")
    public void transferColdAddress() {


        log.error("----------执行transferColdAddress------------");

        List<Coin> ehtCoinList = coinService.selectByType(CoinTypeEnum.ETH.getType());

        for (Coin coin : ehtCoinList) {
            String mainAddress = coin.getMainAddress();
            try {
                String ethBalance = ethRPCApiUtils.getETHBalance(mainAddress);

                //平台eth余额
                BigDecimal balance = EthRPCApiUtils.stringToDBNum(ethBalance);

                if (balance.compareTo(coin.getOutQtyToColdAddress()) > 0) {

                    //需要转入冷钱包
                    String to = coin.getColdAddress();
                    //超出剩下部分
                    BigDecimal qty = balance.subtract(coin.getOutQtyToColdAddress());
                    String ethHexQty = EthRPCApiUtils.dbNum2EthNum(qty,coin.getDecimals());
                    String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(coin.getMainAddressPassword())) ;
                    Map<String,Object> params = ethRPCApiUtils.transferETH(
                            mainAddress,
                            to,
                            ethHexQty,
                            password);

                    PlatformTransfer platformTransfer = new PlatformTransfer();

                    String toEHTgasPrice = (String)params.get("gasPrice");
                    String toEHTgas = (String)params.get("gas");
                    String txHashId = (String) params.get("txHashId");
                    platformTransfer.setGasLimit(EthRPCApiUtils.toNum(toEHTgas).intValue());
                    platformTransfer.setGasPrice(EthRPCApiUtils.ethGasPrice2DbGasPrice(toEHTgasPrice));

                    platformTransfer.setCoinId(coin.getId());
                    platformTransfer.setFromAddress(mainAddress);
                    platformTransfer.setToAddress(to);
                    platformTransfer.setQty(qty);
                    platformTransfer.setType(PlatformTransferTypeEnum.TO_COLD_ADDRESS_TYPE.getType());
                    platformTransfer.setTxId(txHashId);
                    platformTransfer.setStatus(1);

                    platformTransferService.insert(platformTransfer);
                    log.error("eth主地址:{},转入冷钱包:{},------成功------", coin.getMainAddress(), coin.getColdAddress());

                }
            } catch (Throwable throwable) {
                log.error("eth主地址:{},转入冷钱包:{},------失败------{}", coin.getMainAddress(), coin.getColdAddress(), throwable);
            }

        }


        List<Coin> coinTokenList = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());
        coinTokenList = coinTokenList.stream()
                .filter(coin -> StringUtils.isNoneBlank(coin.getContractAddress()))
                .collect(Collectors.toList());


        for (Coin coin : coinTokenList) {

            //合约地址
            String contractAddress = coin.getContractAddress();
            String mainAddress = coin.getMainAddress();
            try {

                String tokenBalance = ethRPCApiUtils.getTokenBalance(mainAddress, contractAddress);
                //token余额
                BigDecimal balance = EthRPCApiUtils.stringToDBNum(tokenBalance, coin.getDecimals());

                if (balance.compareTo(coin.getOutQtyToColdAddress()) > 0) {

                    //方法id
                    String methodId = coin.getMethodId();

                    String to = coin.getColdAddress();
                    //超出剩下部分
                    BigDecimal qty = balance.subtract(coin.getOutQtyToColdAddress());
                    String ethHexQty = EthRPCApiUtils.dbNum2EthNum(qty,coin.getDecimals());
                    String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(coin.getMainAddressPassword())) ;

                    Map<String,Object> params = ethRPCApiUtils.transferToken(
                            contractAddress,
                            methodId,
                            mainAddress,
                            to,
                            ethHexQty,
                            password);

                    PlatformTransfer platformTransfer = new PlatformTransfer();


                    String toEHTgasPrice = (String)params.get("gasPrice");
                    String toEHTgas = (String)params.get("gas");
                    String txHashId = (String) params.get("txHashId");
                    platformTransfer.setGasLimit(EthRPCApiUtils.toNum(toEHTgas).intValue());
                    platformTransfer.setGasPrice(EthRPCApiUtils.ethGasPrice2DbGasPrice(toEHTgasPrice));


                    platformTransfer.setCoinId(coin.getId());
                    platformTransfer.setFromAddress(mainAddress);
                    platformTransfer.setToAddress(to);
                    platformTransfer.setQty(qty);
                    platformTransfer.setType(PlatformTransferTypeEnum.TO_COLD_ADDRESS_TYPE.getType());
                    platformTransfer.setTxId(txHashId);
                    platformTransfer.setStatus(1);
                    platformTransferService.insert(platformTransfer);
                    log.error("eth代币--{}--主地址:{},转入冷钱包:{},------成功------", coin.getShortName(), coin.getMainAddress(), coin.getColdAddress());

                }


            } catch (Throwable throwable) {
                throwable.printStackTrace();
                log.error("eth代币--{}--主地址:{},转入冷钱包:{},------失败------{}", coin.getShortName(), coin.getMainAddress(), coin.getColdAddress(), throwable);

            }


        }
        log.error("----------执行transferColdAddress结束------------");

    }

}
