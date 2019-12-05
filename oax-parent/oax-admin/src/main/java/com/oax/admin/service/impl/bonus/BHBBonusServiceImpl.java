package com.oax.admin.service.impl.bonus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.UserCoinDetailService;
import com.oax.admin.service.bonus.BHBBonusService;
import com.oax.admin.util.UserUtils;
import com.oax.admin.vo.BonusDataVo;
import com.oax.common.AssertHelper;
import com.oax.common.DateHelper;
import com.oax.common.EmptyHelper;
import com.oax.common.RedisUtil;
import com.oax.common.constant.RedisKeyConstant;
import com.oax.common.json.JsonHelper;
import com.oax.entity.admin.param.BonusParam;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.Bonus;
import com.oax.entity.front.BonusLog;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.vo.MemberCoinVo;
import com.oax.mapper.front.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BHBBonusServiceImpl implements BHBBonusService {

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private UserCoinDetailMapper userCoinDetailMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private BonusMapper bonusMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    BigDecimal BHBUSDTratio;

    Double oneLevelBonus, twoLevelBonus, threeLevelBonus, selfLevelBonus;

    public static final String PASSWORD = "exD3vs4efE32";

    private final String ISBONUS_KEY = "BHB_is_bonus:";//用来判断今天是否已经进行了分红，防止误操作

    @Autowired
    private BonusLogMapper bonusLogMapper;

    private String getKey(){
        return ISBONUS_KEY + DateHelper.format(new Date(), DateHelper.DATE);
    }

    public PageInfo<Bonus> pageForAdmin(BonusParam bonusParam){
        PageHelper.startPage(bonusParam.getPageNum(), bonusParam.getPageSize());
        List<Bonus> list = bonusMapper.selectList(new QueryWrapper<Bonus>().lambda()
                .eq(Bonus::getToUserId,Long.parseLong(bonusParam.getUserId()+"")).orderByDesc(Bonus::getId));
        return new PageInfo<>(list);
    }

    /**
     * day = 0,是昨天的20:00
     * day = 1,是前天的20:00
     * @return
     */
    public static Date getStartTime(Integer day){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(-(1+day)).plusHours(20);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * day = 0,是今天20:00
     * day = 1,是昨天的20:00
     * @return
     */
    public static Date getEndTime(Integer day){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(-day).plusHours(20);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * day = 0,是昨天19:30
     * day = 1,前天的19:30
     * @return
     */
    public static Date getSnapshootStartTime(Integer day){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(-(1+day)).plusHours(19).plusMinutes(30);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * day = 0,今天的20:30
     * day = 1,昨天的20:30
     * @return
     */
    public static Date getSnapshootEndTime(Integer day){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime yesterdayMidnight = todayMidnight.plusDays(-day).plusHours(20).plusMinutes(30);
        ZonedDateTime zdt = yesterdayMidnight.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 分红机器人
     * day = 0表示跑的是明天的正式分红数据
     * day = 1表示跑的是今天的正式分红数据
     */
    public void BHBBonus(String password, Integer day,Double bhbUSDTratio,Double oneLevelBonus,
                         Double twoLevelBonus,Double threeLevelBonus,Double selfLevelBonus) {
        log.info("oneLevelBonus======="+oneLevelBonus);
        this.oneLevelBonus = oneLevelBonus==null?0.004:oneLevelBonus;
        this.twoLevelBonus = twoLevelBonus==null?0.002:twoLevelBonus;
        this.threeLevelBonus = threeLevelBonus==null?0.001:threeLevelBonus;
        this.selfLevelBonus = selfLevelBonus==null?0.002:selfLevelBonus;

        BonusDataVo bonusDataVo = new BonusDataVo();
        bonusDataVo.setStartTime(new Date());
        bonusDataVo.setTest(true);

        if (day == null) {
            day = 1;//默认跑今天的数据
        }
        //拥有个数
        Integer thresholdBHBCount = 700;
        //币种id
        Integer coinId = 54;
        //收益币种
        Integer bonusCoinId = 10;
        Integer marketId = 77;
        //获取达到阈值的用户list
        Date startTime = getStartTime(day);
        Date endTime = getEndTime(day);
        Date snapshootStartTime = getSnapshootStartTime(day);
        Date snapshootEndTime = getSnapshootEndTime(day);
        log.info("startTime={},  endTime={}, snapshootStartTime={}, snapshootEndTime={}", startTime, endTime, snapshootStartTime, snapshootEndTime);
        log.error("======================开始进行BHB分红==============================");
//        this.BHBUSDTratio = tradeMapper.getYesterdayAvgByMarketId(marketId);//77是BHB/USDT的交易对
//        this.BHBUSDTratio = tradeMapper.getAvgByMarketIdAndTime(marketId, startTime, endTime);
        if(bhbUSDTratio !=null){
            this.BHBUSDTratio = BigDecimal.valueOf(bhbUSDTratio);
        }else {
            this.BHBUSDTratio = tradeMapper.getAvgByMarketIdAndTime(marketId, startTime, endTime);
        }

        log.info("BHB 转换 USDT的比例：{}", BHBUSDTratio);



//        List<MemberCoinVo> memberCoinVos = userCoinMapper.selectByCoinIdAndCount(coinId, thresholdBHBCount, startTime, endTime);
//        List<MemberCoinVo> memberCoinVos = userCoinMapper.selectMemberCoinVoByBonusLog();
        List<MemberCoinVo> memberCoinVos = userCoinMapper.selectMemberCoinVoByUserCoinSnapshootLight(snapshootStartTime, snapshootEndTime);
        log.info("BHB分红：达标用户个数{}", memberCoinVos.size());
        if (PASSWORD.equals(password)) {
            bonusDataVo.setTest(false);
            String str = redisUtil.getString(getKey());
            AssertHelper.isEmpty(str, "操作失败，今天已经完成分红，无法再次进行分红");
            redisUtil.setString(getKey(), System.currentTimeMillis()+"", RedisUtil.NOT_TIMEOUT);
            log.error("||||||||||||||||||||||密码正确，计算并真正进行分红操作|||||||||||||||||||||");
        }

        BigDecimal allUserBonus = BigDecimal.ZERO;
        for (MemberCoinVo memberCoinVo : memberCoinVos) {
            BigDecimal oneUserAll = forlayered(password, memberCoinVo, memberCoinVos, coinId, bonusCoinId);
            allUserBonus = allUserBonus.add(oneUserAll);
        }
        log.info("===========分红总量:{}个usdt============", allUserBonus);
//        memberCoinVos.forEach(MemberCoinVo -> {
//            BigDecimal oneUserAll = forlayered(password, MemberCoinVo, memberCoinVos, coinId, bonusCoinId);
//        });

        bonusDataVo.setBhbUsdtRatio(BHBUSDTratio);
        bonusDataVo.setReachCount(memberCoinVos.size());
        bonusDataVo.setAllBonus(allUserBonus);
        bonusDataVo.setEndTime(new Date());
        bonusDataVo.setDataTimeStr(DateHelper.format(startTime) + "-" + DateHelper.format(endTime));
        bonusDataVo.setAdminName(UserUtils.getShiroUser().getName());
        redisUtil.zincrement(RedisKeyConstant.BONUS_RECORD_ZSET_KEY, JsonHelper.writeValueAsString(bonusDataVo), System.currentTimeMillis());

    }



    //获取用户分层
    public BigDecimal forlayered(String password, MemberCoinVo memberCoinVo, List<MemberCoinVo> memberCoinVos, Integer coinId, Integer bonusCoinId) {
        BigDecimal memberCoinVoBalance = memberCoinVo.getBanlance();

        //遍历出一二三级用户
        List<MemberCoinVo> oneLevelMemberCoinVos = memberCoinVos.stream().filter(item -> memberCoinVo.getId().equals(item.getFromUserId())).collect(Collectors.toList());
        List<Integer> oneLevelUserIds = oneLevelMemberCoinVos.stream().map(MemberCoinVo::getId).collect(Collectors.toList());
        List<Integer> oneLevelAllUserIds = getChildUserIds(Collections.singletonList(memberCoinVo.getId()));

        List<MemberCoinVo> twoLevelMemberCoinVos = memberCoinVos.stream().filter(item -> oneLevelAllUserIds.contains(item.getFromUserId())).collect(Collectors.toList());
        List<Integer> twoLevelUserIds = twoLevelMemberCoinVos.stream().map(MemberCoinVo::getId).collect(Collectors.toList());
        List<Integer> twoLevelAllUserIds = getChildUserIds(oneLevelAllUserIds);

        List<MemberCoinVo> threeLevelMemberCoinVos = memberCoinVos.stream().filter(item -> twoLevelAllUserIds.contains(item.getFromUserId())).collect(Collectors.toList());
        List<Integer> threeLevelUserIds = threeLevelMemberCoinVos.stream().map(MemberCoinVo::getId).collect(Collectors.toList());
//        List<Integer> threeLevelAllUserIds = getChildUserIds(twoLevelAllUserIds);



        //从不同级别的用户中获取收益
        List<Bonus> oneLevelBonuses = oneLevelMemberCoinVos.stream().map(itemMemberCoinVo -> {
            Bonus bonus = new Bonus();
            bonus.setFromUserId(itemMemberCoinVo.getId().longValue());
            bonus.setToUserId(memberCoinVo.getId().longValue());
            bonus.setMyAverageThreshold(memberCoinVoBalance);
            bonus.setLevelAverageThreshold(itemMemberCoinVo.getBanlance());
            bonus.setRightCoinId(bonusCoinId.longValue());
            bonus.setLeftCoinId(coinId.longValue());
            bonus.setThresholdNumber(itemMemberCoinVo.getBanlance());//分红时的持仓量
            bonus.setAverageThreshold(getMinBonus(memberCoinVoBalance, itemMemberCoinVo.getBanlance()));//分红基数（自己和下级选最小）
            bonus.setBonus(getUSDTFromBHB(bonus.getAverageThreshold().multiply(new BigDecimal(oneLevelBonus))));
            bonus.setHierarchy(1);//层级
            return bonus;
        }).collect(Collectors.toList());

        //二级收益
        List<Bonus> twoLevelBonuses = twoLevelMemberCoinVos.stream().map(itemMemberCoinVo -> {
            Bonus bonus = new Bonus();
            bonus.setFromUserId(itemMemberCoinVo.getId().longValue());
            bonus.setToUserId(memberCoinVo.getId().longValue());
            bonus.setMyAverageThreshold(memberCoinVoBalance);
            bonus.setLevelAverageThreshold(itemMemberCoinVo.getBanlance());
            bonus.setRightCoinId(bonusCoinId.longValue());
            bonus.setLeftCoinId(coinId.longValue());
            bonus.setThresholdNumber(itemMemberCoinVo.getBanlance());//分红时的持仓量
            bonus.setAverageThreshold(getMinBonus(memberCoinVoBalance, itemMemberCoinVo.getBanlance()));//分红基数（自己和下级选最小）
            bonus.setBonus(getUSDTFromBHB(bonus.getAverageThreshold().multiply(new BigDecimal(twoLevelBonus))));
            bonus.setHierarchy(2);//层级
            return bonus;
        }).collect(Collectors.toList());

        //三级收益
        List<Bonus> threeLevelBonuses = threeLevelMemberCoinVos.stream().map(itemMemberCoinVo -> {
            Bonus bonus = new Bonus();
            bonus.setFromUserId(itemMemberCoinVo.getId().longValue());
            bonus.setToUserId(memberCoinVo.getId().longValue());
            bonus.setMyAverageThreshold(memberCoinVoBalance);
            bonus.setLevelAverageThreshold(itemMemberCoinVo.getBanlance());
            bonus.setRightCoinId(bonusCoinId.longValue());
            bonus.setLeftCoinId(coinId.longValue());
            bonus.setThresholdNumber(itemMemberCoinVo.getBanlance());//分红时的持仓量
            bonus.setAverageThreshold(getMinBonus(memberCoinVoBalance, itemMemberCoinVo.getBanlance()));//分红基数（自己和下级选最小）
            bonus.setBonus(getUSDTFromBHB(bonus.getAverageThreshold().multiply(new BigDecimal(threeLevelBonus))));
            bonus.setHierarchy(3);//层级
            return bonus;
        }).collect(Collectors.toList());

        //自身持有收益
        Bonus bonus = new Bonus();
        bonus.setFromUserId(memberCoinVo.getId().longValue());
        bonus.setToUserId(memberCoinVo.getId().longValue());
        bonus.setMyAverageThreshold(memberCoinVoBalance);
        bonus.setLevelAverageThreshold(memberCoinVoBalance);
        bonus.setRightCoinId(bonusCoinId.longValue());
        bonus.setLeftCoinId(coinId.longValue());
        bonus.setThresholdNumber(memberCoinVo.getBanlance());//分红时的持仓量
        bonus.setAverageThreshold(memberCoinVo.getBanlance());
        bonus.setBonus(getUSDTFromBHB(memberCoinVoBalance.multiply(new BigDecimal(selfLevelBonus))));
        bonus.setHierarchy(0);//层级

        BigDecimal myBonus = bonus.getBonus().setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal oneLevelAllBonus = oneLevelBonuses.stream().map(Bonus::getBonus).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal twoLevelAllBonus = twoLevelBonuses.stream().map(Bonus::getBonus).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal threeLevelAllBonus = threeLevelBonuses.stream().map(Bonus::getBonus).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal allBonus = myBonus.add(oneLevelAllBonus).add(twoLevelAllBonus).add(threeLevelAllBonus).setScale(4, BigDecimal.ROUND_HALF_UP);


        //========处理分红，保存到数据库，添加用户余额=================
        executiveBonuses(password, oneLevelBonuses, twoLevelBonuses, threeLevelBonuses, bonus, memberCoinVo, bonusCoinId, allBonus);

        BonusLog bonusLog = new BonusLog();
        bonusLog.setUserId(memberCoinVo.getId());
        bonusLog.setBonus(allBonus);
        bonusLog.setCreateTime(new Date());
        bonusLog.setCurrQty(memberCoinVo.getBanlance());
        bonusLog.setMyBonus(myBonus);
        bonusLog.setOneBonus(oneLevelAllBonus);
        bonusLog.setTwoBonus(twoLevelAllBonus);
        bonusLog.setThreeBonus(threeLevelAllBonus);
        bonusLog.setOneCount(oneLevelBonuses.size());
        bonusLog.setTwoCount(twoLevelBonuses.size());
        bonusLog.setThreeCount(threeLevelBonuses.size());
        bonusLog.setOneUserIds(JsonHelper.writeValueAsString(oneLevelUserIds));
        bonusLog.setTwoUserIds(JsonHelper.writeValueAsString(twoLevelUserIds));
        bonusLog.setThreeUserIds(JsonHelper.writeValueAsString(threeLevelUserIds));
        bonusLogMapper.insert(bonusLog);

        log.info("用户id：{} - 处理分红成功，自身收益={}，一级收益={}，{}；二级收益={}，{}；三级收益={}，{}；总收益：{}"
                , memberCoinVo.getId(), myBonus.floatValue(), oneLevelAllBonus.floatValue(), oneLevelBonuses.size(), twoLevelAllBonus.floatValue(), twoLevelBonuses.size()
                , threeLevelAllBonus.floatValue(), threeLevelBonuses.size(),allBonus.doubleValue());
        return allBonus;
    }

    private List<Integer> getChildUserIds(List<Integer> userIds) {
        if (EmptyHelper.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return memberMapper.getIdsByFromUserIds(userIds);
    }

    /**执行分红*/
    private void executiveBonuses(String password, List<Bonus> oneLevelBonuses,
                                  List<Bonus> twoLevelBonuses, List<Bonus> threeLevelBonuses, Bonus bonus,
                                  MemberCoinVo memberCoinVo, Integer bonusCoinId, BigDecimal allBonus){
        if (PASSWORD.equals(password)){//防止误操作
            log.info("BHB分红，插入分红数据：{} - 总收益个数：{}", memberCoinVo.getId(), allBonus);
            //批量插入

            if (!EmptyHelper.isEmpty(threeLevelBonuses)) {
                bonusMapper.batchInsert(threeLevelBonuses);
            }
            if (!EmptyHelper.isEmpty(twoLevelBonuses)) {
                bonusMapper.batchInsert(twoLevelBonuses);
            }
            if (!EmptyHelper.isEmpty(oneLevelBonuses)) {
                bonusMapper.batchInsert(oneLevelBonuses);
            }

            bonusMapper.insert(bonus);

            //新增用户收益
            UserCoin userCoin = userCoinMapper.selectByUserIdAndCoinId(memberCoinVo.getId(), bonusCoinId);
            if (userCoin == null) {
                UserCoin entity = UserCoin.newInstance(memberCoinVo.getId(), bonusCoinId);
                userCoinMapper.insertSelective(entity);
                userCoin = userCoinMapper.selectByUserIdAndCoinId(memberCoinVo.getId(), bonusCoinId);
            }

            int addSuccess = userCoinMapper.addBanlance(allBonus, bonusCoinId, memberCoinVo.getId(), userCoin.getVersion());
            if (addSuccess <= 0){
                throw new RuntimeException("分红，添加用户资金出错");
            }
            userCoinDetailService.addUserCoinDetail(userCoin, "", UserCoinDetailType.BONUS);
        }
    }

    //BHB个数按比例转化为usdt
    public BigDecimal getUSDTFromBHB(BigDecimal BHBQty) {
        if (BHBUSDTratio == null) {
            BHBUSDTratio = BigDecimal.ONE;
        }
        BHBUSDTratio = BHBUSDTratio.setScale(8, BigDecimal.ROUND_HALF_UP);
        BHBQty = BHBQty.setScale(8, BigDecimal.ROUND_HALF_UP);
        return BHBQty.multiply(BHBUSDTratio).setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    //获取用户的平均持有量
    @Deprecated
    public BigDecimal getAverageThreshold(MemberCoinVo memberCoinVo, Integer coinId, Date startTime, Date endTime) {
        BigDecimal avgThreshold = userCoinDetailMapper.getAverageThreshold(memberCoinVo.getId(), coinId, startTime, endTime);
        if (avgThreshold == null) {//没有交易记录
            return memberCoinVo.getBanlance();
        }
        return avgThreshold;
    }

    /**取最小的一个*/
    public BigDecimal getMinBonus(BigDecimal myBonus, BigDecimal levelBonus) {
//        if (myBonus.compareTo(levelBonus) >=0){
        return levelBonus;
//        }
//        return myBonus;
    }
}