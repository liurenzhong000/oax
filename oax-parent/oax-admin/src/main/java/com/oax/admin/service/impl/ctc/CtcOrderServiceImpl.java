package com.oax.admin.service.impl.ctc;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.UserCoinDetailService;
import com.oax.admin.service.UserCoinService;
import com.oax.admin.service.ctc.CtcOrderService;
import com.oax.admin.util.CtcOrderStatusDescUtil;
import com.oax.common.AssertHelper;
import com.oax.entity.admin.param.CtcOrderAdminParam;
import com.oax.entity.admin.param.CtcOrderParam;
import com.oax.entity.admin.vo.CtcOrderVo;
import com.oax.entity.ctc.*;
import com.oax.entity.enums.*;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.vo.CtcOrderAdminVo;
import com.oax.mapper.ctc.*;
import com.oax.mapper.front.UserCoinMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(isolation= Isolation.REPEATABLE_READ)
public class CtcOrderServiceImpl implements CtcOrderService {

    @Autowired
    private CtcOrderMapper ctcOrderMapper;

    @Autowired
    private CtcAppealMapper ctcAppealMapper;

    @Autowired
    private CtcOrderLogMapper ctcOrderLogMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private CtcAdvertMapper ctcAdvertMapper;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Override
    public PageInfo<CtcOrderVo> pageForBuy(CtcOrderParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<CtcOrderVo> ctcOrderVos = ctcOrderMapper.pageForBuy(param);
        handleCtcOrderVos(ctcOrderVos);
        return new PageInfo<>(ctcOrderVos);
    }

    @Override
    public PageInfo<CtcOrderVo> pageForSale(CtcOrderParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<CtcOrderVo> ctcOrderVos = ctcOrderMapper.pageForSale(param);
        handleCtcOrderVos(ctcOrderVos);
        return new PageInfo<>(ctcOrderVos);
    }

    private void handleCtcOrderVos(List<CtcOrderVo> ctcOrderVos){
        ctcOrderVos.forEach( item ->{
            String desc = CtcOrderStatusDescUtil.getStatusDesc(item.getStatus(), item.getType(), true);
            item.setStatusDesc(desc);
            item.setTypeDesc(item.getType().descForUser);
            item.setTradeTypeDesc("网银支付");
        });
    }

    //后台点击已处理，订单状态从申诉中->处理中，申诉记录的状态：申诉中->处理中
    @Override
    public synchronized void processing(Long id) {
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        //当状态属于申诉中，才可以进行已处理操作
        AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.APPEAL, "当前状态无法操作");

        //更新申诉记录的状态
        CtcAppeal ctcAppeal = ctcAppealMapper.getCanCancelByOrderId(id);
        AssertHelper.notEmpty(ctcAppeal, "申诉记录不存在");
        AssertHelper.isTrue(ctcAppeal.getStatus() == CtcAppealStatus.APPEAL, "申诉记录状态异常");
        ctcAppeal.setStatus(CtcAppealStatus.DEALING);
        ctcAppealMapper.updateById(ctcAppeal);
        //订单状态变为处理中
        ctcOrder.setStatus(CtcOrderStatus.PROCESSING);
        ctcOrderMapper.updateById(ctcOrder);
        //添加订单状态变更记录
        CtcOrderLog ctcOrderLog = CtcOrderLog.newInstance(ctcOrder.getId(), CtcOrderStatus.APPEAL, CtcOrderStatus.PROCESSING, CtcOrderLogType.PROCESSING, null);
        ctcOrderLogMapper.insert(ctcOrderLog);
        log.info("CTC - 后台点击已处理，orderId:{}", id);
    }

    //当平台确认付款方已转账，但放币方没有放币，点击确认到账，完成放币
    @Override
    public synchronized void finish(Long id) {
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PROCESSING, "当前订单状态无法进行放币操作");

        //更新申诉记录的状态
        CtcAppeal ctcAppeal = ctcAppealMapper.getCanCancelByOrderId(id);
        AssertHelper.notEmpty(ctcAppeal, "申诉记录不存在");
        AssertHelper.isTrue(ctcAppeal.getStatus() == CtcAppealStatus.DEALING, "申诉记录状态异常");
        ctcAppeal.setStatus(CtcAppealStatus.FINISH);
        ctcAppealMapper.updateById(ctcAppeal);

        ctcOrder.setStatus(CtcOrderStatus.FINISH);
        ctcOrder.setFinishDate(new Date());
        ctcOrderMapper.updateById(ctcOrder);
        BigDecimal qty = ctcOrder.getQty();
        //先判断付款方是否有user_coin表记录, 获取交易双方的交易前的资金记录
        UserCoin oldFromUserCoin = userCoinService.selectAndInsert(ctcOrder.getFromUserId(), ctcOrder.getCoinId());
        UserCoin oldToUserCoin = userCoinService.selectAndInsert(ctcOrder.getToUserId(), ctcOrder.getCoinId());
        int subtractFreezingSucc = userCoinMapper.subtractFreezing(qty, ctcOrder.getCoinId(), ctcOrder.getFromUserId(), oldFromUserCoin.getVersion());
        int addBanlanceSucc = userCoinMapper.addBanlance(qty, ctcOrder.getCoinId(), ctcOrder.getToUserId(), oldToUserCoin.getVersion());
        AssertHelper.isTrue(subtractFreezingSucc >=1 && addBanlanceSucc >=1, "余额变更失败");
        calculateMerchant(ctcOrder);
        //添加金额变更记录
        userCoinDetailService.addUserCoinDetail(oldFromUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_FINISH);
        userCoinDetailService.addUserCoinDetail(oldToUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_FINISH);
        //添加订单状态变更记录
        CtcOrderLog ctcOrderLog = CtcOrderLog.newInstance(ctcOrder.getId(), CtcOrderStatus.PROCESSING, CtcOrderStatus.FINISH, CtcOrderLogType.PLATFORM_FINISH, null);
        ctcOrderLogMapper.insert(ctcOrderLog);
        log.info("CTC - 平台进行放币操作，orderId:{}", id);
    }

    //保存交易记录统计
    private void calculateMerchant(CtcOrder ctcOrder) {
        Merchant merchant = merchantMapper.selectById(ctcOrder.getMerchantId());
        AssertHelper.notEmpty(merchant, "广告商户不存在");
        if (ctcOrder.getType() == CtcOrderType.USER_BUY) {
            merchant.setSaleFee(merchant.getSaleFee().add(ctcOrder.getTotalCost()));
            merchant.setSaleQty(merchant.getSaleQty().add(ctcOrder.getQty()));
            merchant.setSaleSuccCount(merchant.getSaleSuccCount() + 1);
        } else if (ctcOrder.getType() == CtcOrderType.USER_SALE) {
            merchant.setBuyFee(merchant.getBuyFee().add(ctcOrder.getTotalCost()));
            merchant.setBuyQty(merchant.getBuyQty().add(ctcOrder.getQty()));
            merchant.setBuySuccCount(merchant.getBuySuccCount() + 1);
        }
        merchantMapper.updateById(merchant);
    }

    //平台判断付款方没有付款，关闭当前这笔订单，并关闭申诉记录
//    @Override
//    public synchronized void close(Long id) {
//        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
//        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
//        AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PROCESSING, "当前订单状态无法进行关闭订单操作");
//        //更新申诉记录的状态
//        CtcAppeal ctcAppeal = ctcAppealMapper.getCanCancelByOrderId(id);
//        AssertHelper.notEmpty(ctcAppeal, "申诉记录不存在");
//        AssertHelper.isTrue(ctcAppeal.getStatus() == CtcAppealStatus.DEALING, "申诉记录状态异常");
//        ctcAppeal.setStatus(CtcAppealStatus.CLOSE);
//        ctcAppealMapper.updateById(ctcAppeal);
//
//        UserCoin oldUserCoin = userCoinService.selectAndInsert(ctcOrder.getFromUserId(), ctcOrder.getCoinId());
//        //关闭订单=修改订单状态，取消冻结金额
//        ctcOrder.setStatus(CtcOrderStatus.CANCEL);
//        ctcOrderMapper.updateById(ctcOrder);
//        int cancelFreezingSucc = userCoinMapper.cancelFreezingByUserIdAndCoinId(ctcOrder.getQty(), ctcOrder.getCoinId(), ctcOrder.getFromUserId(), oldUserCoin.getVersion());
//        AssertHelper.isTrue(cancelFreezingSucc >= 1, "取消订单，解冻金额失败");
//        //添加资金变更记录
//        userCoinDetailService.addUserCoinDetail(oldUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_CLOSE);
//        //添加订单状态变更记录
//        CtcOrderLog ctcOrderLog = CtcOrderLog.newInstance(ctcOrder.getId(), CtcOrderStatus.PROCESSING, CtcOrderStatus.CANCEL, CtcOrderLogType.PLATFORM_FINISH, null);
//        ctcOrderLogMapper.insert(ctcOrderLog);
//        //广告，剩余可交易余额增加
//        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(ctcOrder.getCtcAdvertId());
//        ctcAdvert.setRemainQty(ctcAdvert.getRemainQty().add(ctcOrder.getQty()));
//        ctcAdvertMapper.updateById(ctcAdvert);
//        log.info("CTC - 平台关闭申诉，关闭交易订单，orderId:{}");
//    }

    //关闭订单，用户买入-已付款状态也可以关闭订单
    //平台判断付款方没有付款，关闭当前这笔订单，并关闭申诉记录
    @Override
    public synchronized void close(Long id) {
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        CtcOrderStatus oldStatus = ctcOrder.getStatus();
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        AssertHelper.isTrue(oldStatus == CtcOrderStatus.PROCESSING
                || (oldStatus == CtcOrderStatus.PAYED && ctcOrder.getType()==CtcOrderType.USER_BUY)
                || (oldStatus == CtcOrderStatus.NO_PAY && ctcOrder.getType()==CtcOrderType.USER_SALE), "当前订单状态无法进行关闭订单操作");
        if (ctcOrder.getStatus() == CtcOrderStatus.PROCESSING) {//申述情况下的处理
            //更新申诉记录的状态
            CtcAppeal ctcAppeal = ctcAppealMapper.getCanCancelByOrderId(id);
            AssertHelper.notEmpty(ctcAppeal, "申诉记录不存在");
            AssertHelper.isTrue(ctcAppeal.getStatus() == CtcAppealStatus.DEALING, "申诉记录状态异常");
            ctcAppeal.setStatus(CtcAppealStatus.CLOSE);
            ctcAppealMapper.updateById(ctcAppeal);
        }

        UserCoin oldUserCoin = userCoinService.selectAndInsert(ctcOrder.getFromUserId(), ctcOrder.getCoinId());
        //关闭订单=修改订单状态，取消冻结金额
        ctcOrder.setStatus(CtcOrderStatus.CANCEL);
        ctcOrderMapper.updateById(ctcOrder);
        int cancelFreezingSucc = userCoinMapper.cancelFreezingByUserIdAndCoinId(ctcOrder.getQty(), ctcOrder.getCoinId(), ctcOrder.getFromUserId(), oldUserCoin.getVersion());
        AssertHelper.isTrue(cancelFreezingSucc >= 1, "取消订单，解冻金额失败");
        //添加资金变更记录
        userCoinDetailService.addUserCoinDetail(oldUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_CLOSE);
        //添加订单状态变更记录
        CtcOrderLog ctcOrderLog = CtcOrderLog.newInstance(ctcOrder.getId(), oldStatus, CtcOrderStatus.CANCEL, CtcOrderLogType.PLATFORM_FINISH, null);
        ctcOrderLogMapper.insert(ctcOrderLog);
        //广告，剩余可交易余额增加
        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(ctcOrder.getCtcAdvertId());
        ctcAdvert.setRemainQty(ctcAdvert.getRemainQty().add(ctcOrder.getQty()));
        ctcAdvertMapper.updateById(ctcAdvert);
        log.info("CTC - 平台关闭申诉，关闭交易订单，orderId:{}", id);
    }

    //关闭申诉，返回原来的订单状态
    @Override
    public synchronized void closeAppeal(Long id) {
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PROCESSING, "当前订单状态无法进行关闭申诉操作");
        //更新申诉记录的状态
        CtcAppeal ctcAppeal = ctcAppealMapper.getCanCancelByOrderId(id);
        AssertHelper.notEmpty(ctcAppeal, "申诉记录不存在");
        AssertHelper.isTrue(ctcAppeal.getStatus() == CtcAppealStatus.DEALING, "申诉记录状态异常");
        ctcAppeal.setStatus(CtcAppealStatus.CLOSE_APPEAL);
        ctcAppealMapper.updateById(ctcAppeal);

        //关闭申诉=修改订单状态回申诉前的状态
        CtcOrderStatus beforeAppealStatus = ctcOrderLogMapper.getBeforeAppealStatus(ctcOrder.getId());
        ctcOrder.setStatus(beforeAppealStatus);
        ctcOrderMapper.updateById(ctcOrder);
        //添加订单状态变更记录
        CtcOrderLog ctcOrderLog = CtcOrderLog.newInstance(ctcOrder.getId(), CtcOrderStatus.PROCESSING, beforeAppealStatus, CtcOrderLogType.CLOSE_APPEAL, null);
        ctcOrderLogMapper.insert(ctcOrderLog);
        log.info("CTC - 平台关闭申诉，orderId:{}", id);
    }

    @Override
    public PageInfo<CtcOrderAdminVo> listFinishByUserId(CtcOrderAdminParam param){
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<CtcOrderAdminVo> adminVos = ctcOrderMapper.listFinishByUserId(param.getUserId());
        adminVos.forEach(item-> item.setTypeDesc(item.getType().descForUser));
        return new PageInfo<>(adminVos);
    }
}
