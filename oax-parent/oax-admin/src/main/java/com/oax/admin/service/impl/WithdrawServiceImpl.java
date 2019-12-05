package com.oax.admin.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.oax.admin.service.UserCoinDetailService;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoinDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.IWithdrawService;
import com.oax.admin.service.UserCoinService;
import com.oax.admin.service.WithdrawLogService;
import com.oax.admin.util.UserUtils;
import com.oax.common.enums.WithdrawStatusEnum;
import com.oax.entity.admin.User;
import com.oax.entity.admin.WithdrawLog;
import com.oax.entity.admin.param.RechargesOrWithdrawParam;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.WithdrawPageParam;
import com.oax.entity.admin.vo.WithdrawFeeVo;
import com.oax.entity.admin.vo.WithdrawPageVo;
import com.oax.entity.admin.vo.WithdrawSums;
import com.oax.entity.admin.vo.WithdrawsVo;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.Withdraw;
import com.oax.mapper.front.WithdrawMapper;

@Service
public class WithdrawServiceImpl implements IWithdrawService {
    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private WithdrawLogService withdrawLogService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Override
    public PageInfo<WithdrawsVo> getByUserIdQueryWithdraw(RechargesOrWithdrawParam withdrawsParam) {
        PageHelper.startPage(withdrawsParam.getPageNum(), withdrawsParam.getPageSize());
        List<WithdrawsVo> list = withdrawMapper.getByUserIdQueryWithdraw(withdrawsParam);
        return new PageInfo<>(list);
    }

    @Override
    public WithdrawSums countWithdrawSumsByParam(WithdrawPageParam withdrawPageParam) {
        return withdrawMapper.countWithdrawSumsByParam(withdrawPageParam);
    }

    @Override
    public PageInfo<WithdrawPageVo> getByWithdrawPageParam(WithdrawPageParam withdrawPageParam) {
        PageHelper.startPage(withdrawPageParam.getPageNum(), withdrawPageParam.getPageSize());
        List<WithdrawPageVo> withdrawList = withdrawMapper.getByWithdrawPageParam(withdrawPageParam);
        return new PageInfo<>(withdrawList);
    }

    @Override
    public Withdraw selectById(int withdrawId) {
        return withdrawMapper.selectByPrimaryKey(withdrawId);
    }

    @Override
    public int update(Withdraw withdraw) {
        return withdrawMapper.updateByPrimaryKeySelective(withdraw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAndInsertLog(Withdraw withdraw, String description) {

        withdraw.setUpdateTime(null);
        withdrawMapper.updateByPrimaryKeySelective(withdraw);
        if (withdraw.getStatus() == WithdrawStatusEnum.FIRST_CHECK_FAIL.getStatus() ||
                withdraw.getStatus() == WithdrawStatusEnum.LAST_CHECK_FAIL.getStatus()) {
            UserCoin userCoin = userCoinService.selectByUserIdAndCoinId(withdraw.getUserId(), withdraw.getCoinId());
            UserCoin oldUserCoin = (UserCoin) BeanHepler.clone(userCoin);
            //审核不通过 恢复币
            BigDecimal qty = withdraw.getQty();
            userCoin.setBanlance(userCoin.getBanlance().add(qty));
            //TODO user_coin锁
            userCoinService.update(userCoin);
            userCoinDetailService.addUserCoinDetail(oldUserCoin, withdraw.getId()+"", UserCoinDetailType.WITHDRAW_FAIL);
        }


        User shiroUser = UserUtils.getShiroUser();
        WithdrawLog withdrawLog = new WithdrawLog();
        withdrawLog.setAdminId(shiroUser.getId());
        withdrawLog.setAdminName(shiroUser.getName());
        withdrawLog.setDescription(description);
        withdrawLog.setRemark(withdraw.getRemark());
        withdrawLog.setWithdrawId(withdraw.getId());
        int insert = withdrawLogService.insert(withdrawLog);
        return insert;
    }

    @Override
    public Withdraw selectByHash(String hash) {
        return withdrawMapper.selectByHash(hash);
    }

    @Override
    public PageInfo<WithdrawFeeVo> sumWithdrawFee(SimpleCoinParam simpleCoinParam) {
        PageHelper.startPage(simpleCoinParam.getPageNum(),simpleCoinParam.getPageSize());
         List<WithdrawFeeVo> withdrawFeeVoList =  withdrawMapper.sumWithdrawFee(simpleCoinParam);

         return new PageInfo<>(withdrawFeeVoList);
    }

    //拉黑提现和解除拉黑
    @Override
    public void blockWithdraw(List<Integer> withdrawIds, byte passStatus, String remark) {
        if (passStatus == WithdrawStatusEnum.BLOCK.getStatus()){//拉黑
            handleBlock(withdrawIds, remark);
        } else if (passStatus == WithdrawStatusEnum.WAIT_FIRST_CHECK.getStatus()){//解除拉黑回到待初审状态
            handleUnBlock(withdrawIds, remark);
        }
    }

    private void handleBlock(List<Integer> withdrawIds, String remark){
        withdrawIds.forEach(withdrawId -> {
            Withdraw withdraw = withdrawMapper.selectByPrimaryKey(withdrawId);
            AssertHelper.notEmpty(withdraw, "提现记录不存在，id："+withdrawId);
            AssertHelper.isTrue(withdraw.getStatus() == WithdrawStatusEnum.WAIT_FIRST_CHECK.getStatus()
                || withdraw.getStatus() ==WithdrawStatusEnum.WAIT_LAST_CHECK.getStatus(), "该记录不属于待初审或待终审状态，id："+withdrawId);
            //加入冻结
            withdraw.setStatus(WithdrawStatusEnum.BLOCK.getStatus());
            updateAndInsertLog(withdraw, "设置为'提现审核冻结' "+ remark);
        });
    }

    private void handleUnBlock(List<Integer> withdrawIds, String remark){
        withdrawIds.forEach(withdrawId -> {
            Withdraw withdraw = withdrawMapper.selectByPrimaryKey(withdrawId);
            AssertHelper.notEmpty(withdraw, "提现记录不存在，id："+withdrawId);
            AssertHelper.isTrue(withdraw.getStatus() == WithdrawStatusEnum.BLOCK.getStatus(), "该记录不属于拉黑状态，id："+withdrawId);
            //变回待初审状态
            withdraw.setStatus(WithdrawStatusEnum.WAIT_FIRST_CHECK.getStatus());
            updateAndInsertLog(withdraw, "设置为'提现待初审状态' "+ remark);
        });
    }


}
