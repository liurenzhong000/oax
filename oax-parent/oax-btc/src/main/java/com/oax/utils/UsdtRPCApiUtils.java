package com.oax.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.exception.MyException;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/27
 * Time: 9:54
 */
@Component
public class UsdtRPCApiUtils {


    @Value("${usdt.server.url}")
    private String URL;

    @Value("${usdt.server.basicauth}")
    private String basicAuth;

    @Value("${btc.address.balance_url}")
    private String BTC_ADDRESS_BALANCE_URL;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 创建新地址
     *
     * @return
     */
    public String newAccount() throws Exception {

        String res = UsdtRPCRquest.invoke(URL, "getnewaddress", basicAuth, new Object[]{});

        return res;
    }

    /**
     * 获取区块高度
     *
     * @return
     */
    public BigInteger getblockcount() throws Exception {

        String getblockcount = UsdtRPCRquest.invoke(URL, "getblockcount", basicAuth, new Object[]{});

        return new BigInteger(getblockcount);
    }

    /**
     * 获取余额
     *
     * @param account
     * @return
     */
    public BigDecimal getbalance(String account, Integer propertyid) throws Exception {

        Object[] params = {};
        if (StringUtils.isNoneEmpty(account)) {
            params = new Object[]{account, propertyid};
        }
        String balance = UsdtRPCRquest.invoke(URL, "omni_getbalance", basicAuth, params);
        JSONObject jsonObject = JSON.parseObject(balance);
        return jsonObject.getBigDecimal("balance");
    }


    public BigDecimal getBtcBalanceByAddress(String address) throws Exception {
        JSONObject forObject = null;
        try {
            forObject = restTemplate.getForObject(BTC_ADDRESS_BALANCE_URL + address, JSONObject.class);
            String status = forObject.getString("status");
            if (StringUtils.equalsIgnoreCase(status, "success")) {

                String confirmed_balance = forObject.getJSONObject("data").getString("confirmed_balance");

                return new BigDecimal(confirmed_balance);
            } else {
                throw new MyException("address获取BTC余额失败");
            }
        } catch (RestClientException e) {
            throw new MyException("address获取BTC余额失败" + e.getMessage());
        }


    }


    /**
     * 转账
     *
     * @param from
     * @param to
     * @param amount
     * @return {
     * "result": {
     * "amount": 0,
     * "fee": -0.00001783,
     * "confirmations": 3754,
     * "blockhash": "0000000000016e66d08d7e67312b0ab8c86959498a01914013ec00dbb65ba71d",
     * "blockindex": 1,
     * "blocktime": 1530157439,
     * "txid": "4129df3a6fc3e466f137c30efe72ee27f21f8fb335bcf995b3f144048d38d191",
     * "walletconflicts": [],
     * "time": 1530157433,
     * "timereceived": 1530157433,
     * "bip125-replaceable": "no",
     * "details": [
     * {
     * "account": "",
     * "address": "2NCXGNDYuVmLcoXjJXo5kFcmVUSbs8JLnFi",
     * "category": "send",
     * "amount": -1.45,
     * "label": "",
     * "vout": 0,
     * "fee": -0.00001783,
     * "abandoned": false
     * },
     * {
     * "account": "",
     * "address": "2NCXGNDYuVmLcoXjJXo5kFcmVUSbs8JLnFi",
     * "category": "receive",
     * "amount": 1.45,
     * "label": "",
     * "vout": 0
     * }
     * ],
     * "hex": "020000000001031b3367626ca6aa53a4ab77110781a20223e9e07b083c0daeaeb6b18a559257d5010000001716001459149beb77f8cc5b5ddb8f1802cb0c999fc75ebdfeffffff202ff01c750181448ec1c199328bbe17473d7508f34773e9f90c7853ba631a1d000000001716001429c0a478c113aa125c2700a6b8223748d468d20bfeffffff202ff01c750181448ec1c199328bbe17473d7508f34773e9f90c7853ba631a1d0100000017160014e0a939a03ca26bd3356c37b55885ecd1540d6783feffffff024086a4080000000017a914d37307ccbe7b9fd2cfb4df1a1c6cf51c597cf5a587b62231010000000017a914b6f35ff7832da432e47c00dde936cd773c3fe9238702473044022038b8edfcdfee3196d3d73de249cd4c413df5b5e3c659a666c917f00e600fcb790220270fe638787645458a01ed262d04fc1b4e3366b4e6a0ae627a1bf0d592480e770121024312a9c1b25cfe8aadd3a05269a884b8d2818788aee1f11b62dd81b7fcaf789202473044022050e5645c67f8852b8640c23bb910700035a0795eb4852fcb3d52bb7f26f61fe502206b86839d89c01a1ba3f6a236ca9242ad276f568e7117e89699734545ccbc8914012102c1a7e603192758142d332c04d0dca4cf92a76de1e1e76ecca52bdc5c9b764dab02483045022100c071d5aff6a6827d96d6e9beee09daf21faf5b3b26c1e7bf7f65e9e97f9c80660220220e6db832e1e64f2b20de6e6925b288a061eb2b9544961a826223ca5fd4186a0121031612f24edcc1fa23098e78493fb56e06a50ad44c2cb5977d8efb43f70e09231681571400"
     * },
     * "error": null,
     * "id": "1"
     * }
     */
    public String sendtoaddress(String from, String to, BigDecimal amount, Integer propertyid) throws Exception {
        String txid = UsdtRPCRquest.invoke(URL, "omni_send", basicAuth, new Object[]{from, to, propertyid, amount.toString()});
        return txid;
    }


    public String btcSendtoaddress(String address, BigDecimal amount) throws Exception {
        String txid = UsdtRPCRquest.invoke(URL, "sendtoaddress", basicAuth, new Object[]{address, amount});
        return txid;
    }

    /**
     * 获取交易池
     *
     * @return
     */
    public JSONArray listtransactions() throws Exception {
        String listtransactions = null;
        listtransactions = UsdtRPCRquest.invoke(URL, "listtransactions", basicAuth, new Object[]{});
        return JSON.parseArray(listtransactions);
    }

    /**
     * {
     * "result": [
     * "14dbeffbb044f0013ab8e975171eda1b6ba807eb9b8c06c0932180a192c4732c"
     * ],
     * "error": null,
     * "id": "1"
     * }
     * <p>
     * 指定区块高度 所有交易hash
     *
     * @return
     */
    public JSONArray omniListblocktransactions(Integer blockIndex) throws Exception {

        String listtransactions = UsdtRPCRquest.invoke(URL, "omni_listblocktransactions", basicAuth, new Object[]{blockIndex});
        return JSON.parseArray(listtransactions);
    }


    /**
     * {
     * "result": {
     * "txid": "43dfc661eed95800766ffd6a4871641827ed458fed11647806cdc5219536830f",
     * "fee": "0.00102400",
     * "sendingaddress": "1Cc6TokpgD1jHG1HATXij76ne5yvLWgQop", //发送者
     * "referenceaddress": "1JECVy2oTzoRgHAKhTgyaCPPvM92UnJRa1", //接受者
     * "ismine": false,
     * "version": 0,
     * "type_int": 0,
     * "type": "Simple Send",
     * "propertyid": 31,
     * "divisible": true,
     * "amount": "963.00000000",
     * "valid": true, //是否成功
     * "blockhash": "0000000000000000001e9a02fcf3cc5fd842c797f43b77505ba7f282e00d62b3",
     * "blocktime": 1522655728,
     * "positioninblock": 31,
     * "block": 516247,
     * "confirmations": 8560
     * },
     * "error": null,
     * "id": "1"
     * }
     *
     * @param txId
     * @return
     * @throws Exception
     */
    public JSONObject getTransaction(String txId) throws Exception {
        String gettransaction = UsdtRPCRquest.invoke(URL, "omni_gettransaction", basicAuth, new Object[]{txId});
        return JSON.parseObject(gettransaction);
    }

    public JSONObject getBtcTransaction(String txId) throws Exception {
        String gettransaction = UsdtRPCRquest.invoke(URL, "gettransaction", basicAuth, new Object[]{txId});
        return JSON.parseObject(gettransaction);
    }


    /**
     * 通过 原始交易转账
     *
     * @param from
     * @param to
     * @param balance
     * @return
     */
    public String sendByRawTransaction(String from, String to, BigDecimal balance, Integer propertyid, String mainAddress, BigDecimal outQtyToMainAddress) throws Exception {

        //找零地址()
        String changeAddress;
        if (StringUtils.equals(from, mainAddress)) {
            changeAddress = mainAddress;
        } else {
            changeAddress = newAccount();
        }
//      1) List unspent outputs -> listunspent
        List<JSONObject> listunspent = listunspent();

        List<JSONObject> createrawtransactionArgs = listunspent.stream()
                .filter(e -> e.getString("address").equalsIgnoreCase(from))
                .map(e -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("txid", e.getString("txid"));
                    jsonObject.put("vout", e.getInteger("vout"));
                    return jsonObject;
                }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(createrawtransactionArgs)) {
            //说明并没有转入记录
            throw new MyException("地址:" + from + ",usdt没有转入时携带BTC(0.00000546)");
        }


        List<JSONObject> omniCreaterawtxChangeArgs = listunspent.stream()
                .filter(e -> e.getString("address").equalsIgnoreCase(from))
                .map(e -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("txid", e.getString("txid"));
                    jsonObject.put("vout", e.getInteger("vout"));
                    jsonObject.put("scriptPubKey", e.getString("scriptPubKey"));
                    jsonObject.put("value", e.getBigDecimal("amount"));
                    return jsonObject;
                }).collect(Collectors.toList());

        BigDecimal amount = listunspent.stream()
                .filter(e -> e.getString("address").equalsIgnoreCase(from))
                .map(e -> e.getBigDecimal("amount"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (amount.compareTo(new BigDecimal("0.0001").add(new BigDecimal("0.00000546"))) < 0) {

            Optional<JSONObject> first = listunspent.stream()
                    .filter(e -> !e.getString("address").equalsIgnoreCase(from) &&
                            !StringUtils.equalsIgnoreCase(e.getString("address"), mainAddress))
                    .filter(e -> {
                        String address = e.getString("address");

                        try {
                            BigDecimal getbalance = getbalance(address, propertyid);
                            return getbalance.compareTo(outQtyToMainAddress) < 0;
                        } catch (Exception e1) {
                            throw new MyException("usdt接口请求错误");
                        }

                    })
                    .filter(e -> e.getBigDecimal("amount").compareTo(new BigDecimal("0.0001")) > 0)
                    .findFirst();

            JSONObject jsonObject = null;
            if (first.isPresent()) {
//                throw new MyException("没有足够的BTC余额");
                jsonObject = first.get();
            }else {
                Optional<JSONObject> fromUnspentOptional = listunspent.stream()
                        .filter(e -> e.getString("address").equalsIgnoreCase(mainAddress) &&
                                e.getBigDecimal("amount").compareTo(new BigDecimal("0.0001").add( new BigDecimal("0.00000546"))) >= 0)
                        .min(Comparator.comparing(unspent -> unspent.getBigDecimal("amount")));

                if (fromUnspentOptional.isPresent()){
                    jsonObject = fromUnspentOptional.get();
                }else {
                    throw new MyException("没有足够的BTC余额");
                }


            }
            JSONObject createrawFeeMap = new JSONObject();
            createrawFeeMap.put("txid", jsonObject.getString("txid"));
            createrawFeeMap.put("vout", jsonObject.getInteger("vout"));
            createrawtransactionArgs.add(createrawFeeMap);

            JSONObject omniCreaterawtxFeeMap = new JSONObject();
            omniCreaterawtxFeeMap.put("txid", jsonObject.getString("txid"));
            omniCreaterawtxFeeMap.put("vout", jsonObject.getInteger("vout"));
            omniCreaterawtxFeeMap.put("scriptPubKey", jsonObject.getString("scriptPubKey"));
            omniCreaterawtxFeeMap.put("value", jsonObject.getBigDecimal("amount"));
            omniCreaterawtxChangeArgs.add(omniCreaterawtxFeeMap);
        }

        BigDecimal fee = new BigDecimal("0.0001");
//        BigDecimal fee = calculationFee(1);


//      2) Construct payload
        String createpayloadSimplesend = UsdtRPCRquest.invoke(URL, "omni_createpayload_simplesend", basicAuth, new Object[]{propertyid, balance.toString()});
//      3) Construct transaction base
        String createrawtransaction = UsdtRPCRquest.invoke(URL, "createrawtransaction", basicAuth, new Object[]{createrawtransactionArgs, new HashMap<>()});
//      4) Attach payload output
        String omniCreaterawtxOpreturn = UsdtRPCRquest.invoke(URL, "omni_createrawtx_opreturn", basicAuth, new Object[]{createrawtransaction, createpayloadSimplesend});
//      5) Attach reference/receiver output 此时 to为归总地址
        String omniCreaterawtxReference = UsdtRPCRquest.invoke(URL, "omni_createrawtx_reference", basicAuth, new Object[]{omniCreaterawtxOpreturn, to});
//      6) Specify miner fee and attach change output (as needed) 此时 to为找零地址
        String omniCreaterawtxChange = UsdtRPCRquest.invoke(URL, "omni_createrawtx_change", basicAuth, new Object[]{
                omniCreaterawtxReference,
                omniCreaterawtxChangeArgs,
                changeAddress,
                fee});
//      7) Sign transaction
        String signrawtransaction = UsdtRPCRquest.invoke(URL, "signrawtransaction", basicAuth, new Object[]{omniCreaterawtxChange});
        JSONObject jsonObject = JSON.parseObject(signrawtransaction);
//      8) Broadcast transaction
        String txId = UsdtRPCRquest.invoke(URL, "sendrawtransaction", basicAuth, new Object[]{jsonObject.getString("hex")});

        return txId;
    }


    public List<JSONObject> listunspent() throws Exception {
        String listunspent = UsdtRPCRquest.invoke(URL, "listunspent", basicAuth, new Object[]{});
        List<JSONObject> jsonObjectList = JSON.parseArray(listunspent, JSONObject.class);
        return jsonObjectList;
    }

    public String mainSendUsdt(String from, String to, BigDecimal balance, Integer propertyid, String mainAddress, BigDecimal outQtyToMainAddress) throws Exception {
        //找零地址()
        String changeAddress;
        if (StringUtils.equals(from, mainAddress)) {
            changeAddress = mainAddress;
        } else {
            changeAddress = newAccount();
        }
//      1) List unspent outputs -> listunspent
        List<JSONObject> listunspent = listunspent();

        List<JSONObject> createrawtransactionArgs = new ArrayList<>();
        Optional<JSONObject> fromUnspentOptional = listunspent.stream()
                .filter(e -> e.getString("address").equalsIgnoreCase(from) &&
                        e.getBigDecimal("amount").compareTo(new BigDecimal("0.0001").add(new BigDecimal("0.00000546"))) >= 0)
                .min(Comparator.comparing(jsonObject -> jsonObject.getBigDecimal("amount")));

        if (!fromUnspentOptional.isPresent()) {

            fromUnspentOptional = listunspent.stream()
                    .filter(e -> e.getString("address").equalsIgnoreCase(from) &&
                            e.getBigDecimal("amount").compareTo(new BigDecimal("0.00000546")) >= 0)
                    .min(Comparator.comparing(jsonObject -> jsonObject.getBigDecimal("amount")));
            if (!fromUnspentOptional.isPresent()) {
                //说明并没有转入记录
                throw new MyException("地址:" + from + ",usdt没有转入时携带BTC(0.00000546)");
            }
        }

        JSONObject fromUnspent = fromUnspentOptional.get();
        JSONObject createraw = new JSONObject();
        createraw.put("txid", fromUnspent.getString("txid"));
        createraw.put("vout", fromUnspent.getInteger("vout"));
        createrawtransactionArgs.add(createraw);

        List<JSONObject> omniCreaterawtxChangeArgs = new ArrayList<>();
        JSONObject omniCreateraw = new JSONObject();
        omniCreateraw.put("txid", fromUnspent.getString("txid"));
        omniCreateraw.put("vout", fromUnspent.getInteger("vout"));
        omniCreateraw.put("scriptPubKey", fromUnspent.getString("scriptPubKey"));
        omniCreateraw.put("value", fromUnspent.getBigDecimal("amount"));
        omniCreaterawtxChangeArgs.add(omniCreateraw);

        BigDecimal amount = fromUnspent.getBigDecimal("amount");

        if (amount.compareTo(new BigDecimal("0.0001").add(new BigDecimal("0.00000546"))) < 0) {

            Optional<JSONObject> first = listunspent.stream()
                    .filter(e -> !e.getString("address").equalsIgnoreCase(from) &&
                            !StringUtils.equalsIgnoreCase(e.getString("address"), mainAddress))
                    .filter(e -> {
                        String address = e.getString("address");

                        try {
                            BigDecimal getbalance = getbalance(address, propertyid);
                            return getbalance.compareTo(outQtyToMainAddress) < 0;
                        } catch (Exception e1) {
                            throw new MyException("usdt接口请求错误");
                        }

                    })
                    .filter(e -> e.getBigDecimal("amount").compareTo(new BigDecimal("0.0001").add(new BigDecimal("0.00000546"))) >= 0)
                    .min(Comparator.comparing(e -> e.getBigDecimal("amount")));


            if (!first.isPresent()) {
                throw new MyException("没有足够的BTC余额");
            }
            JSONObject jsonObject = first.get();
            JSONObject createrawFeeMap = new JSONObject();
            createrawFeeMap.put("txid", jsonObject.getString("txid"));
            createrawFeeMap.put("vout", jsonObject.getInteger("vout"));
            createrawtransactionArgs.add(createrawFeeMap);

            JSONObject omniCreaterawtxFeeMap = new JSONObject();
            omniCreaterawtxFeeMap.put("txid", jsonObject.getString("txid"));
            omniCreaterawtxFeeMap.put("vout", jsonObject.getInteger("vout"));
            omniCreaterawtxFeeMap.put("scriptPubKey", jsonObject.getString("scriptPubKey"));
            omniCreaterawtxFeeMap.put("value", jsonObject.getBigDecimal("amount"));
            omniCreaterawtxChangeArgs.add(omniCreaterawtxFeeMap);
        }

        BigDecimal fee = new BigDecimal("0.0001");
//        BigDecimal fee = calculationFee(1);


//      2) Construct payload
        String createpayloadSimplesend = UsdtRPCRquest.invoke(URL, "omni_createpayload_simplesend", basicAuth, new Object[]{propertyid, balance.toString()});
//      3) Construct transaction base
        String createrawtransaction = UsdtRPCRquest.invoke(URL, "createrawtransaction", basicAuth, new Object[]{createrawtransactionArgs, new HashMap<>()});
//      4) Attach payload output
        String omniCreaterawtxOpreturn = UsdtRPCRquest.invoke(URL, "omni_createrawtx_opreturn", basicAuth, new Object[]{createrawtransaction, createpayloadSimplesend});
//      5) Attach reference/receiver output 此时 to为归总地址
        String omniCreaterawtxReference = UsdtRPCRquest.invoke(URL, "omni_createrawtx_reference", basicAuth, new Object[]{omniCreaterawtxOpreturn, to});
//      6) Specify miner fee and attach change output (as needed) 此时 to为找零地址
        String omniCreaterawtxChange = UsdtRPCRquest.invoke(URL, "omni_createrawtx_change", basicAuth, new Object[]{
                omniCreaterawtxReference,
                omniCreaterawtxChangeArgs,
                changeAddress,
                fee});
//      7) Sign transaction
        String signrawtransaction = UsdtRPCRquest.invoke(URL, "signrawtransaction", basicAuth, new Object[]{omniCreaterawtxChange});
        JSONObject jsonObject = JSON.parseObject(signrawtransaction);
//      8) Broadcast transaction
        String txId = UsdtRPCRquest.invoke(URL, "sendrawtransaction", basicAuth, new Object[]{jsonObject.getString("hex")});


        return txId;
    }


    /**
     * 计算手续费
     *
     * @param inputCount
     * @return
     */
    public BigDecimal calculationFee(int inputCount) {
        //计算手续费获取每个字节的手续费
        String url = "https://bitcoinfees.earn.com/api/v1/fees/recommended";
        //计算字节大小和费用
        JSONObject forObject = restTemplate.getForObject(url, JSONObject.class);
        //148 * 输入数额 + 34 * 输出数额 + 10
        BigDecimal keyCount = BigDecimal.valueOf((inputCount * 148 + 44) * forObject.getDoubleValue("halfHourFee"));
        BigDecimal transferFee = keyCount.divide(new BigDecimal("100000000"), 8, RoundingMode.HALF_UP);
        return transferFee;
    }


}
