package com.oax.scheduled;

import com.oax.common.EmptyHelper;
import com.oax.common.RedisUtil;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.Bonus;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.vo.MemberCoinVo;
import com.oax.mapper.front.*;
import com.oax.service.RechargeService;
import com.oax.service.UserCoinDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 新版BHB分红任务
 */
@Slf4j
@Component
@Transactional
public class NewBonusScheduled {

    @Autowired
    UserCoinMapper userCoinMapper;

    @Autowired
    UserCoinDetailMapper userCoinDetailMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    RechargeService rechargeService;

    @Autowired
    TradeMapper tradeMapper;

    @Autowired
    BonusMapper bonusMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    BigDecimal BHBUSDTratio;

    /**
     * 分红机器人
     */
    public void BHBBonus() {
        log.error("======================开始进行BHB分红==============================");
        this.BHBUSDTratio = tradeMapper.getYesterdayAvgByMarketId(77);//77是BHB/USDT的交易对
        log.info("BHB 转换 USDT的比例：{}", BHBUSDTratio);
        //拥有个数
        Integer thresholdBHBCount = 700;
        //币种id
        Integer coinId = 54;
        //收益币种
        Integer bonusCoinId = 10;

        //获取达到阈值的用户list
        Date startTime = getStartTime();
        Date endTime = getEndTime();
        List<MemberCoinVo> memberCoinVos = userCoinMapper.selectByCoinIdAndCount(coinId, thresholdBHBCount, startTime , endTime);
        log.info("BHB分红：获取持币达标用户成功，memberCoinVos.size={}", memberCoinVos.size());
        memberCoinVos.forEach(MemberCoinVo -> {
            forlayered(MemberCoinVo, memberCoinVos, coinId, bonusCoinId);
        });
    }

    //获取用户分层
    public void forlayered(MemberCoinVo memberCoinVo, List<MemberCoinVo> memberCoinVos, Integer coinId, Integer bonusCoinId) {
        //遍历出一二三级用户
        List<MemberCoinVo> oneLevelMemberCoinVos = memberCoinVos.stream().filter(item -> memberCoinVo.getId().equals(item.getFromUserId())).collect(Collectors.toList());
        List<Integer> oneLevelUserIds = oneLevelMemberCoinVos.stream().map(MemberCoinVo::getId).collect(Collectors.toList());

        List<MemberCoinVo> twoLevelMemberCoinVos = memberCoinVos.stream().filter(item -> oneLevelUserIds.contains(item.getFromUserId())).collect(Collectors.toList());
        List<Integer> twoLevelUserIds = twoLevelMemberCoinVos.stream().map(MemberCoinVo::getId).collect(Collectors.toList());

        List<MemberCoinVo> threeLevelMemberCoinVos = memberCoinVos.stream().filter(item -> twoLevelUserIds.contains(item.getFromUserId())).collect(Collectors.toList());
//        List<Integer> threeLevelUserIds = threeLevelMemberCoinVos.stream().map(MemberCoinVo::getFromUserId).collect(Collectors.toList());

        //从不同级别的用户中获取收益
        List<Bonus> oneLevelBonuses = oneLevelMemberCoinVos.stream().map(itemMemberCoinVo -> {
            Bonus bonus = new Bonus();
            bonus.setFromUserId(itemMemberCoinVo.getId().longValue());
            bonus.setToUserId(memberCoinVo.getId().longValue());

            bonus.setRightCoinId(bonusCoinId.longValue());
            bonus.setLeftCoinId(coinId.longValue());
            bonus.setThresholdNumber(itemMemberCoinVo.getBanlance());//分红时的持仓量
            bonus.setAverageThreshold(getAverageThreshold(itemMemberCoinVo, coinId));
            bonus.setBonus(getUSDTFromBHB(itemMemberCoinVo.getBanlance().multiply(new BigDecimal(0.01))));
            bonus.setHierarchy(1);//层级
            return bonus;
        }).collect(Collectors.toList());

        //二级收益
        List<Bonus> twoLevelBonuses = twoLevelMemberCoinVos.stream().map(itemMemberCoinVo -> {
            Bonus bonus = new Bonus();
            bonus.setFromUserId(itemMemberCoinVo.getId().longValue());
            bonus.setToUserId(memberCoinVo.getId().longValue());

            bonus.setRightCoinId(bonusCoinId.longValue());
            bonus.setLeftCoinId(coinId.longValue());
            bonus.setThresholdNumber(itemMemberCoinVo.getBanlance());//分红时的持仓量
            bonus.setAverageThreshold(getAverageThreshold(itemMemberCoinVo, coinId));
            bonus.setBonus(getUSDTFromBHB(itemMemberCoinVo.getBanlance().multiply(new BigDecimal(0.005))));
            bonus.setHierarchy(2);//层级
            return bonus;
        }).collect(Collectors.toList());

        //三级收益
        List<Bonus> threeLevelBonuses = threeLevelMemberCoinVos.stream().map(itemMemberCoinVo -> {
            Bonus bonus = new Bonus();
            bonus.setFromUserId(itemMemberCoinVo.getId().longValue());
            bonus.setToUserId(memberCoinVo.getId().longValue());

            bonus.setRightCoinId(bonusCoinId.longValue());
            bonus.setLeftCoinId(coinId.longValue());
            bonus.setThresholdNumber(itemMemberCoinVo.getBanlance());//分红时的持仓量
            bonus.setAverageThreshold(getAverageThreshold(itemMemberCoinVo, coinId));
            bonus.setBonus(getUSDTFromBHB(itemMemberCoinVo.getBanlance().multiply(new BigDecimal(0.002))));
            bonus.setHierarchy(3);//层级
            return bonus;
        }).collect(Collectors.toList());

        //自身持有收益
        Bonus bonus = new Bonus();
        bonus.setFromUserId(memberCoinVo.getId().longValue());
        bonus.setToUserId(memberCoinVo.getId().longValue());

        bonus.setRightCoinId(bonusCoinId.longValue());
        bonus.setLeftCoinId(coinId.longValue());
        bonus.setThresholdNumber(memberCoinVo.getBanlance());//分红时的持仓量
        bonus.setAverageThreshold(getAverageThreshold(memberCoinVo, coinId));
        bonus.setBonus(getUSDTFromBHB(memberCoinVo.getBanlance().multiply(new BigDecimal(0.07))));
        bonus.setHierarchy(0);//层级
        bonusMapper.insert(bonus);

        BigDecimal myBouns = bonus.getBonus().setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal oneLevelAllBouns = oneLevelBonuses.stream().map(Bonus::getBonus).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal twoLevelAllBouns = twoLevelBonuses.stream().map(Bonus::getBonus).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal threeLevelAllBouns = threeLevelBonuses.stream().map(Bonus::getBonus).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP);

//        //批量插入
//        if (!EmptyHelper.isEmpty(oneLevelBonuses)) {
//            bonusMapper.batchInsert(oneLevelBonuses);
//        }
//        if (!EmptyHelper.isEmpty(twoLevelBonuses)) {
//            bonusMapper.batchInsert(twoLevelBonuses);
//        }
//
//        if (!EmptyHelper.isEmpty(threeLevelBonuses)) {
//            bonusMapper.batchInsert(threeLevelBonuses);
//        }
//
//        bonusMapper.insert(bonus);
//        //新增用户收益
//        UserCoin userCoin = userCoinMapper.selectByUserIdAndCoinId(memberCoinVo.getId(), bonusCoinId);
//        if (userCoin == null) {
//            UserCoin entity = UserCoin.newInstance(memberCoinVo.getId(), bonusCoinId);
//            userCoinMapper.insertSelective(entity);
//            userCoin = userCoinMapper.selectByUserIdAndCoinId(memberCoinVo.getId(), bonusCoinId);
//        }
        BigDecimal allBonus = myBouns.add(oneLevelAllBouns).add(twoLevelAllBouns).add(threeLevelAllBouns);
//        int addSuccess = userCoinMapper.addBanlance(allBonus, bonusCoinId, memberCoinVo.getId(), userCoin.getVersion());
//        if (addSuccess <= 0){
//            throw new RuntimeException("分红，添加用户资金出错");
//        }
//        userCoinDetailService.addUserCoinDetail(userCoin, "", UserCoinDetailType.BONUS);

        log.info("用户id：{} - 处理分红成功，自身收益={}，一级收益={}，{}；二级收益={}，{}；三级收益={}，{}；总收益：{}"
                , memberCoinVo.getId(), myBouns.floatValue(), oneLevelAllBouns.floatValue(), oneLevelBonuses.size(), twoLevelAllBouns.floatValue(), twoLevelBonuses.size()
                , threeLevelAllBouns.floatValue(), threeLevelBonuses.size(),allBonus.doubleValue());
    }

    //BHB个数按比例转化为usdt
    public BigDecimal getUSDTFromBHB(BigDecimal BHBQty) {
        if (BHBUSDTratio == null) {
            BHBUSDTratio = BigDecimal.ONE;
        }
        return BHBQty.divide(BHBUSDTratio);
    }

    //获取用户的平均持有量
    public BigDecimal getAverageThreshold(MemberCoinVo memberCoinVo, Integer coinId) {
        Date startTime = getStartTime();
        Date endTime = getEndTime();
        BigDecimal avgThreshold = userCoinDetailMapper.getAverageThreshold(memberCoinVo.getId(), coinId, startTime, endTime);
        if (avgThreshold == null) {//没有交易记录
            return memberCoinVo.getBanlance();
        }
        return avgThreshold;
    }

    /**
     * 前天的20:00
     * @return
     */
    public static Date getStartTime(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(-2).plusHours(20);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 昨天的20:00
     * @return
     */
    public static Date getEndTime(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(-1).plusHours(20);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

}