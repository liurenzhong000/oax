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
 * Time: 20:15
 * 申请手续费
 */
@Slf4j
@Component
public class FeeToUserScheduled {


    @Autowired
    private CoinService coinService;

    @Autowired
    private PlatformTransferService platformTransferService;

    @Autowired
    private RechargeAddressService rechargeAddressService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;

    /**
     * 每三十分钟执行
     */
    @Async
    @Scheduled(cron="0 0/30 * * * ?")
    public void feeToUser() {

        log.error("-------------执行feeToUser-------------");

        List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectAllETHAdress();

        rechargeAddressList = rechargeAddressList.stream()
                .filter(rechargeAddress -> StringUtils.isNoneBlank(rechargeAddress.getAddress()))
                .collect(Collectors.toList());

        List<Coin> coinList = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());
        coinList = coinList.stream()
                .filter(coin -> StringUtils.isNoneBlank(coin.getContractAddress()))
                .collect(Collectors.toList());

        for (RechargeAddress rechargeAddress : rechargeAddressList) {
//
//            List<PlatformTransfer> notVerifyList = platformTransferService.selectNotVerifyByAddressAndType(userCoin.getAddress());
//            if (CollectionUtils.isNotEmpty(notVerifyList)) {
//                continue;
//            }


            String address = rechargeAddress.getAddress();
            BigDecimal ethBalance = null;
            try {
                String ethBalanceString = ethRPCApiUtils.getETHBalance(address);
                ethBalance = new BigDecimal(EthRPCApiUtils.toNum(ethBalanceString));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                continue;
            }


            BigDecimal usedFee = new BigDecimal("0");

            for (Coin coin : coinList) {

                try {
                    String tokenBalanceString = ethRPCApiUtils.getTokenBalance(address, coin.getContractAddress());
                    BigDecimal tokenBalance = EthRPCApiUtils.stringToDBNum(tokenBalanceString, coin.getDecimals());

                    BigDecimal outQtyToMainAddress = coin.getOutQtyToMainAddress();
                    if (tokenBalance.compareTo(outQtyToMainAddress) > 0) {

                        //实时获取 gasLimit gasPrice
                        //from为平台冷钱包
                        //to为用户地址
                        Integer gasLimit = ethRPCApiUtils.getGasLimit(
                                coin.getContractAddress(),
                                coin.getMethodId(),
                                coin.getMainAddress(),
                                rechargeAddress.getAddress());
                        Integer gasPrice = ethRPCApiUtils.ethGasPrice();

                        BigDecimal gasPriceEth = EthRPCApiUtils.dbGasPrice2EthNum(gasPrice*2);
                        usedFee = usedFee.add(gasPriceEth.multiply(new BigDecimal(gasLimit*2)));
                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            BigDecimal subtract = usedFee.subtract(ethBalance);
            if (subtract.compareTo(new BigDecimal("0")) > 0) {
                //说明 手续费不够用
                //需要申请转入

                List<Coin> ethCoinlist = coinService.selectByType(CoinTypeEnum.ETH.getType());
                Coin ethCoin = ethCoinlist.get(0);

                String from = ethCoin.getMainAddress();
                String to = rechargeAddress.getAddress();

                BigDecimal qty = subtract;

                String password = Base64Utils.getFromBase64(Base64Utils.getFromBase64(ethCoin.getMainAddressPassword())) ;

                String ethHexQty = EthRPCApiUtils.toHexString(qty.toBigInteger());

                try {
                    Map<String,Object> params = ethRPCApiUtils.transferETHForFee(
                            from,
                            to,
                            ethHexQty,
                            password
                    );

                    PlatformTransfer platformTransfer = new PlatformTransfer();

                    String toEHTgasPrice = (String)params.get("gasPrice");
                    String toEHTgas = (String)params.get("gas");
                    String txHashId = (String) params.get("txHashId");
                    platformTransfer.setGasLimit(EthRPCApiUtils.toNum(toEHTgas).intValue());
                    platformTransfer.setGasPrice(EthRPCApiUtils.ethGasPrice2DbGasPrice(toEHTgasPrice));

                    platformTransfer.setCoinId(ethCoin.getId());
                    platformTransfer.setFromAddress(from);
                    platformTransfer.setToAddress(to);
                    platformTransfer.setQty(EthRPCApiUtils.stringToDBNum(ethHexQty));
                    platformTransfer.setType(PlatformTransferTypeEnum.FEE_TYPE.getType());
                    platformTransfer.setTxId(txHashId);
                    platformTransfer.setStatus(1);
                    platformTransferService.insert(platformTransfer);

                    log.info("主地址转账到账到user_address::{}成功", to);
                } catch (Exception e) {

                    log.error("主地址转账到user_address::{}不成功,错误信息:{}", to, e);
                }

            }


        }

        log.error("--------------执行feeToUser结束-------------");
    }

}
