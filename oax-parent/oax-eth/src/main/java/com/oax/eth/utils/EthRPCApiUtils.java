package com.oax.eth.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.oax.common.RPCRquest;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/20
 * Time: 14:01
 * 请求ETH节点,所有信息
 */
@Slf4j
@Component
public class EthRPCApiUtils {

    @Value("${eth.server.url}")
    private String walletURL;
//    private static final String walletURL = "http://47.52.225.58:6677";

    public static String fillZero(String input) {
        String str = input.replace("0x", "");

        int strLen = input.length();
        StringBuffer sb = null;
        while (strLen < 64) {
            sb = new StringBuffer();
            sb.append("0").append(str);// 左补0
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

    public static BigDecimal stringToDBNum(String hexString) {
        hexString = hexString.substring(2);
        BigInteger bigInteger = new BigInteger(hexString, 16);
        BigDecimal multiply = new BigDecimal(bigInteger).divide(new BigDecimal(10).pow(18));
        return multiply;
    }


    public static BigDecimal stringToDBNum(String hexString, int decimals) {
        hexString = hexString.substring(2);
        BigInteger bigInteger = new BigInteger(hexString, 16);
        BigDecimal multiply = new BigDecimal(bigInteger).divide(new BigDecimal(10).pow(decimals));
        return multiply;
    }

    public static BigInteger toNum(String hexString) {
        hexString = hexString.substring(2);
        return new BigInteger(hexString, 16);
    }

    public static String toHexString(BigInteger numString) {
        return "0x" + numString.toString(16);
    }


    /**
     * 获取当前 安全GasPrice价格
     *
     * @return 数据库存入值 单位Gwei
     * @throws Exception
     */
    public int ethGasPrice() throws Exception {
        String ethGasPrice = RPCRquest.invoke(walletURL, "eth_gasPrice", new Object[]{});
        return ethGasPrice2DbGasPrice(ethGasPrice);
    }


    public String ethSyncing() throws Exception {
        String jsons = RPCRquest.invoke(walletURL, "eth_syncing", new Object[]{});
        return jsons;
    }

    /**
     * 获取最近一个区块数(区块高度)
     *
     * @return
     */
    public BigInteger getBlockNumber() throws Exception {
        String num = RPCRquest.invoke(walletURL, "eth_blockNumber", new Object[]{});

        return toNum(num);
    }

    /**
     * 获取指定区块中 交易记录数(count) index从0开始
     *
     * @param blockNumber
     * @return
     */
    public BigInteger getBlockTransactionCountByNumber(BigInteger blockNumber) throws Exception {
        String count = RPCRquest.invoke(walletURL, "eth_getBlockTransactionCountByNumber", new Object[]{toHexString(blockNumber)});

        return toNum(count);
    }

    /**
     * 获取指定区块(blockNumber)中指定事务(交易记录,事务index)
     *
     * @param blockNumber
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getTransactionByBlockNumberAndIndex(BigInteger blockNumber, BigInteger index) throws Exception {
        String jsons = RPCRquest.invoke(walletURL, "eth_getTransactionByBlockNumberAndIndex", new Object[]{toHexString(blockNumber), toHexString(index)});

		/*
		 * eth转账
		 *
		 * {
		    "jsonrpc": "2.0",
		    "id": 1,
		    "result": {
		        "blockHash": "0xdd5f178275cadaea45af211908afb3a387d154ec91f784f5dc84794aac63d31b",
		        "blockNumber": "0x260f2b",
		        "from": "0xe847380de90060074f53af90fad193f4f94450d3",
		        "gas": "0x5208",
		        "gasPrice": "0x3b9aca00",
		        "hash": "0x961be12561d1df2ee4563cb78867885ace917a5d83e9f74d83c1ee5a54581ac9",
		        "input": "0x", //如果input为0x6f6178时为申请手续费
		        "nonce": "0x354",
		        "to": "0x951fc27402eff842edb9091f81ae799ef1883156",
		        "transactionIndex": "0x5",
		        "value": "0x16345785d8a0000",
		        "v": "0x2b",
		        "r": "0x6ffe12530e7dc3b777c208a06cbf0c6714028a4a075daab49512e81c6f1fc9d5",
		        "s": "0x43a3c4adf9477ec6b7d3e6742e5d57155da2bcd606b7c464877b13fe7f27116f"
		    }
		}*/


		/*
		 * 代币
		 * {
		    "jsonrpc": "2.0",
		    "id": 1,
		    "result": {
		        "blockHash": "0xe7ca5ad1a806b8b15fb6e980dab1eea184ea47a0cde7fef066f2563925cca226",
		        "blockNumber": "0x2285db",
		        "from": "0x17749c6ce8facd525aa29329eb48b8c24619a9e5",
		        "gas": "0x134f1",
		        "gasPrice": "0x98bca5a00",
		        "hash": "0x34a06a4bfd48759a3fe374175e9ab90168772930af353d1dd7a29bd1ffbf0db7",
		        "input": "0xa9059cbb000000000000000000000000906e1b3dd5c4043f898a583d5fe9ed2b956265850000000000000000000000000000000000000000000000000000000ba43b7400",
		        "nonce": "0x32",
		        "to": "0x9faab12b8b19ba5849fcb3776b2a2d66d0366dc1", -- 代币转账to为合约地址
		        "transactionIndex": "0x0",
		        "value": "0x0",
		        "v": "0x2b",
		        "r": "0x8a9ab10d78e441d0b86dd76a299f19b9285037108e4fccd8455e45864f934d34",
		        "s": "0x32636acd62603e44a03ff4772d349e1bfee0cfb149634f3895e6829a90afd4bc"
		    }
		}*/
        return JSONObject.parseObject(jsons, Map.class);
    }

    /**
     * 通过 交易hash获取 交易记录
     *
     * @param txHash
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getTransactionByHash(String txHash) throws Exception {
        String jsons = RPCRquest.invoke(walletURL, "eth_getTransactionByHash", new Object[]{txHash});

		/*
		 * eth转账
		 *
		 * {
		    "jsonrpc": "2.0",
		    "id": 1,
		    "result": {
		        "blockHash": "0xdd5f178275cadaea45af211908afb3a387d154ec91f784f5dc84794aac63d31b",
		        "blockNumber": "0x260f2b",
		        "from": "0xe847380de90060074f53af90fad193f4f94450d3",
		        "gas": "0x5208",
		        "gasPrice": "0x3b9aca00",
		        "hash": "0x961be12561d1df2ee4563cb78867885ace917a5d83e9f74d83c1ee5a54581ac9",
		        "input": "0x",  //如果input为0x6f6178时为申请手续费
		        "nonce": "0x354",
		        "to": "0x951fc27402eff842edb9091f81ae799ef1883156",
		        "transactionIndex": "0x5",
		        "value": "0x16345785d8a0000",
		        "v": "0x2b",
		        "r": "0x6ffe12530e7dc3b777c208a06cbf0c6714028a4a075daab49512e81c6f1fc9d5",
		        "s": "0x43a3c4adf9477ec6b7d3e6742e5d57155da2bcd606b7c464877b13fe7f27116f"
		    }
		}*/


		/*
		 * 代币
		 * {
		    "jsonrpc": "2.0",
		    "id": 1,
		    "result": {
		        "blockHash": "0xe7ca5ad1a806b8b15fb6e980dab1eea184ea47a0cde7fef066f2563925cca226",
		        "blockNumber": "0x2285db",
		        "from": "0x17749c6ce8facd525aa29329eb48b8c24619a9e5",
		        "gas": "0x134f1",
		        "gasPrice": "0x98bca5a00",
		        "hash": "0x34a06a4bfd48759a3fe374175e9ab90168772930af353d1dd7a29bd1ffbf0db7",
		        "input": "0xa9059cbb000000000000000000000000906e1b3dd5c4043f898a583d5fe9ed2b956265850000000000000000000000000000000000000000000000000000000ba43b7400",
		        "nonce": "0x32",
		        "to": "0x9faab12b8b19ba5849fcb3776b2a2d66d0366dc1", -- 代币转账to为合约地址
		        "transactionIndex": "0x0",
		        "value": "0x0",
		        "v": "0x2b",
		        "r": "0x8a9ab10d78e441d0b86dd76a299f19b9285037108e4fccd8455e45864f934d34",
		        "s": "0x32636acd62603e44a03ff4772d349e1bfee0cfb149634f3895e6829a90afd4bc"
		    }
		}*/

//        0xa9059cbb //methodId
//        000000000000000000000000
//        906e1b3dd5c4043f898a583d5fe9ed2b95626585 //to
//        0000000000000000000000000000000000000000000000000000000
//        ba43b7400 // qty

        return JSONObject.parseObject(jsons, Map.class);
    }

    /**
     * 获取 以太坊确认的交易
     *
     * @param txHash
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getTransactionReceipt(String txHash) throws Exception {
        String jsons = RPCRquest.invoke(walletURL, "eth_getTransactionReceipt", new Object[]{txHash});

		/*
		 * eth转账
		 *
		 *{
			    "jsonrpc": "2.0",
			    "id": 1,
			    "result": {
			        "blockHash": "0xdd5f178275cadaea45af211908afb3a387d154ec91f784f5dc84794aac63d31b",
			        "blockNumber": "0x260f2b",
			        "contractAddress": null,
			        "cumulativeGasUsed": "0x48d58",
			        "from": "0xe847380de90060074f53af90fad193f4f94450d3",
			        "gasUsed": "0x5208",
			        "logs": [],
			        "logsBloom": "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
			        "status": "0x1",	// status为1表示成功
			        "to": "0x951fc27402eff842edb9091f81ae799ef1883156",
			        "transactionHash": "0x961be12561d1df2ee4563cb78867885ace917a5d83e9f74d83c1ee5a54581ac9",
			        "transactionIndex": "0x5"
			    }
			}
		}*/


		/*
		 * 代币
		 *{
			    "jsonrpc": "2.0",
			    "id": 1,
			    "result": {
			        "blockHash": "0xe7ca5ad1a806b8b15fb6e980dab1eea184ea47a0cde7fef066f2563925cca226",
			        "blockNumber": "0x2285db",
			        "contractAddress": null,
			        "cumulativeGasUsed": "0xcdf6",
			        "from": "0x17749c6ce8facd525aa29329eb48b8c24619a9e5",
			        "gasUsed": "0xcdf6",
			        "logs": [
			            {
			                "address": "0x9faab12b8b19ba5849fcb3776b2a2d66d0366dc1",
			                "topics": [
			                    "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
			                    "0x00000000000000000000000017749c6ce8facd525aa29329eb48b8c24619a9e5",
			                    "0x000000000000000000000000906e1b3dd5c4043f898a583d5fe9ed2b95626585"
			                ],
			                "data": "0x0000000000000000000000000000000000000000000000000000000ba43b7400",
			                "blockNumber": "0x2285db",
			                "transactionHash": "0x34a06a4bfd48759a3fe374175e9ab90168772930af353d1dd7a29bd1ffbf0db7",
			                "transactionIndex": "0x0",
			                "blockHash": "0xe7ca5ad1a806b8b15fb6e980dab1eea184ea47a0cde7fef066f2563925cca226",
			                "logIndex": "0x0",
			                "removed": false
			            }
			        ],
			        "logsBloom": "0x00000000000000000000000000000000000000000000000000000008000000000000000000000000000000800000000000000000000000000000000000000000000001000000000000000008000000000000000002000000000000000000000000000000040000000000000000000000000200000000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000080000400000000000000000000000",
			        "status": "0x1",
			        "to": "0x9faab12b8b19ba5849fcb3776b2a2d66d0366dc1",
			        "transactionHash": "0x34a06a4bfd48759a3fe374175e9ab90168772930af353d1dd7a29bd1ffbf0db7",
			        "transactionIndex": "0x0"
			    }
			}
		}*/


//        0xa9059cbb //methodId
//        000000000000000000000000
//        906e1b3dd5c4043f898a583d5fe9ed2b95626585 //to
//        0000000000000000000000000000000000000000000000000000000
//        ba43b7400 // qty
        return JSONObject.parseObject(jsons, Map.class);
    }

    /**
     * 获取 ETH余额
     *
     * @param address 查询地址
     * @return
     * @throws Throwable
     */
    public String getETHBalance(String address) throws Throwable {

        String balance = RPCRquest.invoke(walletURL, "eth_getBalance", new Object[]{address, "pending"});
        return balance;
    }


    /**
     * 获取ETHToken余额
     *
     * @param address         查询地址
     * @param contractAddress 合约地址
     * @return
     * @throws Throwable
     */
    public String getTokenBalance(String address, String contractAddress) throws Throwable {

        String data = "0x70a08231" + fillZero(address);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("to", contractAddress);
        param.put("data", data);

        String balance = RPCRquest.invoke(walletURL, "eth_call", new Object[]{param, "pending"});
        return balance;
    }

    /**
     * ETH/token转账
     *
     * @param contractAddress 合约地址  为null时,为eth转账
     * @param methodId        合约方法  为null时,为eth转账
     * @param from            转出地址
     * @param to              转入地址
     * @param qty             转账金额(0x +  十六进制)wei单位 18
     * @param password        from密码
     * @throws Exception
     */
    public Map<String, Object> transfer(String contractAddress, String methodId, String from, String to, String qty,  String password) throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("from", from);
        if (StringUtils.isNoneEmpty(contractAddress) &&
                StringUtils.isNoneEmpty(methodId)) {
            String data = methodId + fillZero(to) + fillZero(qty);
            param.put("value", "0x0");
            param.put("data", data);
            param.put("to", contractAddress);
        } else {
            param.put("value", qty);
            param.put("to", to);
        }

        int ethGasPrice = this.ethGasPrice();
        String gasPrice = dbGasPrice2EthGasPrice((int) Math.round(ethGasPrice * 1.5));
        param.put("gasPrice", gasPrice);
        param.put("gas", getGasLimit(param));

        String txHashId = RPCRquest.invoke(walletURL, "personal_sendTransaction", new Object[]{param, password});
        log.info("--完成token转账-txHashId:{}--", txHashId);
        param.put("txHashId", txHashId);
        return param;

    }

    /**
     * ETH转账
     *
     * @param from     转出地址
     * @param to       转入地址
     * @param qty      转账金额(0x +  十六进制)wei单位 18
     * @param password from密码
     * @throws Exception
     */
    public Map<String, Object> transferETH(String from, String to, String qty, String password) throws Exception {

        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("from", from);
        param.put("to", to);

        int ethGasPrice = this.ethGasPrice();
        String gasPrice = dbGasPrice2EthGasPrice((int) Math.round(ethGasPrice * 1.5));
//        param.put("gas", gasLimit);
        param.put("gasPrice", gasPrice);
        param.put("value", qty);


        param.put("gas", getGasLimit(param));
        String txHashId = RPCRquest.invoke(walletURL, "personal_sendTransaction", new Object[]{param, password});
        log.info("--完成token转账-txHashId:{}--", txHashId);
        param.put("txHashId", txHashId);

        return param;

    }

    /**
     * EHTToken转账
     *
     * @param contractAddress 合约地址
     * @param methodId        合约方法
     * @param from            转出地址
     * @param to              转入地址
     * @param qty             转账金额(0x +  十六进制)wei单位 18
     * @param password        from密码
     * @throws Exception
     */
    public Map<String, Object> transferToken(String contractAddress, String methodId, String from, String to, String qty, String password) throws Exception {

        String data = methodId + fillZero(to) + fillZero(qty);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("from", from);
        param.put("to", contractAddress);
//        param.put("gas", gasLimit);

        int ethGasPrice = this.ethGasPrice();
        String gasPrice = dbGasPrice2EthGasPrice((int) Math.round(ethGasPrice * 1.5));
        param.put("gasPrice", gasPrice);
        param.put("value", "0x0");
        param.put("data", data);

        param.put("gas", getGasLimit(param));


        String txHashId = RPCRquest.invoke(walletURL, "personal_sendTransaction", new Object[]{param, password});
        log.info("--完成token转账-txHashId:{}--", txHashId);
        param.put("txHashId", txHashId);
        return param;
    }

    /**
     * ETH转账
     * 覆盖 为打包的转账
     *
     * @param from     转出地址
     * @param to       转入地址
     * @param qty      转账金额(0x +  十六进制)wei单位 18
     * @param password from密码
     * @throws Exception
     */
    public Map<String, Object> coverTransferETH(String from, String to, String qty, String password, String nonce, String input,String oldgasPrice ) throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("from", from);
        param.put("to", to);

        int ethGasPrice = EthRPCApiUtils.ethGasPrice2DbGasPrice(oldgasPrice);
        String gasPrice = dbGasPrice2EthGasPrice((int) Math.round(ethGasPrice * 1.5));

        param.put("gasPrice", gasPrice);
        param.put("value", qty);
        if (StringUtils.isNoneEmpty(nonce)) {
            param.put("nonce", nonce);
        }
        if (StringUtils.isNoneEmpty(input)) {
            param.put("data", input);
        }
        param.put("gas", getGasLimit(param));
        String txHashId = RPCRquest.invoke(walletURL, "personal_sendTransaction", new Object[]{param, password});
        log.info("--完成token转账-txHashId:{}--", txHashId);
        param.put("txHashId", txHashId);
        return param;

    }

    /**
     * ETH转账 为了手续费
     *
     * @param from     转出地址
     * @param to       转入地址
     * @param qty      转账金额(0x +  十六进制)wei单位 18
     * @param password from密码
     * @throws Exception
     */
    public Map<String, Object> transferETHForFee(String from, String to, String qty, String password) throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("from", from);
        param.put("to", to);
//        param.put("gas", gasLimit);
        int ethGasPrice = this.ethGasPrice();
        String gasPrice = dbGasPrice2EthGasPrice((int) Math.round(ethGasPrice * 1.5));
        param.put("gasPrice", gasPrice);
        param.put("value", qty);
        param.put("data", "0x6f6178");


        param.put("gas", getGasLimit(param));

        String txHashId = RPCRquest.invoke(walletURL, "personal_sendTransaction", new Object[]{param, password});
        log.info("--完成token转账-txHashId:{}--", txHashId);
        param.put("txHashId", txHashId);
        return param;

    }


    /**
     * EHT/Token获取建议 gaslimit
     *
     * @return eth建议的gaslimit(可直接使用)
     * @throws Exception
     */
    public String getGasLimit(Map<String, Object> params) throws Exception {
        String gaslimit = RPCRquest.invoke(walletURL, "eth_estimateGas", new Object[]{params});
        BigInteger bigInteger = EthRPCApiUtils.toNum(gaslimit);
        BigInteger multiply = bigInteger.multiply(new BigInteger("2"));
        return toHexString(multiply);
    }

    /**
     * EHT/Token获取建议 gaslimit
     *
     * @param contractAddress 合约地址  为null时,为eth转账
     * @param methodId        合约方法  为null时,为eth转账
     * @param from            转出地址
     * @param to              转入地址
     * @param qty             转账金额(0x +  十六进制)wei单位 18
     * @param gasLimit        最大限制(0x +  十六进制)
     * @param gasPrice        手续费单价(0x + 十六进制)Gwei单位 9
     * @throws Exception
     */
    public String getGasLimit(String contractAddress,
                                     String methodId,
                                     String from,
                                     String to,
                                     String qty,
                                     String gasLimit,
                                     String gasPrice) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("from", from);
        if (StringUtils.isNoneEmpty(contractAddress)) {
            String data = methodId + fillZero(to) + fillZero(qty);
            param.put("to", contractAddress);
            param.put("data", data);
            param.put("value", "0x0");
        } else {
            param.put("to", to);
            param.put("value", qty);
        }
        param.put("gas", gasLimit);
        param.put("gasPrice", gasPrice);

        String gaslimit = RPCRquest.invoke(walletURL, "eth_estimateGas", new Object[]{param});

        log.info("-----获取gaslimit:{}-----", gasLimit);
        return gaslimit;
    }


    /**
     * fee2user获取gaslimit,已成
     * @param contractAddress
     * @param methodId
     * @param from
     * @param to
     * @return
     * @throws Exception
     */
    public int getGasLimit(String contractAddress,
                                  String methodId,
                                  String from,
                                  String to) throws Exception {
        String qty = "0x5";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("from", from);
        String data = methodId + fillZero(to) + fillZero(qty);
        param.put("to", contractAddress);
        param.put("data", data);
        param.put("value", "0x0");
        String gaslimit = RPCRquest.invoke(walletURL, "eth_estimateGas", new Object[]{param});


        return toNum(gaslimit).intValue();
    }

    /**
     * 创建新帐户
     *
     * @param pwd 密码
     * @return
     * @throws Exception
     */
    public String newAccount(String pwd) throws Exception {
        return RPCRquest.invoke(walletURL, "personal_newAccount", new Object[]{pwd});
    }

    /**
     * 获取 本地交易池
     *
     * @return {
     * "pending": {
     * "0x00B8FBD65D61b7DFe34b9A3Bb6C81908d7fFD541": {
     * "397": {
     * "blockHash": "0x0000000000000000000000000000000000000000000000000000000000000000",
     * "blockNumber": null,
     * "from": "0x00b8fbd65d61b7dfe34b9a3bb6c81908d7ffd541",
     * "gas": "0x6691b7",
     * "gasPrice": "0x174876e800",
     * "hash": "0x09fddc1a3410556515d3759c655c657f7be69b2cb98e71749546928017043de6",
     * "input": "0x",
     * "nonce": "0x18d",
     * "to": null,
     * "transactionIndex": "0x0",
     * "value": "0x0",
     * "v": "0x1b",
     * "r": "0x68ae89974eb81b9fc1f6d83cd42b48ed0d829466b56ee0ceac92a92f678855e3",
     * "s": "0x3834369388b962a0aa8346d9fe19fe962e28034c32ee720e3a53a2bd22ae6c97"
     * }
     * }
     * },
     * "queued": {
     * "0x05A94caaCdb83e8B9E0b9a582988de448440ECc1": {
     * "665": {
     * "blockHash": "0x0000000000000000000000000000000000000000000000000000000000000000",
     * "blockNumber": null,
     * "from": "0x05a94caacdb83e8b9e0b9a582988de448440ecc1",
     * "gas": "0x21db3",
     * "gasPrice": "0x3b9aca00",
     * "hash": "0x0b6fa0d19ade44d0a94a4294136fd8c06916e8a70fbf672ba2b62036bc554f5b",
     * "input": "0x",
     * "nonce": "0x299",
     * "to": "0xa35c16f796f0d7dd67fc80be7e14a83eb0942224",
     * "transactionIndex": "0x0",
     * "value": "0x0",
     * "v": "0x2c",
     * "r": "0x4c9b3664f4294cec064b7e6dfa32d6f897a3e05c110d94d4615225a30fc60a17",
     * "s": "0x10161333c61cd5067c595c7eb94b38fa8cacf4388ec8c97b091ad8383bb5b27a"
     * }
     * }
     * }
     * }
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public Map<String, Map<String, Map<String, Map<String, String>>>> txpoolContent() throws Exception {
        String txpoolContent = RPCRquest.invoke(walletURL, "txpool_content", new Object[]{});
        return JSONObject.parseObject(txpoolContent, Map.class);
    }

    /**
     * 将所有txpoolContent blocking抽取
     *
     * @return
     */
    public ArrayList<Map<String, String>> getAllTxpoolBlockings() {
        Map<String, Map<String, Map<String, Map<String, String>>>> stringMapMap = null;
        try {
            stringMapMap = this.txpoolContent();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Set<String> statusKeys = stringMapMap.keySet();

        ArrayList<Map<String, String>> allblockingTxList = new ArrayList<>();

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

        allblockingTxList.sort(Comparator.comparing(e -> EthRPCApiUtils.toNum(e.get("nonce"))));
        return allblockingTxList;
    }


    public static void main(String[] args) throws Throwable {


        BigDecimal qty = new BigDecimal("12836.6538");

        String ethHexQty = dbNum2EthNum(qty,18);


        String data = "0xa9059cbb" + fillZero("0x0CF2C62DF81B89D8870C3a5Df0c57b0B73f8E97D") + fillZero(ethHexQty);

        log.info(data);



    }


//    public static String dbNum2EthNum(BigDecimal dbNum) {
//        BigDecimal multiply = dbNum.multiply(new BigDecimal(10).pow(18));
//        log.info("dbNum2EthNum::{}", multiply.toBigInteger());
//        return toHexString(multiply.toBigInteger());
//    }
    public static String dbNum2EthNum(BigDecimal dbNum, int decimals) {
        BigDecimal multiply = dbNum.multiply(new BigDecimal(10).pow(decimals));
        log.info("dbNum2EthNum::{}", multiply.toBigInteger());
        return toHexString(multiply.toBigInteger());
    }

    public static BigDecimal dbGasPrice2EthNum(int dbNum) {
        BigDecimal bigDecimal = new BigDecimal(dbNum);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(10).pow(9));
        log.info("dbGasPrice2EthNum::{}", multiply.toBigInteger());
        return multiply;
    }

    public static String dbGasPrice2EthGasPrice(int dbNum) {
        BigDecimal bigDecimal = new BigDecimal(dbNum);
        BigDecimal divide = bigDecimal.multiply(new BigDecimal(10).pow(9));
        log.info("dbGasPrice2EthGasPrice::{}", divide.toBigInteger());
        return toHexString(divide.toBigInteger());
    }

    public static int ethGasPrice2DbGasPrice(String ethGasPrice) {
        BigInteger bigInteger = toNum(ethGasPrice);
        BigInteger divide = bigInteger.divide(new BigInteger("10").pow(9));
        log.info("ethGasPrice2DbGasPrice::{}", divide.intValue());
        return divide.intValue();
    }


    public static String getridof_zero_address(String input) throws Throwable {
        if (input.length() < 40) {
            return null;
        }
        String str = input.substring(input.length() - 40, input.length());
        return "0x" + str;
    }

    public static String getridof_zero(String input) throws Throwable {
        String str = input.replaceFirst("^0*", "");
        return "0x" + str;
    }
//
//    public static void main(String[] args) throws Throwable {
////
////        String methodId = "0xa9059cbb";
////        String inputData = "0xa9059cbb000000000000000000000000906e1b3dd5c4043f898a583d5fe9ed2b956265850000000000000000000000000000000000000000000000000000000ba43b7400";
////        //拆分合约数据
////        inputData = inputData.replace(methodId, "");
////        String toAddressInfo = inputData.substring(0, inputData.length() / 2);
////        String moneyInfo = inputData.substring(inputData.length() / 2);
////        String toAddress = getridof_zero_address(toAddressInfo);
//////        BigDecimal money = stringToDBNum(getridof_zero(moneyInfo));
////        String s = toNum(getridof_zero(moneyInfo)).toString();
////        BigDecimal money = new BigDecimal(toNum(getridof_zero(moneyInfo)).toString());
////        BigDecimal realMoney = money.divide(new BigDecimal(10).pow(8));
////
////        log.info("toAddress::{}",toAddress);
////        log.info("realMoney::{}",realMoney);
//
//
//        String ethBalance = getETHBalance("0x0CF2C62DF81B89D8870C3a5Df0c57b0B73f8E97D");
//        log.info("ethBalance::{}",ethBalance);
//        BigDecimal bigDecimal = stringToDBNum(ethBalance);
//
//        log.info("bigDecimal::{}",bigDecimal);
//        Map<String, Object> transactionByHash = getTransactionByHash("0x34a06a4bfd48759a3fe374175e9ab90168772930af353d1dd7a29bd1ffbf0db7");
//
//        log.info("transactionByHash;;{}", JSON.toJSONString(transactionByHash));
//
//
//        Map<String, Object> transactionByHash1 = getTransactionByHash("0x34A06A4bfd48759a3fe374175e9aB90168772930af353d1dd7a29bd1ffbf0db7");
//
//        log.info("transactionByHash1;;{}", JSON.toJSONString(transactionByHash1));
//    }

}
