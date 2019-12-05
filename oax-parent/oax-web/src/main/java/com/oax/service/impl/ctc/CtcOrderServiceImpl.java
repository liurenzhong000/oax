package com.oax.service.impl.ctc;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.*;
import com.oax.context.HttpContext;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.ctc.*;
import com.oax.entity.enums.*;
import com.oax.entity.front.Member;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.vo.CtcBuyVo;
import com.oax.entity.front.vo.CtcOrderDetailVo;
import com.oax.entity.front.vo.CtcOrderForMerchantVo;
import com.oax.entity.front.vo.CtcOrderForUserVo;
import com.oax.exception.VoException;
import com.oax.mapper.ctc.*;
import com.oax.mapper.front.MemberMapper;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.*;
import com.oax.service.ctc.CtcOrderLogService;
import com.oax.service.ctc.CtcOrderService;
import com.oax.service.delay.DelayQueueDesc;
import com.oax.service.delay.DelayQueueService;
import com.oax.service.delay.DelayQueueType;
import com.oax.util.CtcOrderIdFactory;
import com.oax.util.CtcOrderStatusDescUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(isolation= Isolation.REPEATABLE_READ)
@Slf4j
public class CtcOrderServiceImpl implements CtcOrderService {

    @Autowired
    private CtcAdvertMapper ctcAdvertMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private CtcOrderMapper ctcOrderMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private CtcAppealMapper ctcAppealMapper;

    @Autowired
    private CtcOrderLogMapper ctcOrderLogMapper;

    @Autowired
    private CommonCheckService commonCheckService;

    @Autowired
    private CtcOrderLogService ctcOrderLogService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private EmailCaptchaService emailCaptchaService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private CtcOrderIdFactory ctcOrderIdFactory;

    @Autowired
    private DelayQueueService delayQueueService;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Autowired
    private RedisUtil redisUtil;

    public static final String lastRefreshTimeKey = "c2c_last_refresh:";
//    @Autowired
//    private EmailCaptchaServiceImpl emailCaptchaServiceImpl;

    @Override
    @DataSource(DataSourceType.SLAVE)
    public synchronized CtcOrderDetailVo getDetailVoById(Long id) {
        Integer userId = HttpContext.getUserId();
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        boolean isUser = false;
        if (ctcOrder.getType() == CtcOrderType.USER_BUY) {
            isUser = ctcOrder.getToUserId().equals(userId);
        } else if (ctcOrder.getType() == CtcOrderType.USER_SALE){
            isUser = ctcOrder.getFromUserId().equals(userId);
        }
        CtcOrderDetailVo detailVo = ctcOrderMapper.getDetailVOById(id);
        detailVo.setStatusDesc(CtcOrderStatusDescUtil.getStatusDesc(detailVo.getStatus(), detailVo.getType(), isUser));
        return detailVo;
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public synchronized CtcOrderDetailVo buy(Integer ctcAdvertId, BigDecimal qty) {
        //当前操作用户
        Integer userId = HttpContext.getUserId();
        //资金是否冻结
        commonCheckService.checkFreezing(userId);
        //是否实名认证
        commonCheckService.checkHasAuthentication(userId);
        //判断用户是否存在未完成的买入订单，一次只能存在一笔未完成订单
        boolean hasOrder = ctcOrderMapper.hasNoFinishOrder(userId);
        AssertHelper.isTrue(!hasOrder, "您还存在一笔未完成交易，无法再次买入");
        //首先验证商户广告的真实性，验证广告的最大，最小量，验证广告的可购买量
        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(ctcAdvertId);
        AssertHelper.notEmpty(ctcAdvert, "C2C广告不存在");
        AssertHelper.isTrue(ctcAdvert.getStatus() == CtcAdvertStatus.PUTAWAY, "C2C广告已下架");
        AssertHelper.isTrue(ctcAdvert.getType() == CtcAdvertType.SALE, "C2C广告类型不正确");
        AssertHelper.isTrue(qty.compareTo(ctcAdvert.getMinQty()) >=0, "购买个数小于最小购买数");
        AssertHelper.isTrue(qty.compareTo(ctcAdvert.getMaxQty()) <=0, "购买个数大于最大购买数");
        AssertHelper.isTrue(!userId.equals(ctcAdvert.getUserId()), "商户禁止购买自己的广告");
        //验证商户可用余额
        BigDecimal banlance = userCoinMapper.getBanlanceByCoinIdAndUserId(ctcAdvert.getUserId(), ctcAdvert.getCoinId());
        Member merchantUser = memberMapper.selectByPrimaryKey(ctcAdvert.getUserId());
        //商户资金是否冻结
        AssertHelper.isTrue(!commonCheckService.hasFreezing(merchantUser.getId()), "商户资金冻结，无法交易");
        if (banlance.compareTo(qty) < 0){
            sendSmsAndEmail(merchantUser, "法币交易，您的账户余额不足，用户可能无法正常买入，请充值");
        }
        AssertHelper.isTrue(banlance.compareTo(qty) >= 0, "商户余额不足，当前还可以购买"+banlance.setScale(8, BigDecimal.ROUND_HALF_UP)+"个");

        AssertHelper.isTrue(qty.compareTo(ctcAdvert.getRemainQty()) <=0, "广告剩余可交易余额不足，当前还可以购买"+ctcAdvert.getRemainQty().setScale(8, BigDecimal.ROUND_HALF_UP)+"个");
        if (qty.compareTo(ctcAdvert.getRemainQty().subtract(new BigDecimal(1000))) > 0) {
            // 广告剩余可交易余额不足时，短信或邮箱通知商户
            sendSmsAndEmail(merchantUser, "法币交易，您上架的卖出广告可交易余额不足1000");
        }

        if (ctcAdvert.getRemainQty().subtract(qty).compareTo(ctcAdvert.getMinQty()) < 0) {
            // 用户交易后如果商户的可交易余额或余额不足下限时，短信或邮箱通知商户
            sendSmsAndEmail(merchantUser, "法币交易，您上架的卖出广告可交易余额不足或余额不足广告下线，系统将切换为其他商家");
        }


        //下单之前用户资金信息
        UserCoin oldUserCoin = userCoinService.selectAndInsert(ctcAdvert.getUserId(), ctcAdvert.getCoinId());
        BankCard bankCard = bankCardMapper.getDefaultCard(ctcAdvert.getUserId());//商户的银行卡
        AssertHelper.notEmpty(bankCard, "获取商户默认银行卡失败");
        //用户下单，提交订单
        CtcOrder ctcOrder = new CtcOrder();
        ctcOrder.setId(ctcOrderIdFactory.generateOrderId(ctcAdvert.getMerchantId()));
        ctcOrder.setFromUserId(ctcAdvert.getUserId());//从商家id到用户id
        ctcOrder.setToUserId(userId);
        ctcOrder.setCoinId(ctcAdvert.getCoinId());
        ctcOrder.setPrice(ctcAdvert.getCnyPrice());
        ctcOrder.setQty(qty);
        BigDecimal totalCost = ctcAdvert.getCnyPrice().multiply(qty);
        ctcOrder.setTotalCost(totalCost);
        ctcOrder.setCreateDate(new Date());
        ctcOrder.setStatus(CtcOrderStatus.NO_PAY);
        ctcOrder.setType(CtcOrderType.USER_BUY);
        ctcOrder.setMerchantId(ctcAdvert.getMerchantId());
        ctcOrder.setCtcAdvertId(ctcAdvertId);

        ctcOrder.setBankName(bankCard.getBankName());
        ctcOrder.setBankBranch(bankCard.getBankBranch());
        ctcOrder.setCardNo(bankCard.getCardNo());
        ctcOrder.setCity(bankCard.getCity());
        ctcOrder.setRealName(bankCard.getRealName());
        ctcOrderMapper.insert(ctcOrder);

        //提交订单后，冻结商家对应额度的金额
        int freezingSucc = userCoinMapper.freezingByUserIdAndCoinId(ctcOrder.getQty(), ctcOrder.getCoinId(), ctcOrder.getFromUserId(), oldUserCoin.getVersion());
        AssertHelper.isTrue(freezingSucc >= 1, "冻结商家对应额度的金额失败");
        //广告，剩余可交易余额减少
        ctcAdvert.setRemainQty(ctcAdvert.getRemainQty().subtract(qty));
        ctcAdvertMapper.updateById(ctcAdvert);
        //填家商家统计数据
        addMerchantCount(ctcOrder);
        //添加订单定时任务
        DelayQueueDesc queueDesc = DelayQueueDesc.newInstance(DelayQueueType.CTC_BUYORDER_TIMEOUT, ctcOrder.getId() + "");
        delayQueueService.send(queueDesc);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), CtcOrderStatus.NO_PAY, CtcOrderStatus.NO_PAY, CtcOrderLogType.NO_PAY, userId);
        //添加金额变更记录
        userCoinDetailService.addUserCoinDetail(oldUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_ORDER);
        log.info("C2C - 用户添加买入订单成功，orderId:{} - userId:{}", ctcOrder.getId(), userId);

        //返回前端需要的数据
        CtcOrderDetailVo detailVo = ctcOrderMapper.getDetailVOById(ctcOrder.getId());
        detailVo.setStatusDesc(CtcOrderStatusDescUtil.getStatusDesc(detailVo.getStatus(), CtcOrderType.USER_BUY, true));
        return detailVo;
    }

    private void sendSmsAndEmail(Member user, String message) {
        try {
            if (user.getPhoneStatus() == 1) {
                SMSUtil.sendCodeChinaPhone(user.getPhone(), message);
            }
            if (user.getEmailStatus() == 1) {
                sendEmailMsg(user.getEmail(), message, HttpContext.getLang());
            }
        } catch (Exception e) {
            log.error("C2C - 发送用户卖出提醒短信失败 - message：{}", message);
        }

    }


    //用户卖出要保存银行卡
    @Override
    @DataSource(DataSourceType.MASTER)
    public synchronized void sale(Integer ctcAdvertId, BigDecimal qty, String smsCode, String emailCode) {
        //当前操作用户
        Integer userId = HttpContext.getUserId();
        //资金是否冻结
        commonCheckService.checkFreezing(userId);
        //是否实名认证,是否绑定银行卡,是否绑定手机
        commonCheckService.checkHasAuthentication(userId);
        commonCheckService.checkHasBankCard(userId);
        commonCheckService.checkHasPhone(userId);
        //首先验证商户广告的真实性，验证广告的最大，最小量，验证广告的可购买量
        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(ctcAdvertId);
        AssertHelper.notEmpty(ctcAdvert, "C2C广告不存在");
        AssertHelper.isTrue(ctcAdvert.getStatus() == CtcAdvertStatus.PUTAWAY, "C2C广告已下架");
        AssertHelper.isTrue(ctcAdvert.getType() == CtcAdvertType.BUY, "C2C广告类型不正确");
        AssertHelper.isTrue(qty.compareTo(ctcAdvert.getMinQty()) >=0, "卖出个数小于最小卖出数");
        AssertHelper.isTrue(qty.compareTo(ctcAdvert.getMaxQty()) <=0, "卖出个数大于最大卖出数");
        AssertHelper.isTrue(!userId.equals(ctcAdvert.getUserId()), "商户禁止卖出自己的广告");
        //获取用户变更前的资金,验证用户可用余额
        UserCoin oldUserCoin = userCoinService.selectAndInsert(userId, ctcAdvert.getCoinId());
        AssertHelper.isTrue(oldUserCoin.getBanlance().compareTo(qty) >= 0, "余额不足，提交卖出订单失败");
        AssertHelper.isTrue(qty.compareTo(ctcAdvert.getRemainQty()) <=0, "广告剩余可交易余额不足");

        //广告剩余可交易余额不足时，短信或邮箱通知商户
        Member merchantUser = memberMapper.selectByPrimaryKey(ctcAdvert.getUserId());
        //商户资金是否冻结
        AssertHelper.isTrue(!commonCheckService.hasFreezing(merchantUser.getId()), "商户资金冻结，无法交易");
        if (qty.compareTo(ctcAdvert.getRemainQty().subtract(new BigDecimal(1000))) > 0){
            sendSmsAndEmail(merchantUser, "法币交易，您上架的买入广告剩余可交易余额不足1000");
        }
        BankCard bankCard = bankCardMapper.getDefaultCard(userId);//用户的银行卡
        AssertHelper.notEmpty(bankCard, "获取商户默认银行卡失败");

        //用户下单，提交订单，冻结下单金额的用户可用余额，
        BigDecimal totalCost = ctcAdvert.getCnyPrice().multiply(qty);
        CtcOrder ctcOrder = new CtcOrder();
        ctcOrder.setId(ctcOrderIdFactory.generateOrderId(ctcAdvert.getMerchantId()));
        ctcOrder.setFromUserId(userId);//从用户id到商家id
        ctcOrder.setToUserId(ctcAdvert.getUserId());
        ctcOrder.setCoinId(ctcAdvert.getCoinId());
        ctcOrder.setPrice(ctcAdvert.getCnyPrice());
        ctcOrder.setQty(qty);
        ctcOrder.setTotalCost(totalCost);
        ctcOrder.setCreateDate(new Date());
        ctcOrder.setStatus(CtcOrderStatus.NO_PAY);
        ctcOrder.setType(CtcOrderType.USER_SALE);
        ctcOrder.setMerchantId(ctcAdvert.getMerchantId());
        ctcOrder.setCtcAdvertId(ctcAdvertId);

        ctcOrder.setBankName(bankCard.getBankName());
        ctcOrder.setBankBranch(bankCard.getBankBranch());
        ctcOrder.setCardNo(bankCard.getCardNo());
        ctcOrder.setCity(bankCard.getCity());
        ctcOrder.setRealName(bankCard.getRealName());
        ctcOrderMapper.insert(ctcOrder);

        //广告，剩余可交易余额减少
        ctcAdvert.setRemainQty(ctcAdvert.getRemainQty().subtract(qty));
        ctcAdvertMapper.updateById(ctcAdvert);
        //添加订单定时任务
        DelayQueueDesc queueDesc = DelayQueueDesc.newInstance(DelayQueueType.CTC_SALEORDER_TIMEOUT, ctcOrder.getId() + "");
        delayQueueService.send(queueDesc);
        //卖出金额超过5w，邮箱验证------不超过5w，短信验证
        if (totalCost.compareTo(new BigDecimal(50000)) >= 0) {
            commonCheckService.checkHasEmail(userId);
        }
        //判断验证码是否正确
        checkVerificationCode(userId, totalCost, smsCode, emailCode);
        //提交订单后，冻结用户对应额度的金额
        int freezingSucc = userCoinMapper.freezingByUserIdAndCoinId(ctcOrder.getQty(), ctcOrder.getCoinId(), ctcOrder.getFromUserId(), oldUserCoin.getVersion());
        AssertHelper.isTrue(freezingSucc >= 1, "冻结用户对应额度的金额失败");
        //填家商家统计数据
        addMerchantCount(ctcOrder);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), CtcOrderStatus.NO_PAY, CtcOrderStatus.NO_PAY, CtcOrderLogType.NO_PAY, userId);
        //添加金额变更记录
        userCoinDetailService.addUserCoinDetail(oldUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_ORDER);
        log.info("C2C - 用户添加卖出订单成功，orderId:{} - userId:{}", ctcOrder.getId(), userId);
        //发短信提醒商户
        sendSmsAndEmail(merchantUser, "收到用户卖出订单，请及时打款 订单id："+ctcOrder.getId());
    }

    //添加对应的交易次数
    private void addMerchantCount(CtcOrder ctcOrder){
        Merchant merchant = merchantMapper.selectById(ctcOrder.getMerchantId());
        AssertHelper.notEmpty(merchant, "广告商户不存在");
        if (ctcOrder.getType() == CtcOrderType.USER_BUY) {
            merchant.setSaleTotalCount(merchant.getSaleSuccCount() + 1);
        } else if (ctcOrder.getType() == CtcOrderType.USER_SALE) {
            merchant.setBuyTotalCount(merchant.getBuyTotalCount() + 1);
        }
        merchantMapper.updateById(merchant);
    }

    /**
     * 验证短信验证码和邮箱验证码
     */
    private void checkVerificationCode(Integer userId, BigDecimal totalCost, String smsCode, String emailCode){
        Member user = memberMapper.selectByPrimaryKey(userId);
        try {
            boolean trueSmsCode = smsCaptchaService.checkSmsOnce(user.getPhone(), smsCode);
            AssertHelper.isTrue(trueSmsCode, "短信验证码错误");
            if (totalCost.compareTo(new BigDecimal(50000))>=0){
                boolean trueEmailCode = emailCaptchaService.checkEmailCodeOnce(emailCode, user.getEmail());
                AssertHelper.isTrue(trueEmailCode, "邮箱验证码错误");
            }
        } catch (VoException e) {
            throw new RuntimeException("验证短信验证码和邮箱验证码出错", e);
        }
    }

    //取消订单
    @Override
    @DataSource(DataSourceType.MASTER)
    public synchronized void cancel(Long id) {
        Integer userId = HttpContext.getUserId();
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        AssertHelper.isTrue(ctcOrder.getToUserId().equals(userId), "您不属于订单的买入方");
        AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.NO_PAY && ctcOrder.getType() == CtcOrderType.USER_BUY, "操作失败，订单已经超时自动取消或无法取消");

        //放币方from_user_id 的变更前金额记录
        UserCoin oldUserCoin = userCoinService.selectAndInsert(ctcOrder.getFromUserId(), ctcOrder.getCoinId());
        //取消订单=修改订单状态，取消冻结金额
        ctcOrder.setStatus(CtcOrderStatus.CANCEL);
        ctcOrderMapper.updateById(ctcOrder);
        int cancelFreezingSucc = userCoinMapper.cancelFreezingByUserIdAndCoinId(ctcOrder.getQty(), ctcOrder.getCoinId(), ctcOrder.getFromUserId(), oldUserCoin.getVersion());
        AssertHelper.isTrue(cancelFreezingSucc >= 1, "取消订单，解冻金额失败");

        //广告，剩余可交易余额增加
        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(ctcOrder.getCtcAdvertId());
        ctcAdvert.setRemainQty(ctcAdvert.getRemainQty().add(ctcOrder.getQty()));
        ctcAdvertMapper.updateById(ctcAdvert);
        //添加资金变更记录
        userCoinDetailService.addUserCoinDetail(oldUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_CANCEL);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), CtcOrderStatus.NO_PAY, CtcOrderStatus.CANCEL, CtcOrderLogType.CANCEL, userId);
        log.info("C2C - 用户取消订单成功，orderId:{} - userId:{} - cancelFreezing:{}", id, userId, ctcOrder.getQty());
    }

    //用户或商户点击已付款
    @Override
    @DataSource(DataSourceType.MASTER)
    public synchronized void payed(Long id) {
        Integer userId = HttpContext.getUserId();
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        AssertHelper.isTrue(ctcOrder.getToUserId().equals(userId) || ctcOrder.getFromUserId().equals(userId), "订单不属于用户");
        AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.NO_PAY, "订单不属于用户");

        //******用户卖出订单，商户点击已付款，直接完成交易
        if (ctcOrder.getType() == CtcOrderType.USER_SALE && userId.equals(ctcOrder.getToUserId())){
            finish(id);
            Member user = memberMapper.selectByPrimaryKey(ctcOrder.getFromUserId());
            sendSmsAndEmail(user, "商家已经付款，请注意查收 订单id："+ctcOrder.getId());
            return;
        }
        //*****************************************************

        ctcOrder.setStatus(CtcOrderStatus.PAYED);
        ctcOrderMapper.updateById(ctcOrder);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), CtcOrderStatus.NO_PAY, CtcOrderStatus.PAYED, CtcOrderLogType.PAY, userId);
        log.info("C2C - 用户点击已付款，orderId:{} - userId:{}", id, userId);
        if (ctcOrder.getType() == CtcOrderType.USER_BUY && userId.equals(ctcOrder.getToUserId())) {//用户买入，用户点击付款,发消息给商户
            Member user = memberMapper.selectByPrimaryKey(ctcOrder.getFromUserId());
            sendSmsAndEmail(user, "用户已经付款，请检查后放币 订单id："+ctcOrder.getId());
        } else if (ctcOrder.getType() == CtcOrderType.USER_SALE && userId.equals(ctcOrder.getToUserId())){//用户卖出，商户点击付款,发消息给用户
            Member user = memberMapper.selectByPrimaryKey(ctcOrder.getFromUserId());
            sendSmsAndEmail(user, "商家已经付款，请注意查收 订单id："+ctcOrder.getId());
        }

    }

    //付款方进行订单申诉

    /**
     * 可以进行申诉的情况：
     * （用户）
     * 1.用户卖出订单，商家未付款时
     * 2.用户买入，用户已付款时
     * 3.用户卖出订单，商家已付款时
     *
     * （商户）
     * 4.用户卖出订单，商家已付款
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public synchronized void appeal(Long id, String appealDesc) {
        Integer userId = HttpContext.getUserId();
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);

        checkCanAppeal(userId, ctcOrder);
        //添加申诉记录，修改订单状态为申诉中
        CtcAppeal ctcAppeal = CtcAppeal.newInstance(userId, id, appealDesc);
        ctcAppealMapper.insert(ctcAppeal);
        ctcOrder.setStatus(CtcOrderStatus.APPEAL);
        ctcOrderMapper.updateById(ctcOrder);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), CtcOrderStatus.PAYED, CtcOrderStatus.APPEAL, CtcOrderLogType.APPEAL, userId);
        log.info("C2C - 用户发起申诉，orderId:{} - userId:{}", id, userId);
    }

    private void checkCanAppeal(Integer userId, CtcOrder ctcOrder) {
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        boolean isUser = isUser(userId, ctcOrder);
        if (ctcOrder.getType() == CtcOrderType.USER_BUY) {
            AssertHelper.isTrue(isUser, "当前状态无法申诉");
            AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PAYED, "当前状态无法申诉，请联系客服");
        } else if (ctcOrder.getType() == CtcOrderType.USER_SALE){
            if (isUser) {
//                AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PAYED || ctcOrder.getStatus() == CtcOrderStatus.NO_PAY, "当前状态无法申诉，请联系客服");
                AssertHelper.isTrue(false, "当前状态无法申诉，请联系客服");
            } else {
//                AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PAYED, "当前状态无法申诉，请联系客服");
                AssertHelper.isTrue(false, "当前状态无法申诉，请联系客服");

            }
        }
    }

    //判断是否为用户（相对商家而言）
    private  boolean isUser(Integer userId, CtcOrder ctcOrder) {
        if (ctcOrder.getType() == CtcOrderType.USER_BUY) {
            return userId.equals(ctcOrder.getToUserId());
        } else if (ctcOrder.getType() == CtcOrderType.USER_SALE) {
            return userId.equals(ctcOrder.getFromUserId());
        }
        return false;
    }

    //付款方撤销订单申诉
    @Override
    @DataSource(DataSourceType.MASTER)
    public synchronized void cancelAppeal(Long id) {
        Integer userId = HttpContext.getUserId();
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        boolean isAppealed = ctcAppealMapper.isAppealed(id, userId);
        AssertHelper.isTrue(isAppealed, "申诉不属于用户");
        CtcOrderStatus oldCtcOrderStatus = ctcOrder.getStatus();
        //当状态属于申诉中，或处理中才可以撤销申诉
        AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.APPEAL
                || ctcOrder.getStatus() == CtcOrderStatus.PROCESSING, "当前状态无法撤销申诉，请联系客服");
        //更新申诉记录的状态
        CtcAppeal ctcAppeal = ctcAppealMapper.getCanCancelByOrderId(id);
        AssertHelper.notEmpty(ctcAppeal, "申诉记录不存在");
        ctcAppeal.setStatus(CtcAppealStatus.CANCEL);
        ctcAppealMapper.updateById(ctcAppeal);
        //订单状态变为申诉前的状态
        //获取申诉前的状态
        CtcOrderStatus beforeAppealStatus = ctcOrderLogMapper.getBeforeAppealStatus(ctcOrder.getId());
        ctcOrder.setStatus(beforeAppealStatus);
        ctcOrderMapper.updateById(ctcOrder);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), oldCtcOrderStatus, beforeAppealStatus, CtcOrderLogType.CANCEL_APPEAL, userId);
        log.info("C2C - 用户撤销申诉，orderId:{} - userId:{}", id, userId);
    }

    //收款方进行放币操作
    @Override
    @DataSource(DataSourceType.MASTER)
    public synchronized void finish(Long id) {
        Integer userId = HttpContext.getUserId();
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        AssertHelper.notEmpty(ctcOrder, "交易订单不存在");
        if (ctcOrder.getType() == CtcOrderType.USER_BUY) {
            AssertHelper.isTrue(ctcOrder.getFromUserId().equals(userId) , "订单不属于用户");
            AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PAYED, "当前订单状态无法进行放币操作");
        }
        if (ctcOrder.getType() == CtcOrderType.USER_SALE) {
            AssertHelper.isTrue(ctcOrder.getToUserId().equals(userId), "订单不属于用户");
            AssertHelper.isTrue(ctcOrder.getStatus() == CtcOrderStatus.PAYED || ctcOrder.getStatus() == CtcOrderStatus.NO_PAY, "当前订单状态无法进行放币操作");
        }

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
        //填家商家统计数据
        calculateMerchant(ctcOrder);
        //添加金额变更记录
        userCoinDetailService.addUserCoinDetail(oldFromUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_FINISH);
        userCoinDetailService.addUserCoinDetail(oldToUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_FINISH);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), CtcOrderStatus.PAYED, CtcOrderStatus.FINISH, CtcOrderLogType.FINISH, userId);
        log.info("C2C - 收款方进行放币操作，orderId:{} - userId:{}", id, userId);

        //用户卖出订单，商户放币，发送提示短信给用户
        if (ctcOrder.getType() == CtcOrderType.USER_BUY && userId.equals(ctcOrder.getFromUserId())) {
            Member user = memberMapper.selectByPrimaryKey(ctcOrder.getToUserId());
            sendSmsAndEmail(user, "您购买的币已到账，请注意查收 订单id："+ctcOrder.getId());
        }


    }

    //保存交易成功记录统计
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

    //用户交易记录
    @Override
    @DataSource(DataSourceType.SLAVE)
    public PageInfo<CtcOrderForUserVo> pageForUserWeb(PageParam pageParam, CtcOrderType type) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<CtcOrderForUserVo> ctcOrderForUserVos = ctcOrderMapper.pageForUserWeb(HttpContext.getUserId(), type);
        ctcOrderForUserVos.forEach( item ->{
            String desc = CtcOrderStatusDescUtil.getStatusDesc(item.getStatus(), item.getType(), true);
            item.setStatusDesc(desc);
            item.setTypeDesc(item.getType().descForUser);
            if (item.getStatus() == CtcOrderStatus.APPEAL || item.getStatus() == CtcOrderStatus.PROCESSING ){
                item.setAppealed(ctcAppealMapper.isAppealed(item.getId(), HttpContext.getUserId()));
            }
        });
        return new PageInfo<>(ctcOrderForUserVos);
    }

    //商户交易记录
    @Override
    @DataSource(DataSourceType.SLAVE)
    public PageInfo<CtcOrderForMerchantVo> pageForMerchantWeb(PageParam pageParam, CtcOrderType type) {
        //保存获取数据的时间
        redisUtil.setString(lastRefreshTimeKey + HttpContext.getUserId(), System.currentTimeMillis()+"");
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<CtcOrderForMerchantVo> ctcOrderForMerchantVos = ctcOrderMapper.pageForMerchantWeb(HttpContext.getUserId(), type);
        fillCtcOrderForMerchantVos(ctcOrderForMerchantVos);
        return new PageInfo<>(ctcOrderForMerchantVos);
    }

    //商户订单管理
    @Override
    @DataSource(DataSourceType.SLAVE)
    public PageInfo<CtcOrderForMerchantVo> pageForMerchantManage(PageParam pageParam, CtcOrderType type) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<CtcOrderForMerchantVo> ctcOrderForMerchantVos = ctcOrderMapper.pageForMerchantManage(HttpContext.getUserId(), type);
        fillCtcOrderForMerchantVos(ctcOrderForMerchantVos);
        return new PageInfo<>(ctcOrderForMerchantVos);
    }

    private void fillCtcOrderForMerchantVos(List<CtcOrderForMerchantVo> ctcOrderForMerchantVos){
        ctcOrderForMerchantVos.forEach( item ->{
            String desc = CtcOrderStatusDescUtil.getStatusDesc(item.getStatus(), item.getType(), false);
            item.setStatusDesc(desc);
            item.setTypeDesc(item.getType().descForMerchant);
            if (item.getStatus() == CtcOrderStatus.APPEAL || item.getStatus() == CtcOrderStatus.PROCESSING ){
                item.setAppealed(ctcAppealMapper.isAppealed(item.getId(), HttpContext.getUserId()));
            }

            //添加用户信息
            if (item.getType() == CtcOrderType.USER_BUY) {
                Member user = memberMapper.selectByPrimaryKey(item.getToUserId());
                item.setContactWay(EmptyHelper.isEmpty(user.getPhone()) ? user.getEmail() : user.getPhone());
                item.setIdName(user.getIdName());
            } else if (item.getType() == CtcOrderType.USER_SALE) {
                Member user = memberMapper.selectByPrimaryKey(item.getFromUserId());
                item.setContactWay(EmptyHelper.isEmpty(user.getPhone()) ? user.getEmail() : user.getPhone());
                item.setIdName(user.getIdName());
            }
        });
    }


    //订单超时，取消订单
    @Override
    @DataSource(DataSourceType.MASTER)
    public void timeoutCancel(Long id){
        CtcOrder ctcOrder = ctcOrderMapper.selectById(id);
        if (ctcOrder == null) {
            log.error("交易订单不存在, ctcOrderId:{}", id);
            return;
        }
        if (ctcOrder.getStatus() != CtcOrderStatus.NO_PAY){//超时只处理没有付款的订单
            return;
        }

        UserCoin oldUserCoin = userCoinService.selectAndInsert(ctcOrder.getFromUserId(), ctcOrder.getCoinId());
        //取消订单=修改订单状态，取消冻结金额
        ctcOrder.setStatus(CtcOrderStatus.CANCEL);
        int updateOrderSucc = ctcOrderMapper.updateById(ctcOrder);
        int cancelFreezingSucc = userCoinMapper.cancelFreezingByUserIdAndCoinId(ctcOrder.getQty(), ctcOrder.getCoinId(), ctcOrder.getFromUserId(), oldUserCoin.getVersion());
        AssertHelper.isTrue(cancelFreezingSucc >= 1 && updateOrderSucc >= 1, "订单超时操作 - 系统取消订单，解冻金额失败 - ctcOrderId:"+id);

        //广告，剩余可交易余额增加
        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(ctcOrder.getCtcAdvertId());
        ctcAdvert.setRemainQty(ctcAdvert.getRemainQty().add(ctcOrder.getQty()));
        ctcAdvertMapper.updateById(ctcAdvert);
        //添加资金变更记录
        userCoinDetailService.addUserCoinDetail(oldUserCoin, ctcOrder.getId()+"", UserCoinDetailType.CTC_CANCEL);
        //添加订单状态变更记录
        ctcOrderLogService.saveOne(ctcOrder.getId(), CtcOrderStatus.NO_PAY, CtcOrderStatus.CANCEL, CtcOrderLogType.TIME_OUT, HttpContext.getUserId());
        log.info("C2C - 超时取消订单成功，orderId:{}  - cancelFreezing:{}", id, ctcOrder.getQty());
    }

    public static void sendEmailMsg(String email, String message, String lang) {
        String str1="";
        String str2="";
        String str3="";
        String str4="";
        String str5="";
        String str6="";
        String str7="";
        if("cn".equals(lang)) {
            str1="亲爱的用户，";
            str2="您好！";
            str5="xbtc团队";
            str6="系统邮件，请勿回复！";
            str7="邮箱验证码";
        }else {
            str1="Dear user, ";
            str2="hello!";
            str5="xbtc team";
            str6="system mail, please do not reply!";
            str7="Mailbox verification code";
        }

        StringBuilder content=new StringBuilder();
        content.append("<div id='mailContentContainer' class='qmbox qm_con_body_content qqmail_webmail_only'>");
        content.append("<table width='700' border='0' align='center' cellspacing='0' style='width:700px;'>");
        content.append("<tbody><tr><td>");
        content.append("<div style='width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;'>");
        content.append("<table border='0' cellpadding='0' cellspacing='0' width='700' height='39' style='font:12px Tahoma, Arial, 宋体;'>");
        content.append("<tbody> <tr><td width='210'></td></tr></tbody></table></div>");
        content.append("<div style='width:680px;padding:0 10px;margin:0 auto;'>");
        content.append(" <div style='line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;'>");
        content.append("<strong style='display:block;margin-bottom:15px;'>"+str1+"<span style='color:#f60;font-size: 16px;'></span>"+str2+"</strong>");
        content.append("<strong style='display:block;margin-bottom:15px;'>"+message+"</strong></div>");
        content.append("<div style='margin-bottom:30px;'></div></div>");
        content.append("<div style='width:700px;margin:0 auto;'>");
        content.append("<div style='padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;'>");
        content.append("<p>"+str5+"<br>"+str6+"</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("</td> </tr></tbody></table>");
        content.append("<style type='text/css'>.qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}</style></div>");

        String str=EmailUtil.sendHtmlMail(email,str7,content.toString(),"系统错误",true);
    }

    public static void main(String[] args) {
        sendEmailMsg("1024503877@qq.com", "666", "cn");
    }

}
