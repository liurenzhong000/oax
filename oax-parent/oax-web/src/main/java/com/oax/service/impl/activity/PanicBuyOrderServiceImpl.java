package com.oax.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.MD5;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.PanicBuyActivity;
import com.oax.entity.activity.PanicBuyDetail;
import com.oax.entity.activity.PanicBuyOrder;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.BonusInfo;
import com.oax.entity.front.Member;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinFreezingDetail;
import com.oax.mapper.activity.PanicBuyActivityMapper;
import com.oax.mapper.activity.PanicBuyDetailMapper;
import com.oax.mapper.activity.PanicBuyHelpMapper;
import com.oax.mapper.activity.PanicBuyOrderMapper;
import com.oax.mapper.front.*;
import com.oax.service.CommonCheckService;
import com.oax.service.UserCoinDetailService;
import com.oax.service.UserCoinService;
import com.oax.service.activity.PanicBuyOrderService;
import com.oax.vo.PanicBuyOrderPostVo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(isolation= Isolation.REPEATABLE_READ)
public class PanicBuyOrderServiceImpl implements PanicBuyOrderService {

    @Autowired
    private PanicBuyOrderMapper panicBuyOrderMapper;

    @Autowired
    private PanicBuyDetailMapper panicBuyDetailMapper;

    @Autowired
    private PanicBuyHelpMapper panicBuyHelpMapper;

    @Autowired
    private PanicBuyActivityMapper panicBuyActivityMapper;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private CommonCheckService commonCheckService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Autowired
    private BonusInfoMapper bonusInfoMapper;

    @Autowired
    private UserCoinFreezingDetailMapper userCoinFreezingDetailMapper;

    private static final Integer usdtCoinId = 10;

    private static final Integer bhbCoinId = 54;

    /**购买页数据，判断用户是否拥有购买资格，进入购买页生成订单数据*/
    @Override
    @DataSource(DataSourceType.MASTER)
    public PanicBuyOrderPostVo buyPageData(Integer userId, Integer activityId) {
        PanicBuyActivity activity = panicBuyActivityMapper.selectById(activityId);
        AssertHelper.notEmpty(activity, "获取活动信息失败");

        PanicBuyDetail detail = panicBuyDetailMapper.selectByUserIdAndActivityId(userId, activityId);
        AssertHelper.notEmpty(detail, "您还未参加活动");
        AssertHelper.isTrue(detail.getStatus() == PanicBuyDetail.Status.SUCCESS.ordinal(), "您未达标，请继续努力");
        AssertHelper.isTrue(detail.getValidTime().getTime() > System.currentTimeMillis(), "购买资格已失效");

        //获取最新BHB/USDT 成交数据
        BigDecimal tratio = getBHBUSDTratio();
        //用户usdt余额
        BigDecimal usdtBanlance = userCoinMapper.getBanlanceByCoinIdAndUserId(userId, usdtCoinId);
        Member member = memberMapper.selectByPrimaryKey(userId);

        PanicBuyOrderPostVo panicBuyOrderPostVo = new PanicBuyOrderPostVo();
        panicBuyOrderPostVo.setActivityId(activityId);
        panicBuyOrderPostVo.setUserId(userId);
        panicBuyOrderPostVo.setQty(detail.getQty());
        panicBuyOrderPostVo.setCurrPrice(tratio);
        panicBuyOrderPostVo.setBuyPrice(tratio.multiply(BigDecimal.valueOf(0.77)).setScale(4, BigDecimal.ROUND_HALF_UP));
        panicBuyOrderPostVo.setAllUsdtQty(tratio.multiply(detail.getQty()).setScale(4, BigDecimal.ROUND_HALF_UP));
        panicBuyOrderPostVo.setRealUsdtQty(panicBuyOrderPostVo.getBuyPrice().multiply(detail.getQty()).setScale(4, BigDecimal.ROUND_HALF_UP));
        panicBuyOrderPostVo.setBanlance(usdtBanlance);
        panicBuyOrderPostVo.setHasTransactionPassword(member.getNeedTransactionPassword().equals(1));//是否启用交易密码

        PanicBuyOrder oldOrder = panicBuyOrderMapper.selectByUserIdAndActivityId(userId, activityId);
        //判断是否有已完成或失效的订单
        AssertHelper.isTrue(oldOrder == null || oldOrder.getStatus() == PanicBuyOrder.Status.NO_PAY.ordinal(), "本次活动您已购买成功或已失效");

        //新增订单数据
        PanicBuyOrder newOrder = BeanHepler.copy(panicBuyOrderPostVo, PanicBuyOrder.class);
        newOrder.setStatus(PanicBuyOrder.Status.NO_PAY.ordinal());
        newOrder.setCreateTime(new Date());
        if (oldOrder == null) {
            panicBuyOrderMapper.insert(newOrder);
        } else {
            newOrder.setId(oldOrder.getId());
            panicBuyOrderMapper.updateById(newOrder);
        }
        panicBuyOrderPostVo.setOrderId(newOrder.getId());//返回订单id
        return panicBuyOrderPostVo;
    }

    //BHB个数按比例转化为usdt
    public BigDecimal getBHBUSDTratio() {
        BigDecimal tratio =  tradeMapper.getRatioByLeftIdAndRightId(bhbCoinId, usdtCoinId);
        if (tratio == null || tratio.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }
        return tratio;
    }

    //提交订单，进行购买，减去用户usdt余额
    @Override
    @DataSource(DataSourceType.MASTER)
    public PanicBuyOrderPostVo buy(Integer userId, Integer orderId, String transactionPassword) {
        //获取生成的订单详情
        PanicBuyOrder order = panicBuyOrderMapper.selectById(orderId);
        AssertHelper.notEmpty(order, "订单不存在");
        AssertHelper.isTrue(order.getUserId().equals(userId),"订单不存在");
        AssertHelper.isTrue(order.getStatus() == PanicBuyOrder.Status.NO_PAY.ordinal(), "订单状态异常");
        order.setStatus(PanicBuyOrder.Status.FINISH.ordinal());
        order.setFinishTime(new Date());
        panicBuyOrderMapper.updateById(order);

        //activity buyCount添加
        PanicBuyActivity activity = panicBuyActivityMapper.selectById(order.getActivityId());
        activity.setBuyCount(activity.getBuyCount() + 1);
        panicBuyActivityMapper.updateById(activity);

        //获取用户信息
        Member member = memberMapper.selectByPrimaryKey(userId);
        if (member.getNeedTransactionPassword().equals(1)) {
            AssertHelper.notEmpty(transactionPassword, "交易密码不能为空");
            String transactionPasswordMD5 = MD5.encrypt(transactionPassword);
            AssertHelper.isTrue(transactionPasswordMD5.equals(member.getTransactionPassword()), "交易密码错误");
        }
        //更改订单状态，更新账户余额，交易成功
        //验证用户usdt可用余额
        UserCoin oldUsdtUserCoin = userCoinService.selectAndInsert(userId, usdtCoinId);
        UserCoin oldBhbUserCoin = userCoinService.selectAndInsert(userId, bhbCoinId);
        AssertHelper.isTrue(oldUsdtUserCoin.getBanlance().compareTo(order.getRealUsdtQty())>=0,"USDT余额不足");
        //商户资金是否冻结
        AssertHelper.isTrue(!commonCheckService.hasFreezing(member.getId()), "资金冻结，无法交易");

        //添加BHB余额, 减去USDT
        int subtractBanlanceSucc = userCoinMapper.addBanlance(order.getRealUsdtQty().negate(), usdtCoinId, userId, oldUsdtUserCoin.getVersion());
        //把钱加到余额，在进行冻结
        int addFreezingSucc = userCoinMapper.addFreezing(order.getQty(), bhbCoinId, userId, oldBhbUserCoin.getVersion());

        saveUserCoinFreezingDetail(userId, order.getQty());//添加冻结记录
        saveBonusInfo(userId, order.getQty());
        AssertHelper.isTrue(addFreezingSucc >0 && subtractBanlanceSucc>0 , "系统异常，购买失败");

        //添加金额变更记录
        userCoinDetailService.addUserCoinDetail(oldUsdtUserCoin, order.getId()+"", UserCoinDetailType.PANIC_BUY_BHB);
        userCoinDetailService.addUserCoinDetail(oldBhbUserCoin, order.getId()+"", UserCoinDetailType.PANIC_BUY_BHB);
        PanicBuyOrderPostVo panicBuyOrderPostVo = BeanHepler.copy(order, PanicBuyOrderPostVo.class);
        panicBuyOrderPostVo.setOrderId(order.getId());
        return panicBuyOrderPostVo;
    }

    private void saveBonusInfo(Integer userId, BigDecimal qty) {
        BonusInfo bonusInfo = bonusInfoMapper.selectById(userId);
        if (bonusInfo == null) {
            BonusInfo entity = new BonusInfo();
            entity.setUserId(userId);
            entity.setBanlace(qty);
            bonusInfoMapper.insert(entity);
        }
        bonusInfoMapper.addBanlance(userId, qty);
    }

    private void saveUserCoinFreezingDetail(Integer userId, BigDecimal qty){
        UserCoinFreezingDetail freezingDetail = new UserCoinFreezingDetail();
        freezingDetail.setCoinId(54);//BHB 的coinId
        freezingDetail.setCreateTime(new Date());
        freezingDetail.setUpdateTime(new Date());
        freezingDetail.setUnfreezeTime(new Date());
        freezingDetail.setUserId(userId);
        freezingDetail.setFreezingTime(new Date());
        freezingDetail.setUnfreezeTime(DateUtils.addMonths(new Date(), 3));//3个月后解冻
        freezingDetail.setQty(qty);
        freezingDetail.setStatus(UserCoinFreezingDetail.Status.FREEZING.ordinal());
        userCoinFreezingDetailMapper.insert(freezingDetail);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public PageInfo<PanicBuyOrder> page(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PanicBuyOrder> panicBuyOrders = panicBuyOrderMapper.selectForWeb(userId);
        return new PageInfo<>(panicBuyOrders);
    }
}