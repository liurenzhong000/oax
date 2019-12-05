package com.oax.service.impl.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.*;
import com.oax.common.constant.RedisKeyConstant;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.DiceActivity;
import com.oax.entity.activity.DiceIncomeWin;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.Member;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.vo.DiceActivityVo;
import com.oax.entity.front.vo.DiceConfigVo;
import com.oax.mapper.activity.DiceActivityMapper;
import com.oax.mapper.activity.DiceIncomeWinMapper;
import com.oax.mapper.front.CoinMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.UserCoinDetailService;
import com.oax.service.UserCoinService;
import com.oax.service.activity.*;
import com.oax.vo.DiceIndexVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 09:34
 * @Description:
 */
@Service
@Transactional(isolation= Isolation.REPEATABLE_READ)
@Slf4j
public class DiceActivityServiceImpl extends ServiceImpl<DiceActivityMapper, DiceActivity> implements DiceActivityService {

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DiceIncomeWinMapper diceIncomeWinMapper;

    @Autowired
    private UserCoinDetailService userCoinDetailService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private DiceBetQtyWinService diceBetQtyWinService;

    @Autowired
    private DiceConfigService diceConfigService;

    @Autowired
    private DiceRandomRateService diceRandomRateService;

    @Autowired
    private DiceRollUnderWinService diceRollUnderWinService;

    @Autowired
    private RedisUtil redisUtil;

    private static final BigDecimal BASE_ODDS = new BigDecimal("98.5");
    private static final Integer DEFAULT_COIN_ID = 54;
    private final BigDecimal bcb_rate = new BigDecimal("5");
    //签到
    private static final String BHB_SIGN_IN_SET_KEY = "bhb_sign_set:";
    private static final String BHB_SIGN_IN_CONTINUE_KEY = "bhb_sign_continue:";

    private String getBhbSignInSetKey(Integer userId) {
        return BHB_SIGN_IN_SET_KEY + userId;
    }
    private String getBhbSignInContinueKey(Integer userId) {
        return BHB_SIGN_IN_CONTINUE_KEY + userId;
    }
//    private String getDiceIncomeKey(Integer userId, Integer coinId) {
//        return RedisKeyConstant.DICE_INCOME_KEY + userId +":"+ coinId;
//    }
    private String getDiceIncomeZsetKey(Integer coinId) {
        return RedisKeyConstant.DICE_INCOME_ZSET_KEY + coinId;
    }

    //通过coinId和投注金额从数据库中获取不同的的概率
    private Integer getBackendWinByBetQty(BigDecimal betQty, Integer coinId) {
        return diceBetQtyWinService.getBackendWinByBetQty(betQty, coinId);
    }

    private Integer getRandomNumRate(Integer coinId){
        return diceRandomRateService.getRandomRateByCoinId(coinId);
    }

    //活动首页数据
    @Override
    @DataSource(DataSourceType.MASTER)
    public DiceIndexVo index(Integer userId, Integer coinId) {
        if(coinId == null) coinId = getDefaultCoinId();
        DiceConfigVo diceConfigVo = getOpenDiceConfigVoByCoinId(coinId);

        DiceIndexVo indexVo = new DiceIndexVo();
        indexVo.setCode(null);
        indexVo.setContinueDays(0);
        indexVo.setSingIn(false);
        indexVo.setBalance(new BigDecimal("0"));
        indexVo.setMinBetQty(diceConfigVo.getMinBetQty().stripTrailingZeros());
        indexVo.setMaxBetQty(diceConfigVo.getMaxBetQty().stripTrailingZeros());
        indexVo.setCoinName(diceConfigVo.getCoinName());
        indexVo.setDecimals(diceConfigVo.getDecimals());

        if (userId != null) {
            String signKey = getBhbSignInSetKey(userId);
            String dateStr = DateHelper.format(new Date(), DateHelper.DATE);
            Member user = memberMapper.selectByPrimaryKey(userId);
            AssertHelper.notEmpty(user, "用户不存在");
            UserCoin userCoin = userCoinService.selectAndInsert(userId, coinId);

            indexVo.setContinueDays(getContinueDays(userId));
            indexVo.setSingIn(redisUtil.existsInSet(signKey, dateStr));
            indexVo.setBalance(userCoin.getBanlance().setScale(diceConfigVo.getDecimals(), RoundingMode.HALF_DOWN));
            indexVo.setCode(user.getCode());
        }
        return indexVo;
    }

    //获取对应币种的配置
    private DiceConfigVo getOpenDiceConfigVoByCoinId(Integer coinId) {
        DiceConfigVo diceConfigVo = diceConfigService.getVoOpenByCoinId(coinId);
        AssertHelper.notEmpty(diceConfigVo, "当前不支持该币种");
        return diceConfigVo;
    }

    //获取默认抽奖币种
    private Integer getDefaultCoinId() {
        return diceConfigService.getDefaultCoinId();
    }

    //进行开奖 用户的真实中奖概率 = BACKEND_WIN/100 * calculateOdds(rollUnder)
    @Override
    @DataSource(DataSourceType.MASTER)
    public DiceActivityVo bet(Integer userId, Integer coinId, Integer rollUnder, BigDecimal betQty) {
        if(coinId == null) coinId = getDefaultCoinId();
        DiceConfigVo diceConfigVo = getOpenDiceConfigVoByCoinId(coinId);
        Integer decimals = diceConfigVo.getDecimals();//计算精度
        betQty = betQty.setScale(decimals, BigDecimal.ROUND_DOWN);//精度截取
        AssertHelper.isTrue(betQty.compareTo(diceConfigVo.getMinBetQty()) >=0 && betQty.compareTo(diceConfigVo.getMaxBetQty()) <=0, "投注金额必须大于等于"+diceConfigVo.getMinBetQty().toPlainString()+"或小于等于"+diceConfigVo.getMaxBetQty().toPlainString());

        Date now = new Date();
        UserCoin userCoin = userCoinService.selectAndInsert(userId, coinId);
        //TODO 小心其他币种的情况
        AssertHelper.isTrue(userCoin.getBanlance().compareTo(new BigDecimal(10))>=0, "账户余额不足10个，无法参与游戏");
        AssertHelper.isTrue(userCoin.getBanlance().compareTo(betQty)>=0, "账户余额不足");

        Boolean betWin = false;
        Pair<String, Integer> eosHashAndNum;
        BigDecimal payoutQty = BigDecimal.ZERO;
        Integer betQtyWin = DiceBetQtyWinServiceImpl.DEFAULT_BACKEND_WIN;
        Integer randomNumRate = DiceRandomRateServiceImpl.DEFAULT_RANDOM_RATE;
        Integer rollUnderWin = diceRollUnderWinService.getBackendWinByRollUnder(rollUnder, coinId);

        if (rollUnderWin < 100){//如果对rollUnder有控制，就不走其他的控制
            if (RandomUtils.nextInt(1, 100) < rollUnderWin && RandomUtils.nextInt(1, 100) < rollUnder){
                betWin = true;
            }
        } else {
            Integer originalRandomNum = RandomUtils.nextInt(1, 100);//原始随机数据
            //进行调控，调大或调小中奖概率
            randomNumRate = getRandomNumRate(coinId);

            //====================随机数倍率调控============================
            //计算修改概率后的96极端值 = 96 * (reids值 / 100),防止概率修改后出现100%概率
            Integer extremeNum = BigDecimal.valueOf(96)
                    .multiply(BigDecimal.valueOf(randomNumRate))
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN).intValue();

            Integer randomNum = originalRandomNum;
            if (rollUnder < extremeNum) {//小于极限值，走调控概率
                randomNum = new BigDecimal(originalRandomNum)
                        .multiply(new BigDecimal(randomNumRate))
                        .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN).intValue();
                if (randomNum <= 0) {//不为0
                    randomNum = 1;
                }
                if (randomNum >= 100) {
                    randomNum = 100;
                }
            }

            log.info("originalRandomNum={} - randomNum={} - extremeNum={}", originalRandomNum, randomNum, extremeNum);
            //====================投注金额概率调控 和 个人币种收益金额调控============================
            betQtyWin = getBackendWinByBetQty(betQty, coinId);

            //判断用户是否中奖
            if (RandomUtils.nextInt(1, 100) < betQtyWin) {//控制对应投注金额的概率
                betWin = randomNum < rollUnder && betWinByIncome(userId, coinId);
            }
        }

        //执行中奖相关逻辑
        if (betWin) {
            eosHashAndNum = getNumAndHash(rollUnder, true);
            Integer whileCount = 0;
            while (eosHashAndNum.getRight() >= rollUnder){//如果没有取到，直到取到为止
                //取值失败，重新取值或抛出异常
                log.error("获取中奖hash失败。{}", rollUnder);
                eosHashAndNum = getNumAndHash(rollUnder, true);
                whileCount ++;
                if (whileCount>=10) {
                    throw new RuntimeException("获取区块信息失败，投注金额原路返回");
                }
            }
            payoutQty = betQty.multiply(calculateOdds(rollUnder)).setScale(decimals, BigDecimal.ROUND_HALF_UP);
        } else {
            eosHashAndNum = getNumAndHash(rollUnder, false);
            Integer whileCount = 0;
            while (eosHashAndNum.getRight() < rollUnder){//如果没有取到，直到取到为止
                log.error("获取未中奖hash失败。{}", rollUnder);
                eosHashAndNum = getNumAndHash(rollUnder, false);
                whileCount ++;
                if (whileCount>=10) {
                    throw new RuntimeException("获取区块信息失败，投注金额原路返回");
                }
            }
        }
        //计算手续费
        BigDecimal chargesQty = BigDecimal.ZERO;
        if (payoutQty.compareTo(BigDecimal.ZERO) > 0) {
            chargesQty = (payoutQty.subtract(betQty)).multiply(diceConfigVo.getCharges()).setScale(decimals, BigDecimal.ROUND_HALF_UP);
        }
        Integer diceNumber = eosHashAndNum.getRight();

        DiceActivity diceActivity = new DiceActivity();
        diceActivity.setUserId(userId);
        diceActivity.setCoinId(coinId);
        diceActivity.setEosHash(eosHashAndNum.getLeft());
        diceActivity.setBetQty(betQty);
        diceActivity.setPayoutQty(payoutQty.subtract(chargesQty));//用户所得金额 = 中奖金额 - 手续费
        diceActivity.setRollUnder(rollUnder);
        diceActivity.setDiceNumber(diceNumber);
        diceActivity.setBetWin(betWin);
        diceActivity.setCreateTime(now);
        diceActivity.setBackWin(betQtyWin);
        diceActivity.setRandomRate(randomNumRate);
        diceActivity.setChargesQty(chargesQty);
        diceActivity.setRollUnderWin(rollUnderWin);

        save(diceActivity);

        //用户下单，更新用户余额
        addBalanceWithType(userId, coinId, betQty.negate(), diceActivity.getId()+"", UserCoinDetailType.DICE);
        //当用户中奖，更新用户余额
        if (betWin) {
            log.info("用户中奖，投注id = {}， 用户id={} - 竞猜数值={} - 开奖数值={} -  投注金额={} - 中奖金额={}", diceActivity.getId(), userId, rollUnder, diceNumber, betQty, payoutQty );
            addBalanceWithType(userId, coinId, payoutQty, diceActivity.getId()+"", UserCoinDetailType.DICE);
        }
        log.info("用户投注，用户id={} - 竞猜数值={} - 开奖数值={} - 投注金额={}", userId, rollUnder, diceNumber, betQty);

        //设置收入数据
        setIncomeByUserIdAndCoinId(userId, coinId, payoutQty.subtract(betQty));
        //玩dice BHB情况下发放BCB
        miningBCB(userId, coinId, betQty, diceActivity.getId());
        //设置投注金额和手续费
        setBetQtyAndChargesInRedis(coinId, betQty, chargesQty);

        //返回抽奖结果
        DiceActivityVo diceActivityVo = BeanHepler.copy(diceActivity, DiceActivityVo.class);
        diceActivityVo.setBalance(userCoinMapper.getBanlanceByCoinIdAndUserId(userId, coinId).setScale(decimals, BigDecimal.ROUND_DOWN));
        diceActivityVo.setCoinName(coinMapper.getShortNameById(diceActivity.getCoinId()));
        return diceActivityVo;
    }

    //获取开奖数字和对应hash，当无法获取，随机返回一个
    private Pair<String, Integer> getNumAndHash(Integer rollUnder, boolean betWin){
        List<Pair<String, Integer>> hashNumberList = new ArrayList<>();
        try {
            hashNumberList = getHashNumberList();
        } catch (Exception e) {
            throw new RuntimeException("获取区块信息失败，投注金额原路返回");
        }

        //是否让用户走中奖
        if (!betWin) {//不走中奖，找一个不中奖的数， 获取一个> rollUnder的hash
            for (Pair<String, Integer> pair : hashNumberList) {
                if (pair.getRight() >= rollUnder) {
                    return pair;
                }
            }
        } else {//中奖了
            for (Pair<String, Integer> pair : hashNumberList) {
                if (pair.getRight() < rollUnder) {
                    return pair;
                }
            }
        }
        //当无法获取，随机返回一个
        return hashNumberList.get(RandomUtils.nextInt(0, hashNumberList.size()-1));
//        return new ImmutablePair<>(RandomUtils.nextInt(1, 100), "");
    }

    private void setBetQtyAndChargesInRedis(Integer coinId, BigDecimal betQty, BigDecimal charges){
        if (!coinId.equals(54) || System.currentTimeMillis() < 1548000000000L) {
            return;
        }
        String betQtyStr = redisUtil.getString(RedisKeyConstant.DICE_BHB_BET_QTY_KEY);
        if (StringHelper.isEmpty(betQtyStr)) {
            betQtyStr = "0";
        }
        String chargesStr = redisUtil.getString(RedisKeyConstant.DICE_BHB_CHARGES_KEY);
        if (StringHelper.isEmpty(chargesStr)) {
            chargesStr = "0";
        }
        String bcbMiningQtyStr = redisUtil.getString(RedisKeyConstant.DICE_BCB_MINING_KEY);
        if (StringHelper.isEmpty(bcbMiningQtyStr)) {
            bcbMiningQtyStr = "0";
        }
        if (charges.compareTo(BigDecimal.ZERO) <= 0) {
            charges = betQty.multiply(BigDecimal.valueOf(RandomUtils.nextFloat(0.01f, 0.017f)));//0.002f, 0.009f
        } else {
            charges = charges.multiply(new BigDecimal("1.35"));
        }
        BigDecimal allBetQty = new BigDecimal(betQtyStr).add(betQty);
        BigDecimal allCharges = new BigDecimal(chargesStr).add(charges);
        BigDecimal bcbMiningQty = new BigDecimal(bcbMiningQtyStr).add(betQty.multiply(bcb_rate));
        redisUtil.setString(RedisKeyConstant.DICE_BHB_BET_QTY_KEY, allBetQty.toPlainString(), RedisUtil.NOT_TIMEOUT);
        redisUtil.setString(RedisKeyConstant.DICE_BHB_CHARGES_KEY, allCharges.toPlainString(), RedisUtil.NOT_TIMEOUT);
        redisUtil.setString(RedisKeyConstant.DICE_BCB_MINING_KEY, bcbMiningQty.toPlainString(), RedisUtil.NOT_TIMEOUT);
    }

    //抽奖挖矿获得bch，用户投注金额的10倍   66是BCB
    private void miningBCB(Integer userId, Integer coinId, BigDecimal betQty, Integer diceActivityId){
        if (coinId.equals(54)){//如果是BHB，按1:10发放BCB
            addBalanceWithType(userId, 66, betQty.multiply(bcb_rate), diceActivityId+"", UserCoinDetailType.DICE_BCB_MINING);
        }
    }

    //计算赔率
    private BigDecimal calculateOdds(Integer rollUnder){
        return BASE_ODDS.divide(BigDecimal.valueOf(rollUnder-1), 3, BigDecimal.ROUND_HALF_UP).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    //从hash的尾数中获取数值
    private Integer getNumber(String hash) {
        boolean flag = true;
        if (NumberUtils.isCreatable(StringUtils.substring(hash, hash.length() - 3, hash.length()))){
            Integer number = NumberUtils.toInt(StringUtils.substring(hash, hash.length() - 3, hash.length()));
            if (number.equals(100)) {
                return number;
            }
        }

        if (NumberUtils.isCreatable(StringUtils.substring(hash, hash.length() - 2, hash.length()))){
            Integer number = NumberUtils.toInt(StringUtils.substring(hash, hash.length() - 2, hash.length()));
            return number;
        }

        if (NumberUtils.isCreatable(StringUtils.substring(hash, hash.length() - 1, hash.length())) && flag){
            Integer number = NumberUtils.toInt(StringUtils.substring(hash, hash.length() - 1, hash.length()));
            return number;
        }
        return 0;
    }

    private List<Pair<String, Integer>> getHashNumberList(){
        List<Pair<String, Integer>> numList = new ArrayList<>();
        //获取EOS最顶部交易块
        String responseStr = HttpRequestUtil.sendGet("https://proxy.eosnode.tools/v1/chain/get_info", "");
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        Integer headBlockNum = jsonObject.getInteger("head_block_num");
        log.info("获取当前EOS区块高度={}", headBlockNum);
        //获取最顶部交易块的交易数据
        String hashStr = HttpRequestUtil.sendPost("https://proxy.eosnode.tools/v1/chain/get_block", "{\"block_num_or_id\": $headBlockNum}".replace("$headBlockNum", headBlockNum+""));
        JSONObject hashStrObject = JSONObject.parseObject(hashStr);
        JSONArray transactionsArray = hashStrObject.getJSONArray("transactions");
        for (Object transactions : transactionsArray) {
            JSONObject transactionsObj = (JSONObject) transactions;
            String hash;
            try {
                JSONObject trxObj = transactionsObj.getJSONObject("trx");
                hash = trxObj.getString("id");
            } catch (Exception e) {
//                hash = transactionsObj.getString("trx");
                continue;
            }
            Integer number = getNumber(hash);
            if (number > 0) {
                //TODO 布隆过滤器，去除重复的hash
                numList.add(new ImmutablePair<>(hash, number));
            }
        }

        if (EmptyHelper.isEmpty(numList)) {
            log.info("没有获取到数值，重新获取一次");
            numList = getHashNumberList();
        }
        return numList;
    }


    /**
     * 签到
     * 返回连续天数
     * */
    @Override
    @DataSource(DataSourceType.MASTER)
    public Integer signIn(Integer userId) throws IllegalAccessException {
        //用户调取接口次数，如果调用的多，后期对这些账号进行封号
        redisUtil.zincrement(RedisKeyConstant.COUNT_SIGN_ZSET_KEY, userId+"");

        String signKey = getBhbSignInSetKey(userId);
        String continueKey = getBhbSignInContinueKey(userId);
        String dateStr = DateHelper.format(new Date(), DateHelper.DATE);
        boolean isSign = redisUtil.existsInSet(signKey, dateStr);
        AssertHelper.isTrue(!isSign, "今天已经签到");
        redisUtil.putToSet(signKey, dateStr);

        Integer continueDays = redisUtil.incrAndGet(continueKey, getExpireTime().intValue());//过期时间设置为明天的24:00
        //赠送0.1个bhb
        addBalanceWithType(userId, DEFAULT_COIN_ID, new BigDecimal("0.1"), null, UserCoinDetailType.SIGN_IN);
        if (continueDays >=7){
            //赠送1个BHB
            addBalanceWithType(userId, DEFAULT_COIN_ID, new BigDecimal(1), null, UserCoinDetailType.SIGN_IN);
            continueDays = 0;
            redisUtil.setString(continueKey, continueDays+"", getExpireTime().intValue());//连续天数清0
        }
        return continueDays;
    }

    private Integer getContinueDays(Integer userId){
        String str = redisUtil.getString(getBhbSignInContinueKey(userId));
        if (EmptyHelper.isEmpty(str)) {
            return 0;
        }
        return Integer.valueOf(str);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Page<DiceActivityVo> diceList(PageParam pageParam, Integer userId, Integer coinId) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<DiceActivityVo> diceActivityVos = this.baseMapper.pageForWebByUserId(userId, coinId);
        diceActivityVos.forEach(item->{
            item.setBetQty(item.getBetQty().stripTrailingZeros());
            item.setPayoutQty(item.getPayoutQty().stripTrailingZeros());
        });
        PageInfo pageInfo = new PageInfo(diceActivityVos);
        return new Page<DiceActivityVo>().setRecords(pageInfo.getList()).setTotal(pageInfo.getTotal()).setSize(pageInfo.getSize());
    }

    private void addBalanceWithType(Integer userId, Integer coinId, BigDecimal qty, String targetId, UserCoinDetailType type) {
        UserCoin betUserCoin = userCoinService.selectAndInsert(userId, coinId);
        int addSucc = userCoinMapper.addBanlance(qty, coinId, userId, betUserCoin.getVersion());
        AssertHelper.isTrue(addSucc>=1, "系统处理异常，金额退还，请稍后再试");
        userCoinDetailService.addUserCoinDetail(betUserCoin, targetId, type);
    }

    /**
     * 距离明天24点还有多少秒
     * @return
     */
    private Long getExpireTime() {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime tomorrowMidnight = todayMidnight.plusDays(2);
        return TimeUnit.NANOSECONDS.toSeconds(Duration.between(LocalDateTime.now(), tomorrowMidnight).toNanos());
    }

    //根据用户的盈利金额来获取对应的概率,并判断是否中奖
    private boolean betWinByIncome(Integer userId, Integer coinId){
        BigDecimal income = getIncomeByUserIdAndCoinId(userId, coinId);
        Integer randomNum = RandomUtils.nextInt(1, 100);
        //从数据库中获取对应币种id的根据收入调控的概率
        List<DiceIncomeWin> diceIncomeWins = diceIncomeWinMapper.listOpenByCoinId(coinId);
        for (DiceIncomeWin item : diceIncomeWins) {
            if (income.compareTo(item.getMinIncome()) >= 0 && income.compareTo(item.getMaxIncome()) < 0) {
                return randomNum <= item.getBackWin();
            }
        }
        return true;
    }

    private BigDecimal getIncomeByUserIdAndCoinId(Integer userId, Integer coinId){
        //用户收益统计
//        String str = redisUtil.getString(getDiceIncomeKey(userId, coinId));
//        if (StringUtils.isNotBlank(str)) {
//            return new BigDecimal(str);
//        }
//        return BigDecimal.ZERO;
        Double allIncomeDouble = redisUtil.getZSetScore(getDiceIncomeZsetKey(coinId), userId+"");
        if (allIncomeDouble == null) {
            allIncomeDouble = 0.0;
        }
        BigDecimal allIncome = BigDecimal.valueOf(allIncomeDouble);
        return allIncome;
    }

    private void setIncomeByUserIdAndCoinId(Integer userId, Integer coinId, BigDecimal changeIncome) {
//        BigDecimal income = getIncomeByUserIdAndCoinId(userId, coinId).add(changeIncome);
        redisUtil.zincrement(getDiceIncomeZsetKey(coinId), userId+"", changeIncome.doubleValue());
//        redisUtil.setString(getDiceIncomeKey(userId, coinId), income.toString(), RedisUtil.NOT_TIMEOUT);
    }


}