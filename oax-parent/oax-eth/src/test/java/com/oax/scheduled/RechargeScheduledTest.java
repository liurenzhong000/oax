package com.oax.scheduled;

import static com.oax.eth.utils.EthRPCApiUtils.dbGasPrice2EthGasPrice;
import static com.oax.eth.utils.EthRPCApiUtils.dbNum2EthNum;
import static com.oax.eth.utils.EthRPCApiUtils.toHexString;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.oax.OaxApiApplicationTest;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.RechargeAddress;
import com.oax.entity.front.UserCoin;
import com.oax.eth.scheduled.FeeToUserScheduled;
import com.oax.eth.scheduled.GasPriceAndGasLimitScheduled;
import com.oax.eth.scheduled.RechargeScheduled;
import com.oax.eth.scheduled.ScanOutTxPoolPlatformTransferScheduled;
import com.oax.eth.scheduled.ScanOutTxPoolWithdrawTransferScheduled;
import com.oax.eth.scheduled.ScanWaningQtyScheduled;
import com.oax.eth.scheduled.TransferColdAddressScheduled;
import com.oax.eth.scheduled.UserToMainAddressScheduled;
import com.oax.eth.service.CoinService;
import com.oax.eth.service.RechargeAddressService;
import com.oax.eth.service.UserCoinService;
import com.oax.eth.utils.EthRPCApiUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 23:23
 */
@Slf4j
@Component
public class RechargeScheduledTest extends OaxApiApplicationTest {

    @Autowired
    private RechargeScheduled rechargeScheduled;

    @Autowired
    private CoinService coinService;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;

    @Test
    public void tokenCoinTest() {
        List<Coin> coinList = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());
        log.info("coinList",coinList.toString());
    }

    @Test
    public void rechargeScheduledTest() throws Exception {
        rechargeScheduled.scanRecharge();
    }

    @Autowired
    private UserCoinService userCoinService;
    @Test
    public void inserUserCoin() {

        UserCoin userCoin = new UserCoin();

        userCoin.setUserId(17);
        userCoin.setCoinId(4);

        userCoinService.insert(userCoin);


        Optional<UserCoin> userCoinOptional = Optional.of(userCoin);

        UserCoin userCoin1 = userCoinOptional.get();


        log.info("user::{}",userCoin.getId());


    }

    @Test
    public void newAccountTest() throws Exception {
        String s = ethRPCApiUtils.newAccount("123456");
        log.info("帐户地址:{}", s);
    }

    @Test
    public void txSelect() throws Exception {
        Map<String, Map<String, Map<String, Map<String, String>>>> stringMapMap = ethRPCApiUtils.txpoolContent();

        log.info("stringMapMap:{}", JSON.toJSONString(stringMapMap));

        ArrayList<Map<String, String>> allblockingTxList = new ArrayList<>();
        Set<String> statusKeys = stringMapMap.keySet();
        for (String statusKey : statusKeys) {
            Map<String, Map<String, Map<String, String>>> mapMap = stringMapMap.get(statusKey);

            Set<String> allAddresses = mapMap.keySet();

            for (String allAddress : allAddresses) {

                Map<String, Map<String, String>> queueMap = mapMap.get(allAddress);

                Set<String> queueKeys = queueMap.keySet();

                for (String queueKey : queueKeys) {

                    Map<String, String> stringStringMap = queueMap.get(queueKey);
                    allblockingTxList.add(stringStringMap);
                }
            }
        }
//
//        List<Map<String, String>> nonce = allblockingTxList.stream()
//                .sorted(Comparator.comparing(e -> EthRPCApiUtils.toNum(e.get("nonce"))))
//                .collect(Collectors.toList());

        allblockingTxList.sort(Comparator.comparing(e -> EthRPCApiUtils.toNum(e.get("nonce"))));

        log.info("allblockingTxList::{}", allblockingTxList);
//        Set<String> statusKeys = stringMapMap.keySet();
//
//        for (String statusKey : statusKeys) {
//            Map<String, Map<String, Map<String, String>>> mapMap = stringMapMap.get(statusKey);
//
//
//            Set<String> allAddresses = mapMap.keySet();
//
//            for (String allAddress : allAddresses) {
//
//                Map<String, Map<String, String>> queueMap = mapMap.get(allAddress);
//
//                Set<String> queueKeys = queueMap.keySet();
//
//                for (String queueKey : queueKeys) {
//
//                    Map<String, String> stringStringMap = queueMap.get(queueKey);
//
//                    String from = stringStringMap.get("from");
//                    log.info(from);
//                }
//
//            }
//
//        }

    }


    @Autowired
    private UserToMainAddressScheduled userToMainAddressScheduled;

    @Test
    public void userToMainAddressTest() {

        userToMainAddressScheduled.userToMainAddress();
    }

    @Autowired
    private ScanOutTxPoolPlatformTransferScheduled scanOutTxPoolTransferScheduled;

    @Test
    public void scanOutTxPoolTransfer() {
        scanOutTxPoolTransferScheduled.scanOutTxPoolTransfer();
    }

    @Autowired
    private FeeToUserScheduled feeToUserScheduled;

    @Test
    public void feeToUserTest() {
        feeToUserScheduled.feeToUser();
    }

    @Autowired
    private TransferColdAddressScheduled transferColdAddressScheduled;

    @Test
    public void transferColdAddressTest() {
        transferColdAddressScheduled.transferColdAddress();
    }

    @Test
    public void testFee() throws Exception {

        String txId = "0xe05ad3460631a1020f2290e25d192f088b69405a75dee5f4e5372e57435290af";
        Map<String, Object> txReceipt = ethRPCApiUtils.getTransactionReceipt(txId);
        Map<String, Object> transactionByHash = ethRPCApiUtils.getTransactionByHash(txId);
//获取使用费用
        String gasUsedETH = (String) txReceipt.get("gasUsed");
        String gasPriceETH = (String) transactionByHash.get("gasPrice");

        BigInteger gasUsed = EthRPCApiUtils.toNum(gasUsedETH);
        BigDecimal gasPrice = EthRPCApiUtils.stringToDBNum(gasPriceETH);

        BigDecimal fee = gasPrice.multiply(new BigDecimal(gasUsed));
        log.info("fee::{}", fee);
    }
    private static final String PASSWORD = "wcnm1314";

    @Test
    public void testETHThranfer() throws Exception{


        String contractAddress = "0x6E453c010A606f5D336060f81DE7fe2b15c6cf4C";
        String methodId = "0xa9059cbb";



        //我的地址 (用户)
        String from = "0x9dbe1294fcd35bffcb264e51da9ca04dfdbd2272";

        from = "0x9143f7bd0E0D2ce0De0762FB7c2A0AfCfD4F959c";

        //平台地址
        String to = "0x9143f7bd0e0d2ce0de0762fb7c2a0afcfd4f959c";

        to = "0x3a90625d61decb6f87a752c26aa269e8053d3423";

        BigDecimal qty = new BigDecimal("0.07");
        String ethHexQty = dbNum2EthNum(qty,18);

        int gasPrice = 20;//单位Gwei
        int gasLimit = 72000;


        String gasPriceETHhex = dbGasPrice2EthGasPrice(gasPrice);
//        log.info("gasPriceETHhex::{}",gasPriceETHhex);
        String gasLimitETHhex = toHexString(new BigInteger(gasLimit+""));
//        log.info("gasLimitETHhex::{}",gasLimitETHhex);
//
//        String txBefore = getETHBalance(to);
//        log.info("转账之前 地址:{},余额为:{}",to,stringToDBNum(txBefore));


        Map<String, Object> params = ethRPCApiUtils.transferETH(from, to, ethHexQty, PASSWORD);
        String toEHTgasPrice = (String)params.get("gasPrice");
        String toEHTgas = (String)params.get("gas");
        String txHashId = (String) params.get("txHashId");

        log.info("params::{}",JSON.toJSONString(params));

        log.info("toEHTgas:{},toEHTgasPrice:{}",EthRPCApiUtils.toNum(toEHTgas).intValue(),EthRPCApiUtils.ethGasPrice2DbGasPrice(toEHTgasPrice));
    }

    @Autowired
    private GasPriceAndGasLimitScheduled gasPriceAndGasLimitScheduled;

    @Test
    public void updateGasPriceAndGasLimit() {
        gasPriceAndGasLimitScheduled.updateGasPriceAndGasLimit();
    }

    @Autowired
    private ScanOutTxPoolWithdrawTransferScheduled scanOutTxPoolWithdrawTransferScheduled;

    @Test
    public void scanOutTxPoolWithdrawTransfer() {
        scanOutTxPoolWithdrawTransferScheduled.ScanOutTxPoolTransfer();
    }

    @Autowired
    private ScanWaningQtyScheduled scanWaningQtyScheduled;

    @Test
    public void scanWaningQty() {
        scanWaningQtyScheduled.scanWaningQty();
    }



    @Test
    public void realiseTransferQty(){

        ArrayList<Map<String, String>> allTxpoolBlockings = ethRPCApiUtils.getAllTxpoolBlockings();

        List<Map<String, String>> blockingList = allTxpoolBlockings.stream()
                .filter(e -> e.get("from").equalsIgnoreCase("0xf1777c88c02b5089e8ac63cf17b6be01d0c8507f"))
                .collect(Collectors.toList());

        assert blockingList.size() > 0;


    }



    @Autowired
    private RechargeAddressService rechargeAddressService;

    @Test
    public void countAllTokenBalance() throws Throwable {

        Coin coin = coinService.selectById(22);

        List<RechargeAddress> rechargeAddressList = rechargeAddressService.selectAllETHAdress();

        BigDecimal count = BigDecimal.ZERO;

        for (RechargeAddress rechargeAddress : rechargeAddressList) {

            String tokenBalance = ethRPCApiUtils.getTokenBalance(rechargeAddress.getAddress(), coin.getContractAddress());

            BigDecimal bigDecimal = EthRPCApiUtils.stringToDBNum(tokenBalance, coin.getDecimals());

            if (bigDecimal.compareTo(new BigDecimal("500"))>0){
                log.error("地址:{},余额:{}",rechargeAddress.getAddress(),bigDecimal);
            }
            count = bigDecimal.add(count);
        }
        log.error("use所有用户余额:"+count);
    }


    @Test
    public void allUsersTokenBalance() throws Throwable {


        File file = new File("D:\\long_text_2018-09-19-15-51-02.txt");


        List<String> strings = IOUtils.readLines(new FileInputStream(file));

        List<Coin> tokenlist = coinService.selectByType(CoinTypeEnum.ETH_TOKEN.getType());

        List<String> allAddressList = new ArrayList<>();

        for (String string : strings) {

            String[] split = string.split("--");
            String s = split[2];
            allAddressList.add("0x"+s);
        }

        HashMap<String, BigDecimal> coinBalanceMap = new HashMap<>();

        for (Coin coin : tokenlist) {

            BigDecimal tokenBalance = BigDecimal.ZERO;
            for (String address : allAddressList) {
                String balance = ethRPCApiUtils.getTokenBalance(address, coin.getContractAddress());
                BigDecimal bigDecimal = EthRPCApiUtils.stringToDBNum(balance, coin.getDecimals());
                tokenBalance = tokenBalance.add(bigDecimal);
            }
            coinBalanceMap.put(coin.getShortName(),tokenBalance);
        }

        List<Coin> ethCoinList = coinService.selectByType(CoinTypeEnum.ETH.getType());

        for (Coin coin : ethCoinList) {

            BigDecimal balance = BigDecimal.ZERO;
            for (String address : allAddressList) {

                String ethBalance = ethRPCApiUtils.getETHBalance(address);
                BigDecimal bigDecimal = EthRPCApiUtils.stringToDBNum(ethBalance, coin.getDecimals());
                balance = balance.add(bigDecimal);
            }

            coinBalanceMap.put("ETH",balance);
        }
        log.error(JSON.toJSONString(coinBalanceMap));
    }
}