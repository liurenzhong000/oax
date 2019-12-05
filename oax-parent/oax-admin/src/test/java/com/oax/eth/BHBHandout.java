package com.oax.eth;

import com.oax.common.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Ethereum;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Collection;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * @Auther: hyp
 * @Date: 2018/12/30 09:29
 * @Description: BHB分发程序，web3j
 * https://www.jianshu.com/p/1b716180bc4b
 */
@SpringBootTest
@Slf4j
public class BHBHandout {

    @Test
    public void test() {
        System.out.println(1);
    }

    /*************创建一个钱包文件**************/
    @Test
    public void createAccount() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        String walletFileName0="";//文件名
        String walletFilePath0="D:\\work\\bhb-wallet";
        //钱包文件保持路径，请替换位自己的某文件夹路径

        walletFileName0 = WalletUtils.generateNewWalletFile("zcdPassword", new File(walletFilePath0), false);
        //WalletUtils.generateFullNewWalletFile("password1",new File(walleFilePath1));
        //WalletUtils.generateLightNewWalletFile("password2",new File(walleFilePath2));
        log.info("walletName: "+walletFileName0);
    }

    /********加载钱包文件**********/
    @Test
    public void loadWallet() throws IOException, CipherException {
        String walletFilePath="D:\\work\\BHBHandout\\UTC--2018-12-30T01-35-19.174000000Z--b00d2b549bfae38481eeaf35e02ef4acbd90bc2a.json";
        String passWord="123456";
        Credentials credentials = WalletUtils.loadCredentials(passWord, walletFilePath);
        String address = credentials.getAddress();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

        log.info("address="+address);
        log.info("public key="+publicKey);
        log.info("private key="+privateKey);

    }

    /*******连接以太坊客户端**************/
    @Test
    public void connectETHclient() throws IOException {
        Admin web3j = Admin.build(new HttpService("https://mainnet.infura.io/v3/575f8945fae34aaeaf328a68729df48"));
        //连接方式1：使用infura 提供的客户端
//        Ethereum web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/575f8945fae34aaeaf328a68729df48c"));// token更改为自己的
        //连接方式2：使用本地客户端
        //web3j = Web3j.build(new HttpService("127.0.0.1:7545"));
        //测试是否连接成功
        String web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        log.info("version=" + web3ClientVersion);


//        PersonalUnlockAccount personalUnlockAccount = web3j.personalUnlockAccount("0xb00d2b549bfae38481eeaf35e02ef4acbd90bc2a", "123456").send();
//        System.out.println(personalUnlockAccount.accountUnlocked());
//        if (personalUnlockAccount.accountUnlocked()) {
//            // send a transaction
//        }
        log.info(JsonHelper.writeValueAsString(web3j.personalListAccounts()));

    }

    /***********查询指定地址的余额***********/
    @Test
    public void getBalanceOf() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/575f8945fae34aaeaf328a68729df48c"));// token更改为自己的
        if (web3j == null) return;
        String address = "0x4770BC542b639D42aB4A17925212828B483928f9";//等待查询余额的地址（黑哥地址）
        //第二个参数：区块的参数，建议选最新区块
        EthGetBalance balance = web3j.ethGetBalance(address,  DefaultBlockParameter.valueOf("latest")).send();
        //格式转化 wei-ether
        String balanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
        log.info(balanceETH);

        String walletFilePath="D:\\work\\BHBHandout\\my\\imtoken_keystore.json";//0xaabab5002b9fae05aad9ae95ec3532b96a06db15
        String passWord="hao5287317";
        Credentials credentials = WalletUtils.loadCredentials(passWord, walletFilePath);
        System.out.println("address="+credentials.getAddress());

        //获取账户内的bhb余额
//        Function function = new Function(
//                "balanceOf",
//                Arrays.asList(new Address("0x8c905b210AbE598B105e840fc2eA382801C12529")),  // Solidity Types in smart contract functions
//                Arrays.asList(new TypeReference<Type>() {
//                }));
//        String encodedFunction = FunctionEncoder.encode(function);
//        EthCall response = web3j.ethCall(
//                Transaction.createEthCallTransaction(address, "0x8c905b210AbE598B105e840fc2eA382801C12529", encodedFunction),
//                DefaultBlockParameterName.LATEST)
//                .sendAsync().get();
//
//        String returnValue = response.getValue(); //返回16进制余额
//        returnValue = returnValue.substring(2);
//        BigInteger balanceBHB = new BigInteger(returnValue, 16);
//        System.out.println("balanceBHB="+balanceBHB);

        BHB bhb = BHB.load("0x8c905b210AbE598B105e840fc2eA382801C12529", web3j, credentials, BigInteger.valueOf(0L), BigInteger.valueOf(0));
        RemoteCall<BigInteger> balanceOf = bhb.balanceOf("0x4770BC542b639D42aB4A17925212828B483928f9");
        BigInteger send2 = balanceOf.send();
        System.out.println(toDecimal(18, send2));

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                "0x4770BC542b639D42aB4A17925212828B483928f9", DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        System.out.println("nonce="+nonce);

    }

    /****************交易*****************/
    private void transto() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/575f8945fae34aaeaf328a68729df48c"));
        String walletFilePath="D:\\work\\BHBHandout\\base_acount_keystore.json";
        String passWord="123456";
        Credentials credentials = WalletUtils.loadCredentials(passWord, walletFilePath);

        if (web3j == null) return;
        if (credentials == null) return;
        //开始发送0.01 =eth到指定地址
        String address_to = "0x4770BC542b639D42aB4A17925212828B483928f9";
        TransactionReceipt send = Transfer.sendFunds(web3j, credentials, address_to, BigDecimal.ONE, Convert.Unit.FINNEY).send();

        log.info("Transaction complete:");
        log.info("trans hash=" + send.getTransactionHash());
        log.info("from :" + send.getFrom());
        log.info("to:" + send.getTo());
        log.info("gas used=" + send.getGasUsed());
        log.info("status: " + send.getStatus());
    }

    @Test
    public void rawTransaction() throws IOException, CipherException, ExecutionException, InterruptedException {
        String fromAddress = "0x4770BC542b639D42aB4A17925212828B483928f9";
        String toAddress = "0xb00d2b549bfae38481eeaf35e02ef4acbd90bc2a";
        String amount = "1";
        String contractAddress = "0x8c905b210AbE598B105e840fc2eA382801C12529";//合约地址

        Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/575f8945fae34aaeaf328a68729df48c"));
        String walletFilePath="D:\\work\\BHBHandout\\base_account_keystore.json";
        String passWord="zzf195765901";
        Credentials credentials = WalletUtils.loadCredentials(passWord, walletFilePath);

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        log.info("nonce={}", nonce);

        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(toAddress), new Uint256(new BigInteger(amount))),
                Arrays.asList(new TypeReference<Type>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, Convert.toWei("18", Convert.Unit.GWEI).toBigInteger(),
                Convert.toWei("100000", Convert.Unit.WEI).toBigInteger(), contractAddress, encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        log.info("transfer hexValue:" + hexValue);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        if (ethSendTransaction.hasError()) {
            log.info("transfer error:", ethSendTransaction.getError().getMessage());

        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            log.info("Transfer transactionHash:" + transactionHash);

        }

    }


    /**
     * 转换成符合 decimal 的数值
     * @param decimal
     * @return
     */
    public static String toDecimal(int decimal,BigInteger integer){
//		String substring = str.substring(str.length() - decimal);
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
        return balance;
    }



}
