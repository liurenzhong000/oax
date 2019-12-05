package com.oax.admin.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.oax.admin.service.UserCoinDetailService;
import com.oax.entity.enums.UserCoinDetailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.enums.OperationLogEnum;
import com.oax.admin.enums.RechargeTypeEnum;
import com.oax.admin.exception.MyException;
import com.oax.admin.service.IRechargeService;
import com.oax.admin.service.OperationLogService;
import com.oax.admin.util.UserUtils;
import com.oax.entity.admin.OperationLog;
import com.oax.entity.admin.param.RechargeParam;
import com.oax.entity.admin.param.RechargesOrWithdrawParam;
import com.oax.entity.admin.vo.RechargePageVo;
import com.oax.entity.admin.vo.RechargesVo;
import com.oax.entity.front.Member;
import com.oax.entity.front.Recharge;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.MemberMapper;
import com.oax.mapper.front.RechargeMapper;
import com.oax.mapper.front.UserCoinMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RechargeServiceImpl implements IRechargeService {
    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Autowired
    private OperationLogService operationLogService;


    @Override
    public PageInfo<RechargesVo> getByUserIdQueryRecharge(RechargesOrWithdrawParam rechargesOrWithdrawParam) {
        PageHelper.startPage(rechargesOrWithdrawParam.getPageNum(), rechargesOrWithdrawParam.getPageSize());
        List<RechargesVo> list = rechargeMapper.getByUserIdQueryRecharge(rechargesOrWithdrawParam);

        OperationLog operationLog = new OperationLog();

        operationLog.setAdminId(UserUtils.getShiroUser().getId());
        operationLog.setUserId(rechargesOrWithdrawParam.getUserId());
        operationLog.setType(OperationLogEnum.USER_DETAIL_RECHARGE.getType());
        operationLogService.operationLog(operationLog);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<RechargePageVo> getByRechargeParam(RechargeParam rechargeParam) {
        PageHelper.startPage(rechargeParam.getPageNum(), rechargeParam.getPageSize());
        List<RechargePageVo> recharges = rechargeMapper.selectByRechargeParam(rechargeParam);


        OperationLog operationLog = new OperationLog();

        operationLog.setAdminId(UserUtils.getShiroUser().getId());
        operationLog.setType(OperationLogEnum.RECHARGE.getType());
         operationLogService.operationLog(operationLog);
        return new PageInfo<>(recharges);
    }

    @Override
    public BigDecimal countTotalQtyByCoinId(int coinId) {
        return rechargeMapper.countTotalQtyByCoinId(coinId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int employeeWelfare(Map o) {



        JSONObject jsonObject = new JSONObject(o);

        Integer coinId = jsonObject.getInteger("coinId");
        Integer userid = jsonObject.getInteger("用户id");
        BigDecimal qty = jsonObject.getBigDecimal("发币数");
        String remark = jsonObject.getString("remark");
        Member member = memberMapper.selectByPrimaryKey(userid);
        if (member == null) {
            log.error("用户不存在:{}",userid);
            throw new MyException("不存在用户id:" + userid);
        }


        Recharge recharge = new Recharge();

        recharge.setQty(qty);
        recharge.setUserId(userid);
        recharge.setType(RechargeTypeEnum.WELFARES.getType());
        recharge.setCoinId(coinId);
        recharge.setRemark(remark);

        rechargeMapper.insertSelective(recharge);

        UserCoin userCoin = userCoinMapper.selectByUserIdAndCoinId(userid, coinId);
        if (userCoin==null){
            userCoin = new UserCoin();
            userCoin.setBanlance(qty);
            userCoin.setCoinId(coinId);
            userCoin.setUserId(userid);
            int i = userCoinMapper.insertSelective(userCoin);
            return i;
        }
        int i=userCoinMapper.addBanlance(qty,coinId,userid,userCoin.getVersion());
        userCoinDetailService.addUserCoinDetail(userCoin,recharge.getId()+"",UserCoinDetailType.RECHARGE);
        log.info("添加成功:{}",userid);
        return i;
    }
}
