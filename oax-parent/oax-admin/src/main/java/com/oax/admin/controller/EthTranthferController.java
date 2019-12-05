package com.oax.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.admin.service.EthTranthferService;
import com.oax.admin.service.IWithdrawService;
import com.oax.admin.walletclient.EthApiClient;
import com.oax.common.ResultResponse;
import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.front.Withdraw;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/4
 * Time: 20:36
 * eth交易 控制层
 */
@RestController
@RequestMapping("/ethtranthfer")
public class EthTranthferController {

    @Autowired
    private EthApiClient ethApiClient;

    @Autowired
    private EthTranthferService ethTranthferService;

    @Autowired
    private IWithdrawService withdrawService;


    @GetMapping("/txpool/{address}/blocking")
    public ResultResponse getTxpoolCoinBlocking(@PathVariable("address")String address) {
        return ethApiClient.getTxpoolCoinBlocking(address);
    }

    @PostMapping("/coverTransfer/{hash}")
    public ResultResponse coverBlockingTransfer(@PathVariable("hash") String hash) {

        //1 根据 hash查询对应记录(自己平台地址)
        //2 覆盖
        //3 更新表
        try {
            ethTranthferService.coverBlockingTransfer(hash);
            return new ResultResponse(true,"覆盖成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false,e.getMessage());
        }
    }

    @PostMapping("/replaceTransfer/{withdrawId}")
    public ResultResponse replaceTransfer(@PathVariable("withdrawId")int withdrawId){
        //1 查询 转出记录
        //2 覆盖
        //3 更新
        Withdraw withdraw = withdrawService.selectById(withdrawId);


        if (withdraw==null){
            return new ResultResponse(false,"不存的转出记录"+withdrawId);
        }

        if (withdraw.getStatus()!=WithdrawStatusEnum.FALL_STATUS.getStatus()){
            return new ResultResponse(false,"此交易状态不能,补单操作");
        }

        try {
            ethTranthferService.replaceTransfer(withdraw);
            return new ResultResponse(true,"补单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false,e.getMessage());
        }

    }

    @GetMapping("/syncing")
    public ResultResponse ethSyncing(){
        return ethApiClient.ethSyncing();
    }



}
