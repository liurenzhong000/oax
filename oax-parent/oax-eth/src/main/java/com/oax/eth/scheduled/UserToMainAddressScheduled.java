package com.oax.eth.scheduled;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
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
import com.oax.entity.front.RechargeAddress;
import com.oax.eth.service.CoinService;
import com.oax.eth.service.PlatformTransferService;
import com.oax.eth.service.RechargeAddressService;
import com.oax.eth.utils.EthRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/21
 * Time: 17:59
 * 提币到主地址
 */
@Slf4j
@Component
public class UserToMainAddressScheduled {

    @Autowired
    private CoinService coinService;

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private RechargeAddressService rechargeAddressService;
    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;

    /**
     * 每整点
     */
    @Async
    @Scheduled(cron="0 0 0/1 * * ?")
    public void userToMainAddress() {


        log.error("----------执行userToMainAddress------------");


        List<Coin> ethCoinList = coinService.selectByType(CoinTypeEnum.ETH.getType());
        Coin ethCoin = ethCoinList.get(0);

        List<Coin> tokenCoinList = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());
        tokenCoinList = tokenCoinList.stream()
                .filter(coin -> StringUtils.isNoneBlank(coin.getContractAddress()))
                .collect(Collectors.toList());


        List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectAllETHAdress();


        rechargeAddressList = rechargeAddressList.stream()
                .filter(rechargeAddress -> StringUtils.isNoneBlank(rechargeAddress.getAddress()))
                .collect(Collectors.toList());

        //检查token余额是否超出
        for (Coin coin : tokenCoinList) {

            BigDecimal outQtyToMainAddress = coin.getOutQtyToMainAddress();

            for (RechargeAddress rechargeAddress : rechargeAddressList) {

                try {
                    String tokenBalance = ethRPCApiUtils.getTokenBalance(rechargeAddress.getAddress(), coin.getContractAddress());


                    BigDecimal banlance = EthRPCApiUtils.stringToDBNum(tokenBalance, coin.getDecimals());

                    if (banlance.compareTo(outQtyToMainAddress) > 0) {

                        List<PlatformTransfer> notVerifyList = platformTransferService.selectNotVerifyByAddressAndType(rechargeAddress.getAddress());
                        if (CollectionUtils.isNotEmpty(notVerifyList)) {
                            continue;
                        }

                        String from = rechargeAddress.getAddress();
                        String to = coin.getMainAddress();

                        BigDecimal qty = banlance;
                        String methodId = coin.getMethodId();
                        String contractAddress = coin.getContractAddress();
                        String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(rechargeAddress.getPassword())) ;

                        String ethHexQty = EthRPCApiUtils.dbNum2EthNum(qty,coin.getDecimals());


                        Map<String,Object> params = ethRPCApiUtils.transferToken(
                                contractAddress,
                                methodId,
                                from,
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
                        platformTransfer.setFromAddress(from);
                        platformTransfer.setToAddress(to);
                        platformTransfer.setQty(qty);
                        platformTransfer.setType(PlatformTransferTypeEnum.TO_MAIN_ADDRESS_TYPE.getType());
                        platformTransfer.setTxId(txHashId);
                        platformTransferService.insert(platformTransfer);
                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }



        //检查eth是否超出
        for (RechargeAddress rechargeAddress : rechargeAddressList) {
            try {
                String ethBalance = ethRPCApiUtils.getETHBalance(rechargeAddress.getAddress());

                BigDecimal banlance = EthRPCApiUtils.stringToDBNum(ethBalance);
                BigDecimal outQtyToMainAddress = ethCoin.getOutQtyToMainAddress();
                if (banlance.compareTo(outQtyToMainAddress) > 0) {

                    List<PlatformTransfer> notVerifyList = platformTransferService.selectNotVerifyByAddressAndType(rechargeAddress.getAddress());
                    if (CollectionUtils.isNotEmpty(notVerifyList)) {
                        continue;
                    }


                    String to = ethCoin.getMainAddress();
                    String from = rechargeAddress.getAddress();
                    BigDecimal qty = banlance.subtract(outQtyToMainAddress);

                    String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(rechargeAddress.getPassword())) ;

                    String ethHexQty = EthRPCApiUtils.dbNum2EthNum(qty,18);


                    Map<String, Object> params = ethRPCApiUtils.transferETH(
                            from,
                            to,
                            ethHexQty,
                            password);




                    PlatformTransfer platformTransfer = new PlatformTransfer();

                    String toEHTgasPrice = (String)params.get("gasPrice");
                    String toEHTgas = (String)params.get("gas");
                    String txHashId = (String) params.get("txHashId");
                    platformTransfer.setGasLimit(EthRPCApiUtils.toNum(toEHTgas).intValue());
                    platformTransfer.setGasPrice(EthRPCApiUtils.ethGasPrice2DbGasPrice(toEHTgasPrice));

                    platformTransfer.setCoinId(ethCoin.getId());
                    platformTransfer.setFromAddress(from);
                    platformTransfer.setToAddress(to);
                    platformTransfer.setQty(qty);
                    platformTransfer.setType(PlatformTransferTypeEnum.TO_MAIN_ADDRESS_TYPE.getType());
                    platformTransfer.setTxId(txHashId);

                    platformTransferService.insert(platformTransfer);
                }

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        log.error("----------执行userToMainAddress结束-----------");
    }

}



