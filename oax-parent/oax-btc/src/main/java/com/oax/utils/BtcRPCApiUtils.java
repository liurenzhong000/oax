package com.oax.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.exception.MyException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/27
 * Time: 9:54
 * <p>
 * [] 可选参数  <>必要参数
 * 创建新帐户    getnewaddress   2NBjtY4DjUvAv67MimaviF5dEdfG15Cfg6L
 * 2NCXGNDYuVmLcoXjJXo5kFcmVUSbs8JLnFi
 * 2NCXGNDYuVmLcoXjJXo5kFcmVUSbs8JLnFi
 *
 * <p>
 * 转账         sendtoaddress        <bitcoinaddress> <amount> [comment] [comment-to]
 * <amount> is a real and is rounded to 8 decimal places. Returns the transaction ID <txid> if successful.
 * <p>
 * 获取交易池    listtransactions    	[account] [count=10] [from=0]
 * <p>
 * 获取区块高度   getblockcount       Returns the number of blocks in the longest block chain.
 * <p>
 * 获取地址余额   getbalance          [account] [minconf=1]
 * If [account] is not specified, returns the server's total available balance.
 * If [account] is specified, returns the balance in the account.
 * <p>
 * 交易hash
 * d55792558ab1b6aeae0d3c087be0e92302a281071177aba453aaa66c6267331b
 * 9d617bc24aa5cc86a83b330a9fd5c03d867c62868cc6f793dac995f2b6059531
 */
@Component
public class BtcRPCApiUtils {


    @Value("${btc.server.url}")
    private String URL;

    @Value("${btc.server.basicauth}")
    private String basicAuth;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 创建新地址
     *
     * @return
     */
    public String newAccount() throws Exception {

        String res = BTCRPCRquest.invoke(URL, "getnewaddress", basicAuth, new Object[]{});

        return res;
    }

    /**
     * 获取区块高度
     *
     * @return
     */
    public String getblockcount() throws Exception {

        String getblockcount = BTCRPCRquest.invoke(URL, "getblockcount", basicAuth, new Object[]{});

        return getblockcount;
    }

    /**
     * 获取余额
     *
     * @param account 帐户 为null时 查询服务器可用余额
     * @return
     */
    public String getbalance(String account) throws Exception {

        Object[] params = {};
        if (StringUtils.isNoneEmpty(account)) {
            params = new Object[]{account};
        }
        String balance = BTCRPCRquest.invoke(URL, "getbalance", basicAuth, params);
        return balance;
    }


    /**
     * 转账
     *
     * @param address
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
    public String sendtoaddress(String address, BigDecimal amount) throws Exception {
        String txid = BTCRPCRquest.invoke(URL, "sendtoaddress", basicAuth, new Object[]{address, amount});
        return txid;
    }


    @Autowired
    private UsdtRPCApiUtils usdtRPCApiUtils;


    public static void main(String[] args) {

        List<BigDecimal> integers = Arrays.asList(new BigDecimal("1"), new BigDecimal("-10"), new BigDecimal("16"), new BigDecimal("2"));
        integers.sort(Comparator.comparing(integer -> integer.multiply(new BigDecimal("-1"))));

        System.out.println(integers);

    }

    @Value("${usdt.propertyid}")
    private Integer propertyid;

    public String sendtoaddressByRaw(String to, BigDecimal amount, String mainAddress, BigDecimal outQtyToMainAddress) throws Exception {

        String changeAddress = mainAddress;


        List<JSONObject> listunspent = listunspent();

        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal fee = new BigDecimal("0.0001");
//        BigDecimal fee = calculationFee(1);
        amount = amount.add(fee);
        List<JSONObject> createrawtransactionArgs = new ArrayList<>();
        List<JSONObject> omniCreaterawtxChangeArgs = new ArrayList<>();

        listunspent.sort(Comparator.comparing(jsonObject -> jsonObject.getBigDecimal("amount")));
        for (JSONObject jsonObject : listunspent) {
            if (jsonObject.getBigDecimal("amount").compareTo(amount) >= 0) {

                if (!StringUtils.equals(jsonObject.getString("address"), mainAddress)) {
                    BigDecimal usdtBalance = usdtRPCApiUtils.getbalance(jsonObject.getString("address"), propertyid);
                    if (usdtBalance.compareTo(outQtyToMainAddress) > 0) {
                        continue;
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
                BigDecimal bigDecimal = jsonObject.getBigDecimal("amount");
                balance = balance.add(bigDecimal);
                break;
            }
        }


        if (CollectionUtils.isEmpty(createrawtransactionArgs)) {
            //倒序排序
            listunspent.sort(Comparator.comparing(jsonObject -> jsonObject.getBigDecimal("amount").multiply(new BigDecimal("-1"))));
            for (JSONObject jsonObject : listunspent) {
                BigDecimal bigDecimal = jsonObject.getBigDecimal("amount");

                if (bigDecimal.compareTo(fee) <= 0) {
                    break;
                }

                if (!StringUtils.equals(jsonObject.getString("address"), mainAddress)) {
                    BigDecimal usdtBalance = usdtRPCApiUtils.getbalance(jsonObject.getString("address"), propertyid);
                    if (usdtBalance.compareTo(outQtyToMainAddress) > 0) {
                        continue;
                    }
                }


                balance = balance.add(bigDecimal);

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

                if (balance.compareTo(amount) > 0) {
                    break;
                }
            }
            if (balance.compareTo(amount) < 0) {
                throw new MyException("未找到合适的手续费地址");
            }
        }


        if (CollectionUtils.isEmpty(createrawtransactionArgs) ||
                CollectionUtils.isEmpty(omniCreaterawtxChangeArgs)) {
            throw new MyException("未找到合适的手续费地址");
        }

        LinkedHashMap<String, BigDecimal> adrressJson = new LinkedHashMap<>();
        //到账地址
        adrressJson.put(to, amount.subtract(fee));
        //找零地址

        if (balance.subtract(amount).compareTo(fee)>=0){
            adrressJson.put(changeAddress, balance.subtract(amount));
        }

        try {
            String createrawtransaction = BTCRPCRquest.invoke(URL, "createrawtransaction", basicAuth, new Object[]{createrawtransactionArgs, adrressJson});
            String signrawtransaction = BTCRPCRquest.invoke(URL, "signrawtransaction", basicAuth, new Object[]{createrawtransaction, omniCreaterawtxChangeArgs});
            JSONObject jsonObject = JSON.parseObject(signrawtransaction);
            String txId = BTCRPCRquest.invoke(URL, "sendrawtransaction", basicAuth, new Object[]{jsonObject.getString("hex")});
            return txId;
        } catch (Exception e) {
            throw new MyException("构建原始交易错误,to参数:"+JSON.toJSONString(adrressJson)+",from参数:"+JSON.toJSONString(omniCreaterawtxChangeArgs));
        }
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

    public List<JSONObject> listunspent() throws Exception {
        String listunspent = UsdtRPCRquest.invoke(URL, "listunspent", basicAuth, new Object[]{});
        List<JSONObject> jsonObjectList = JSON.parseArray(listunspent, JSONObject.class);
        return jsonObjectList;
    }

    /**
     * 获取交易池
     *
     * @return
     */
    public JSONArray listtransactions() throws Exception {
        String listtransactions = null;
        listtransactions = BTCRPCRquest.invoke(URL, "listtransactions", basicAuth, new Object[]{"*",999,0});
        return JSON.parseArray(listtransactions);
    }

    public JSONObject getTransaction(String txId) throws Exception {
        String gettransaction = BTCRPCRquest.invoke(URL, "gettransaction", basicAuth, new Object[]{txId});
        return JSON.parseObject(gettransaction);
    }


}
