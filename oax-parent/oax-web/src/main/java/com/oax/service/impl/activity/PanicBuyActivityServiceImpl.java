package com.oax.service.impl.activity;

import com.oax.common.AssertHelper;
import com.oax.common.RedisUtil;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.PanicBuyActivity;
import com.oax.entity.activity.PanicBuyDetail;
import com.oax.entity.activity.PanicBuyOrder;
import com.oax.mapper.activity.PanicBuyActivityMapper;
import com.oax.mapper.activity.PanicBuyDetailMapper;
import com.oax.mapper.activity.PanicBuyOrderMapper;
import com.oax.service.activity.PanicBuyActivityService;
import com.oax.vo.PanicBuyActivityDataVo;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional
public class PanicBuyActivityServiceImpl implements PanicBuyActivityService {

    @Autowired
    private PanicBuyActivityMapper panicBuyActivityMapper;

    @Autowired
    private PanicBuyDetailMapper panicBuyDetailMapper;

    @Autowired
    private PanicBuyOrderMapper panicBuyOrderMapper;

    @Autowired
    private RedisUtil redisUtil;

    //活动参与人数，随机统计
    private static final String PANIC_BUY_ACTIVITY_RANDOM_KEY = "activity_random:";

    @Override
    @DataSource(DataSourceType.MASTER)
    public void participate(Integer activityId, Integer userId) {
        PanicBuyActivity activity = panicBuyActivityMapper.selectById(activityId);
        AssertHelper.notEmpty(activity, "系统繁忙，未获取到活动信息");
        AssertHelper.isTrue(activity.getStatus() == PanicBuyActivity.Status.OPEN.ordinal(), "活动暂未开始或已结束");//判断活动的当前状态
        boolean participated = panicBuyDetailMapper.participated(activity.getId(), userId);//用户是否参加,活动只能参加一次
        AssertHelper.isTrue(!participated, "请勿重复参加");

        //新增参与详情
        PanicBuyDetail detail = new PanicBuyDetail();
        detail.setActivityId(activityId);
        detail.setCreateTime(new Date());
        detail.setUserId(userId);
        detail.setHelpValue(0);
        detail.setHelpCount(0);
        detail.setQty(BigDecimal.ZERO);
        detail.setRemainQty(BigDecimal.ZERO);
        detail.setShareCode(PanicBuyHelpServiceImpl.generateShareCode(userId, activityId));
        detail.setStatus(PanicBuyDetail.Status.PARTICIPATE.ordinal());
        panicBuyDetailMapper.insert(detail);

        //添加活动参与人数
        activity.setParticipate(activity.getParticipate() + 1);
        panicBuyActivityMapper.updateById(activity);
    }

    /**活动首页数据*/
    @Override
    @DataSource(DataSourceType.SLAVE)
    public PanicBuyActivityDataVo activityIndex(Integer userId){
        PanicBuyActivityDataVo dataVo;
        if (userId == null) {//用户没有登录
            dataVo = getPanicBuyActivityDataVoForNoLogin();
        } else {
            dataVo = getPanicBuyActivityDataVoForLogin(userId);
        }
        return dataVo;
    }

    private PanicBuyActivityDataVo getPanicBuyActivityDataVoForLogin(Integer userId){
        //获取今天的活动id
        PanicBuyActivity activity = panicBuyActivityMapper.getTodayNewlyOne();
        //获取最新的活动id
        PanicBuyActivity newlyActivity = panicBuyActivityMapper.getNewlyOne();
        if ((activity.getEndTime().getTime() + 1000 * 60 * 60L) < System.currentTimeMillis()) {//当前时间 > 今天活动的结束时间 + 1小时
            if (newlyActivity != null) {
                activity = newlyActivity;
            }
        }

        //查询我的最新达标和可购买权限有效记录
        PanicBuyDetail panicBuyDetail = panicBuyDetailMapper.getCanBuyDetail(userId);
        boolean canBuyed = false;
        if (panicBuyDetail != null){
            PanicBuyOrder panicBuyOrder = panicBuyOrderMapper.selectByUserIdAndActivityId(userId, panicBuyDetail.getActivityId());
            canBuyed = panicBuyOrder == null || panicBuyOrder.getStatus() == PanicBuyOrder.Status.NO_PAY.ordinal();//没有下单或没有支付
        }

        if (canBuyed) {
            activity = panicBuyActivityMapper.selectById(panicBuyDetail.getActivityId());
        }
        PanicBuyActivityDataVo activityDataVo = new PanicBuyActivityDataVo();
        activityDataVo.setActivityId(activity.getId());
        activityDataVo.setReachAll(activity.getReach() + activity.getReachBase());
        activityDataVo.setReachNeed(activity.getReachNeed());
        activityDataVo.setParticipateAll(activity.getParticipate() + activity.getParticipateBase() + generateParticipateRandom(activity));
        activityDataVo.setStatus(getPanicBuyActivityDataVoStatus(activity, userId, canBuyed));
        activityDataVo.setEndDate(activity.getEndTime());
        activityDataVo.setShareCode(PanicBuyHelpServiceImpl.generateShareCode(userId, activity.getId()));
        return activityDataVo;
    }

    private PanicBuyActivityDataVo getPanicBuyActivityDataVoForNoLogin(){
        //获取今天的活动id
        PanicBuyActivity activity = panicBuyActivityMapper.getTodayNewlyOne();
        //获取最新的活动id
        PanicBuyActivity newlyActivity = panicBuyActivityMapper.getNewlyOne();
        if ((activity.getEndTime().getTime() + 1000 * 60 * 60L) < System.currentTimeMillis()) {//当前时间 > 今天活动的结束时间 + 1小时
            if (newlyActivity != null) {
                activity = newlyActivity;
            }
        }
        AssertHelper.notEmpty(activity, "活动暂未开始或已结束");
        PanicBuyActivityDataVo activityDataVo = new PanicBuyActivityDataVo();
        activityDataVo.setActivityId(activity.getId());
        activityDataVo.setReachAll(activity.getReach() + activity.getReachBase());
        activityDataVo.setReachNeed(activity.getReachNeed());
        activityDataVo.setParticipateAll(activity.getParticipate() + activity.getParticipateBase() + generateParticipateRandom(activity));
        activityDataVo.setStatus(getPanicBuyActivityDataVoStatus(activity, null, false));
        activityDataVo.setEndDate(activity.getEndTime());
        return activityDataVo;
    }

    /**
     * 活动返回首页数据的状态，由于区分页面的显示
     * @param activity
     * @param userId
     * @param canBuyed
     * @return
     */
    private Integer getPanicBuyActivityDataVoStatus(PanicBuyActivity activity, Integer userId, boolean canBuyed) {
        boolean participated = panicBuyDetailMapper.participated(activity.getId(), userId);//用户是否参加
        if (userId == null || !participated) {//用户未登录, 用户未参加
            if (activity.getStartTime().getTime() > System.currentTimeMillis()){//活动未开始
                return PanicBuyActivityDataVo.Status.NO_START.ordinal();
            } else if (activity.getStartTime().getTime() <= System.currentTimeMillis() && activity.getEndTime().getTime() > System.currentTimeMillis()){//活动开始，未参加
                return PanicBuyActivityDataVo.Status.NO_PARTICIPATE.ordinal();
            } else {//未参加，已结束
                return PanicBuyActivityDataVo.Status.OVER.ordinal();
            }
        }else {//用户已经参加活动
            boolean reached = panicBuyDetailMapper.reached(activity.getId(), userId);//用户是否达标
            if (activity.getStartTime().getTime() <= System.currentTimeMillis() && activity.getEndTime().getTime() > System.currentTimeMillis()){
                if (reached) {//活动开始，未结束，已达标
                    return PanicBuyActivityDataVo.Status.REACH.ordinal();
                } else {
                    return PanicBuyActivityDataVo.Status.PARTICIPATE.ordinal();
                }
            } else if (activity.getEndTime().getTime() < System.currentTimeMillis()){//活动结束
                if (reached) {//活动开始，未结束，已达标
                    if (canBuyed){//用户有购买权限
                        return PanicBuyActivityDataVo.Status.CAN_BUY.ordinal();
                    } else {
                        return PanicBuyActivityDataVo.Status.BUYED.ordinal();
                    }
                } else {
                    return PanicBuyActivityDataVo.Status.NO_REACH.ordinal();
                }
            }
        }
        return PanicBuyActivityDataVo.Status.NO_START.ordinal();
    }

    //每10-20s   加4-5个    每分钟范围（12-30）
    private Integer generateParticipateRandom(PanicBuyActivity activity) {
        Integer participateCount = 0;
        Long timestamp = activity.getStartTime().getTime();
        String str = redisUtil.getString(getKey(activity.getId()));
        if (StringUtils.isNotBlank(str)) {
            String[] array = StringUtils.split(str, "_");
            timestamp = Long.parseLong(array[0]);
            participateCount = Integer.parseInt(array[1]);
        }
        if (activity.getStartTime().getTime() <= System.currentTimeMillis()
                && activity.getEndTime().getTime() >= System.currentTimeMillis()
                && System.currentTimeMillis() >= timestamp) {
            Integer base = Math.toIntExact((System.currentTimeMillis() - timestamp) / (RandomUtils.nextInt(10, 20) * 1000));
            if (base <= 0) {
                base = 0;
            }
            if (base > 0) {
                participateCount = participateCount + base * RandomUtils.nextInt(4, 5);
                redisUtil.setString(getKey(activity.getId()), System.currentTimeMillis()+"_"+participateCount, RedisUtil.NOT_TIMEOUT);
            }
        }
        if (participateCount >= 4000) {
            participateCount = 4000;
        }
        if (participateCount < 0){
            participateCount =0;
        }
        return participateCount;
    }

    private String getKey(Integer activityId) {
        return PANIC_BUY_ACTIVITY_RANDOM_KEY + activityId;
    }

}
