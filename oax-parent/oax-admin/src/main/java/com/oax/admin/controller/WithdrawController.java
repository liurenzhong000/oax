package com.oax.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.oax.admin.form.WithdrawFrom;
import com.oax.admin.service.CoinService;
import com.oax.admin.service.IWithdrawService;
import com.oax.admin.service.WithdrawLogService;
import com.oax.admin.walletclient.BtcApiClient;
import com.oax.admin.walletclient.EthApiClient;
import com.oax.common.AssertHelper;
import com.oax.common.PageResultResponse;
import com.oax.common.RedisUtil;
import com.oax.common.ResultResponse;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.admin.WithdrawLog;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.WithdrawPageParam;
import com.oax.entity.admin.vo.WithdrawFeeVo;
import com.oax.entity.admin.vo.WithdrawPageVo;
import com.oax.entity.admin.vo.WithdrawSums;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.entity.front.Withdraw;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:34
 * 用户体现(虚拟币转出) 控制层
 */
@RestController
@RequestMapping("/withdraws")
public class WithdrawController {

    @Autowired
    private IWithdrawService withdrawService;

    @Autowired
    private WithdrawLogService withdrawLogService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private EthApiClient ethApiClient;

    @Autowired
    private BtcApiClient btcApiClient;


    @PostMapping(value = "/page")
    public ResultResponse allWithdraw(@RequestBody WithdrawPageParam withdrawPageParam) {

        PageInfo<WithdrawPageVo> pageInfo = withdrawService.getByWithdrawPageParam(withdrawPageParam);

        PageResultResponse<WithdrawPageVo> pageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(pageInfo, pageResultResponse);

        pageResultResponse.setParam(withdrawPageParam);
        return new ResultResponse(true, pageResultResponse);
    }

    @PostMapping("/sum")
    public ResultResponse countWithdrawSumsByParam(@RequestBody WithdrawPageParam withdrawPageParam) {

        WithdrawSums withdrawSums = withdrawService.countWithdrawSumsByParam(withdrawPageParam);

        return new ResultResponse(true, withdrawSums);
    }

    @PostMapping("/fee")
    public ResultResponse getAllFee(@RequestBody SimpleCoinParam simpleCoinParam) {

        PageInfo<WithdrawFeeVo> pageInfo = withdrawService.sumWithdrawFee(simpleCoinParam);

        PageResultResponse<WithdrawFeeVo> pageResultResponse = new PageResultResponse<>();
        BeanUtils.copyProperties(pageInfo, pageResultResponse);
        pageResultResponse.setParam(simpleCoinParam);
        return new ResultResponse(true, pageResultResponse);
    }


    /***
     * 提现申请拉黑，后续处理
     */
    @PutMapping(value = "/withdraw/block")
    public ResultResponse blockCheck(@RequestBody WithdrawFrom from){
        AssertHelper.notEmpty(from.getWithdrawIds(), "id列表不能为空");
        withdrawService.blockWithdraw(Lists.newArrayList(from.getWithdrawIds()), from.getPassStatus(), from.getRemark());
        return new ResultResponse(true, "操作成功");
    }

    /**
     * 初审
     *
     * @param passStatus 是否通过 1通过 -1不通过
     * @return
     */
    @PostMapping(value = "/withdraw/fistCheck/{passStatus}",produces="application/json;charset=UTF-8")
    public ResultResponse fistWithdrawCheck(@RequestBody JSONObject jsonObject,@PathVariable("passStatus") byte passStatus,
                                            @RequestParam(name = "remark", required = false) String remark) {
        Map<String, String> map = JSONObject.toJavaObject(jsonObject, Map.class);
        List<String> list = new ArrayList<>();
        if(map.get("withdrawIdList").contains(",")){
            String withdrawIdList = map.get("withdrawIdList");
            list = Arrays.asList(withdrawIdList.substring(1,withdrawIdList.length()-1).split(","));
        }else {
            String withdrawIdList = map.get("withdrawIdList");
            list.add(withdrawIdList.substring(1,withdrawIdList.length()-1));
        }
        StringBuffer result = new StringBuffer();
        list.forEach(item->result.append(handleFirstCheck(Integer.valueOf(item),passStatus,remark)));
        return new ResultResponse(result.toString().contains("审核通过"), result.toString());
    }

    public String handleFirstCheck(Integer withdrawId,byte passStatus, String remark){
        Withdraw withdraw = withdrawService.selectById(withdrawId);

        if (withdraw == null) {
            return  withdrawId+"不存在的转出记录</br>";
        }

        Byte status = withdraw.getStatus();

        if (StringUtils.isNoneEmpty(remark)) {
            withdraw.setRemark(remark);
        }
        if (status != WithdrawStatusEnum.WAIT_FIRST_CHECK.getStatus()) {
            //状态不能初审
            return withdrawId+"此状态不能初审</br>";
        }

        StringBuilder description = new StringBuilder();
        if (passStatus == 1) {
            withdraw.setStatus(WithdrawStatusEnum.WAIT_LAST_CHECK.getStatus());
            description.append("设置为'初审审核通过'</br>");
        } else if (passStatus == -1) {
            withdraw.setStatus(WithdrawStatusEnum.FIRST_CHECK_FAIL.getStatus());
            description.append("设置为'初审审核不通过'</br>");
        } else {
            return withdrawId+"错误的审核状态</br>";
        }
        try {
            withdrawService.updateAndInsertLog(withdraw, description.toString());
            return description.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return withdrawId+"审核设置失败</br>";
        }
    }

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 终审
     *
     * @param passStatus 是否通过 1通过 -1不通过
     * @return
     */
    @PostMapping(value = "/withdraw/lastCheck/{passStatus}",produces="application/json;charset=UTF-8")
    public ResultResponse lastWithdrawCheck(@RequestBody JSONObject jsonObject,@PathVariable("passStatus") byte passStatus,
                                            @RequestParam(name = "remark", required = false) String remark) {
        Map<String, String> map = JSONObject.toJavaObject(jsonObject, Map.class);
        List<String> list = new ArrayList<>();
        if(map.get("withdrawIdList").contains(",")){
            String withdrawIdList = map.get("withdrawIdList");
            list = Arrays.asList(withdrawIdList.substring(1,withdrawIdList.length()-1).split(","));
        }else {
            String withdrawIdList = map.get("withdrawIdList");
            list.add(withdrawIdList.substring(1,withdrawIdList.length()-1));
        }
        StringBuffer result = new StringBuffer();
        list.forEach(item->result.append(handleLastCheck(Integer.valueOf(item),passStatus,remark)));
        return new ResultResponse(result.toString().contains("成功"), result.toString());
    }

    public String handleLastCheck(Integer withdrawId,byte passStatus, String remark){
        String withdrawLastcheck = redisUtil.getString(RedisKeyEnum.WITHDRAW_LASTCHECK.getKey() + withdrawId);

        if (StringUtils.isNotEmpty(withdrawLastcheck)){
            return "转出id:"+withdrawId+",正在审核..稍后再试";
        }
        redisUtil.setString(RedisKeyEnum.WITHDRAW_LASTCHECK.getKey() + withdrawId,"1",10);

        Withdraw withdraw = withdrawService.selectById(withdrawId);

        if (withdraw == null) {
            return withdrawId+"不存在的转出记录";
        }

        Byte status = withdraw.getStatus();
        if (StringUtils.isNoneEmpty(remark)) {
            withdraw.setRemark(remark);
        }

        if (status != WithdrawStatusEnum.WAIT_LAST_CHECK.getStatus()) {
            //状态不能终审
            return withdrawId+"此状态不能终审";
        }

        StringBuilder description = new StringBuilder();

        if (passStatus == 1) {
            CoinWithBLOBs coinWithBLOBs = coinService.selectById(withdraw.getCoinId());

            BigDecimal maxOutQty = coinWithBLOBs.getMaxOutQty();
            BigDecimal minOutQty = coinWithBLOBs.getMinOutQty();

            BigDecimal qty = withdraw.getQty();
            if (qty.compareTo(maxOutQty) > 0 || qty.compareTo(minOutQty) < 0) {
                return withdrawId+"提币金额超出设置范围";
            }

            ResultResponse checkCoinTypeAndGetBalance = checkCoinTypeAndGetBalance(withdraw, coinWithBLOBs);

            if (checkCoinTypeAndGetBalance != null) {
                return withdrawId+checkCoinTypeAndGetBalance.getMsg();
            }


            withdraw.setStatus(WithdrawStatusEnum.IN_TXPOOL_STATUS.getStatus());

            description.append("设置为'终审审核通过'");


            Integer type = coinWithBLOBs.getType();
            /**
             *
             * 请求对应币种钱包接口 转账
             */
            ResultResponse resultResponse = checkCoinTypeAndTransfer(withdraw, type);
            if (resultResponse != null) return withdrawId+resultResponse.getMsg();

        } else if (passStatus == -1) {
            withdraw.setStatus(WithdrawStatusEnum.LAST_CHECK_FAIL.getStatus());
            description.append("设置为'终审审核不通过'");
        } else {
            return withdrawId+"错误的审核状态";
        }
        try {
            int update = withdrawService.updateAndInsertLog(withdraw, description.toString());
            return withdrawId+"审核设置成功";
        } catch (Exception e) {
            e.printStackTrace();
            return withdrawId+"审核设置失败";
        }

    }

    /**
     * 请求对应币种钱包接口 转账
     */
    private ResultResponse checkCoinTypeAndTransfer(Withdraw withdraw, Integer type) {
        ResultResponse resultResponse = null;
        //TODO 根据不同coin编码
        BigDecimal qty = withdraw.getQty().subtract(withdraw.getFee());
        if (CoinTypeEnum.BTC.getType() == type) {
            resultResponse = btcApiClient.transferBtc(withdraw.getToAddress(), qty);
        } else if (CoinTypeEnum.ETH.getType() == type) {
            resultResponse = ethApiClient.transferEth(withdraw.getToAddress(), qty);
        } else if (CoinTypeEnum.ETH_TOKEN.getType() == type) {
            resultResponse = ethApiClient.transferToken(withdraw.getToAddress(), qty, withdraw.getCoinId());
        } else if (CoinTypeEnum.USDT.getType() == type) {
            resultResponse = btcApiClient.transferOmniToken(withdraw.getToAddress(), qty, withdraw.getCoinId());
        }

        if (resultResponse.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(resultResponse.getData()));
            String txHashId = data.getString("txHashId");

            Integer gas = data.getInteger("gas");
            Integer gasPrice = data.getInteger("gasPrice");

            withdraw.setGasLimit(gas);
            withdraw.setGasPrice(gasPrice);
            withdraw.setTxId(txHashId);
        } else {
            return resultResponse;
        }
        return null;
    }

    /**
     * 检查余额是否充足
     */
    private ResultResponse checkCoinTypeAndGetBalance(Withdraw withdraw, CoinWithBLOBs coinWithBLOBs) {
        Integer type = coinWithBLOBs.getType();
        ResultResponse resultResponse = null;
        //TODO 根据不同coin编码
        if (CoinTypeEnum.BTC.getType() == type) {
            resultResponse = btcApiClient.getAddressbalance();
        } else if (CoinTypeEnum.ETH.getType() == type) {
            resultResponse = ethApiClient.getEthBalance(coinWithBLOBs.getMainAddress());
        } else if (CoinTypeEnum.ETH_TOKEN.getType() == type) {
            resultResponse = ethApiClient.getTokenBalance(coinWithBLOBs.getContractAddress(), coinWithBLOBs.getMainAddress());
        } else if (CoinTypeEnum.USDT.getType() == type) {
            resultResponse = btcApiClient.getBalance(coinWithBLOBs.getMainAddress(), coinWithBLOBs.getPropertyid());
        }

        if (resultResponse.isSuccess()) {
            JSONObject data = JSON.parseObject(JSON.toJSONString(resultResponse.getData()));

            BigDecimal qty = withdraw.getActualQty();

            BigDecimal balance = data.getBigDecimal("balance");

            BigDecimal subtract = balance.subtract(qty);

            if (subtract.compareTo(new BigDecimal("0")) < 0) {
                return new ResultResponse(false, "币种:" + coinWithBLOBs.getShortName() + "余额:{" + balance + "}");
            }
        } else {
            return resultResponse;
        }
        return null;
    }


    @GetMapping("/{withdrawId}")
    public ResultResponse getWithdrawById(@PathVariable(name = "withdrawId") int withdrawId) {

        Withdraw withdraw = withdrawService.selectById(withdrawId);

        if (withdraw == null) {
            return new ResultResponse(false, "传入id有误");
        }


        return new ResultResponse(true, withdraw);
    }

    @GetMapping("/withdrawLog/{withdrawId}")
    public ResultResponse getWithdrawLogByWithdrawId(@PathVariable(name = "withdrawId") int withdrawId) {
        List<WithdrawLog> withdrawLogList = withdrawLogService.selectByWithdrawById(withdrawId);
        return new ResultResponse(true, withdrawLogList);
    }
}
