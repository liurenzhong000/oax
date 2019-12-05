package com.oax.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oax.admin.exception.MyException;
import com.oax.admin.service.EthTranthferService;
import com.oax.admin.service.IWithdrawService;
import com.oax.admin.walletclient.EthApiClient;
import com.oax.common.ResultResponse;
import com.oax.common.enums.PlatformTransferTypeEnum;
import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.front.PlatformTransfer;
import com.oax.entity.front.Withdraw;
import com.oax.mapper.front.PlatformTransferMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/4
 * Time: 20:55
 */
@Service
public class EthTranthferServiceImpl implements EthTranthferService {

    @Autowired
    private EthApiClient ethApiClient;

    @Autowired
    private IWithdrawService withdrawService;

    @Autowired
    private PlatformTransferMapper platformTransferMapper;


    /**
     * //1 根据 hash查询对应记录(自己平台地址)
     * //2 覆盖
     * //3 更新表
     *
     * @param hash
     */
    @Override
    public void coverBlockingTransfer(String hash) throws MyException{

        Withdraw withdraw = withdrawService.selectByHash(hash);
        PlatformTransfer platformTransfer = platformTransferMapper.selectByHash(hash);
        if (withdraw != null && platformTransfer == null) {

            if (withdraw.getStatus()!=WithdrawStatusEnum.IN_TXPOOL_STATUS.getStatus()) {
                throw new MyException("此交易状态不能,覆盖操作");
            }
            ResultResponse resultResponse = ethApiClient.coverBlockingTransfer(hash,withdraw.getCoinId());
            if (resultResponse.isSuccess()){
                JSONObject params = JSON.parseObject(JSON.toJSONString(resultResponse.getData()));
                Integer gaslimit = params.getInteger("gas");
                Integer gasPrice = params.getInteger("gasPrice");
                String txHashId = params.getString("txHashId");

                withdraw.setTxId(txHashId);
                withdraw.setGasLimit(gaslimit);
                withdraw.setGasPrice(gasPrice);
                withdraw.setStatus(WithdrawStatusEnum.IN_TXPOOL_STATUS.getStatus());
                withdrawService.update(withdraw);
            }else {
                throw new MyException(resultResponse.getMsg());
            }
        } else if (platformTransfer != null && withdraw == null) {


            if (platformTransfer.getStatus()!=PlatformTransferTypeEnum.IN_TXPOOL_STATUS.getType()){
                throw new MyException("此交易状态不能,覆盖操作");
            }

            ResultResponse resultResponse = ethApiClient.coverBlockingTransfer(hash, platformTransfer.getCoinId());
            if (resultResponse.isSuccess()){
                JSONObject params = JSON.parseObject(JSON.toJSONString(resultResponse.getData()));
                Integer gaslimit = params.getInteger("gas");
                Integer gasPrice = params.getInteger("gasPrice");
                String txHashId = params.getString("txHashId");

                platformTransfer.setTxId(txHashId);
                platformTransfer.setGasLimit(gaslimit);
                platformTransfer.setGasPrice(gasPrice);
                platformTransfer.setStatus(PlatformTransferTypeEnum.IN_TXPOOL_STATUS.getType());
                platformTransferMapper.updateByPrimaryKeySelective(platformTransfer);
            }else {
                throw new MyException(resultResponse.getMsg());
            }
        } else {
            //TODO 不是本平台 转账..但堵塞
            throw new MyException("堵塞交易hash:"+hash+",不为本平台交易");
        }


    }

    /**
     * //1 查询 转出记录
     * //2 覆盖
     * //3 更新
     *
     * @param withdraw
     */
    @Override
    public void replaceTransfer(Withdraw withdraw) throws MyException{


        ResultResponse resultResponse = ethApiClient.replaceTransfer(withdraw.getId());

        if (resultResponse.isSuccess()){
            JSONObject params = JSON.parseObject(JSON.toJSONString(resultResponse.getData()));
            Integer gaslimit = params.getInteger("gas");
            Integer gasPrice = params.getInteger("gasPrice");
            String txHashId = params.getString("txHashId");

            withdraw.setTxId(txHashId);
            withdraw.setGasLimit(gaslimit);
            withdraw.setGasPrice(gasPrice);
            withdraw.setStatus(WithdrawStatusEnum.IN_TXPOOL_STATUS.getStatus());
            withdrawService.update(withdraw);
        }else {
            throw new MyException(resultResponse.getMsg());
        }
    }
}
