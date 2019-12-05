package com.oax.service.impl.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.oax.common.*;
import com.oax.common.constant.RedisKeyConstant;
import com.oax.common.json.JsonHelper;
import com.oax.common.util.string.VerificationHelper;
import com.oax.common.util.thread.ThreadManager;
import com.oax.constant.WebSocketKeyConstant;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.SnatchActivity;
import com.oax.entity.activity.SnatchConfig;
import com.oax.entity.activity.SnatchDetail;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.vo.*;
import com.oax.form.SnatchAggregateForm;
import com.oax.mapper.activity.SnatchActivityMapper;
import com.oax.mapper.front.CoinMapper;
import com.oax.service.UserCoinService;
import com.oax.service.UserService;
import com.oax.service.activity.SnatchActivityService;
import com.oax.service.activity.SnatchConfigService;
import com.oax.service.activity.SnatchDetailService;
import com.oax.service.delay.GetHashNumberListCallable;
import com.oax.service.delay.SnatchRobotQueue;
import com.oax.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 19:20
 * @Description:
 */
@Service
@Transactional(isolation= Isolation.REPEATABLE_READ)
@Slf4j
public class SnatchActivityServiceImpl implements SnatchActivityService {

    @Autowired
    private SnatchActivityMapper snatchActivityMapper;

    @Autowired
    private SnatchDetailService snatchDetailService;

    @Autowired
    private SnatchConfigService snatchConfigService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private RedisUtil redisUtil;

    //手续费
    private final BigDecimal charges = new BigDecimal("0.02");

    //BHB产出BCB个数
    private final BigDecimal bcb_rate = new BigDecimal(5);

    public static final Integer ROBOT_USER_ID = 2;

    //一元夺宝首页数据
    @Override
    @DataSource(DataSourceType.MASTER)
    public SnatchActivityHomeVo index(Integer coinId, Integer userId){
        //如果配置开启，但activity没有，生成activity记录
        List<SnatchConfig> noActivityConfigs = snatchConfigService.listNoActivity();
        noActivityConfigs.forEach(item -> addActivityByConfig(item));

        //设置默认币种
        if (coinId == null) {
            coinId = snatchConfigService.defaultCoinId();
        }
        SnatchActivityHomeVo homeVo = new SnatchActivityHomeVo();
        homeVo.setBalance(BigDecimal.ZERO);
        if (userId != null){
            homeVo.setBalance(userCoinService.selectAndInsert(userId, coinId).getBanlance());
        }
        homeVo.setSnatchActivityVos(listActivity(coinId, userId));
        homeVo.setSnatchCoinVos(snatchConfigService.listConfigCoin());
        return homeVo;
    }

    //获取已经开启的活动池
    public List<SnatchActivityVo> listActivity(Integer coinId, Integer userId){
        List<SnatchActivityVo> activityVos = snatchActivityMapper.listActivityByCoinId(coinId);
        activityVos.forEach(item -> {
//            item.setBettedUnits(snatchDetailService.getBettedUnits(userId, item.getId()));
            item.setUnitPayoutQty(calculatePayoutQty(item.getUnit(), item.getQuantity(), item.getWinNumber()));
        });
        return activityVos;
    }

    private BigDecimal calculatePayoutQty(BigDecimal unit, Integer quantity, Integer winNumber){
        return unit.multiply(BigDecimal.valueOf(quantity)).divide(BigDecimal.valueOf(winNumber).setScale(unit.scale(), BigDecimal.ROUND_DOWN));
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void robotBet(Integer id) {
        Integer robotUserId = 2;
        SnatchActivity snatchActivity = snatchActivityMapper.selectById(id);
        /**
         * 当前机器人小于要添加的机器人数
         * 机器人投注概率
         * 不投最后一单
         */
        if (snatchActivity != null && snatchActivity.getCurrRobotWinCount() < snatchActivity.getRobotWinCount()
                                 && RandomUtils.nextInt(1, 100) < snatchActivity.getRobotBackWin()
                                 && snatchActivity.getStatus().equals(SnatchActivity.Status.RUNNING.ordinal())) {
            //随机投注个数
            BigDecimal betQty = snatchActivity.getUnit();
            if (snatchActivity.getQuantity() - snatchActivity.getFinishQuantity() <= betQty.divide(snatchActivity.getUnit()).intValue()){//机器人不参与开奖，不好保证事务
                return;
            }
            bet(id, robotUserId, betQty, true);
        }
    }

    //用户下注，返回余额，我的已投注数量
    @Override
    @DataSource(DataSourceType.MASTER)
    public Map<String, Object> bet(Integer id, Integer userId, BigDecimal betQty, Boolean robot){

        AssertHelper.isTrue(betQty.compareTo(BigDecimal.ZERO)>0, "投注数要大于0");
        //获取相关数据
        SnatchActivity snatchActivity = snatchActivityMapper.selectById(id);
        AssertHelper.isTrue(betQty.divideAndRemainder(snatchActivity.getUnit())[1].floatValue() ==0 , "投注数量错误，请投注正确的数量");
        AssertHelper.notEmpty(snatchActivity, "奖池不存在");
        AssertHelper.isTrue(snatchActivity.getStatus() == SnatchActivity.Status.RUNNING.ordinal(), "当前期数已结束");
        //投注的数量
        Integer time = betQty.divide(snatchActivity.getUnit()).intValue();
        AssertHelper.isTrue(time + snatchActivity.getFinishQuantity() <= snatchActivity.getQuantity(), "可投注数量不足");
        BigDecimal isBettedQty = snatchDetailService.getBettedQty(userId, id);
        if (!robot){//机器人无投注个数限制
            AssertHelper.isTrue(isBettedQty.add(betQty).compareTo(snatchActivity.getUnit().multiply(BigDecimal.valueOf(snatchActivity.getMaxQuantity()))) <= 0, "超过最大投注金额");
        }

        UserCoin userCoin = userCoinService.selectAndInsert(userId, snatchActivity.getCoinId());
        AssertHelper.isTrue(userCoin.getBanlance().compareTo(new BigDecimal("10")) >= 0, "余额不足10个，无法参与游戏");
        AssertHelper.isTrue(userCoin.getBanlance().compareTo(betQty) >= 0, "余额不足");

        //获取乐观锁
        int lock = snatchActivityMapper.updateById(snatchActivity);
        AssertHelper.isTrue(lock>0, "下注失败，请重试");

        Date createTime = new Date();
        //添加投注记录
        String firstNumberStr = saveDetail(snatchActivity, userId, time, createTime, robot);
        //计数增加
        snatchActivity.setFinishQuantity(snatchActivity.getFinishQuantity() + time);
        //按一定的策略添加机器人
        if (!robot){
            SnatchRobotQueue.put(id, this);
        } else {
            //已经添加机器人数+1
            snatchActivity.setCurrRobotWinCount(snatchActivity.getCurrRobotWinCount() + 1);
        }
        snatchActivityMapper.updateById(snatchActivity);


        //2.更新账户余额
        userCoinService.addBalanceWithType(userId, snatchActivity.getCoinId(), betQty.negate(), snatchActivity.getId()+"", UserCoinDetailType.SNATCH);
        //挖矿
        miningBCB(userId, snatchActivity.getCoinId(), betQty, snatchActivity.getId());


        //3.如果期数刚好满了，进行开奖（更新3个表的相关数据），并根据配置开启下一期
        snatchLottery(snatchActivity);

        //多线程异步发送socket消息
        syncSendWebSocket(userId, createTime, snatchActivity, betQty, firstNumberStr, time);

        log.info("夺宝游戏投注成功 id={} - bet_qty={} - user_id={} - robot={}", id, betQty, userId, robot);
        Map<String, Object> betMap = new HashMap<>();
        betMap.put("balance", userCoinService.selectAndInsert(userId, snatchActivity.getCoinId()).getBanlance());
        List<BettedVo> bettedVos = listActivity(snatchActivity.getCoinId(), userId)
                .stream()
                .map(item ->new BettedVo(item.getId(), snatchDetailService.getBettedUnits(userId, item.getId()))).collect(Collectors.toList());
        betMap.put("bettedUnitsList", bettedVos);
        return betMap;
    }

    //抽奖挖矿获得bch，用户投注金额的10倍   66是BCB
    private void miningBCB(Integer userId, Integer coinId, BigDecimal betQty, Integer diceActivityId){
        if (coinId.equals(54)){//如果是BHB，按1:10发放BCB
            userCoinService.addBalanceWithType(userId, 66, betQty.multiply(bcb_rate), diceActivityId+"", UserCoinDetailType.SNATCH_BCB_MINING);
        }
    }

    //多线程发送用户的投注信息
    private void syncSendWebSocket(Integer userId, Date createTime, SnatchActivity snatchActivity, BigDecimal betQty, String numberStr, Integer time){
        //从数据库获取phoneOrEmail 和 coinName
        String phoneOrEmail = userService.getPhoneOrEmailById(userId);
        phoneOrEmail = VerificationHelper.hidePhoneAndEmail(phoneOrEmail);
        if (userId.equals(ROBOT_USER_ID)) {
            phoneOrEmail = VerificationHelper.hidePhoneAndEmail(VerificationHelper.getRandomPhone());
        }
        String coinName = coinMapper.getShortNameById(snatchActivity.getCoinId());
        SnatchBetMsgVo msgVo = new SnatchBetMsgVo();
        msgVo.setCreateTime(createTime);
        msgVo.setPhoneOrEmail(phoneOrEmail);
        msgVo.setCoinName(coinName);
        msgVo.setOrdinal(snatchActivity.getOrdinal());
        msgVo.setConfigName(snatchActivity.getConfigName());
        msgVo.setBetQty(betQty);
        msgVo.setNumberStr(numberStr);
        msgVo.setBetTime(time);
        msgVo.setConfigName(snatchActivity.getConfigName());

        List<SnatchActivityVo> activityVos = snatchActivityMapper.listActivityByCoinId(snatchActivity.getCoinId());
        activityVos.forEach(item -> {
            item.setUnitPayoutQty(calculatePayoutQty(item.getUnit(), item.getQuantity(), item.getWinNumber()));
        });
//        List<Map<String, Object>> activityMaps = BeanHepler.copyListToMapList(activityVos);
//        activityMaps.forEach(item -> item.remove("bettedUnits"));

//        SnatchActivityVo snatchActivityVo = BeanHepler.copy(snatchActivity, SnatchActivityVo.class);
//        snatchActivityVo.setUnitPayoutQty(calculatePayoutQty(snatchActivity.getUnit(), snatchActivity.getFinishQuantity(), snatchActivity.getWinNumber()));

        //redis订阅发布实现 调用另外一台服务器发送同样的消息，确保连接的两台服务器的用户都能收到
        ThreadManager.getThreadPollProxy().execute(()->{
            redisUtil.getStringRedisTemplate().convertAndSend(WebSocketKeyConstant.SNATCH_BET_MSG, JsonHelper.writeValueAsString(msgVo));
            redisUtil.getStringRedisTemplate().convertAndSend(WebSocketKeyConstant.SNATCH_ACTIVITY_MSG, JsonHelper.writeValueAsString(activityVos));
        });
    }


    //如果投注人数达标，进行开奖操作
    private void snatchLottery(SnatchActivity snatchActivity) {
        if (snatchActivity.getFinishQuantity() < snatchActivity.getQuantity()) {
            return;
        }
        snatchActivity.setLotteryTime(new Date());
        snatchActivity.setStatus(SnatchActivity.Status.FINISH.ordinal());
        snatchActivityMapper.updateById(snatchActivity);

        BigDecimal payoutQty = calculatePayoutQty(snatchActivity.getUnit(), snatchActivity.getQuantity(), snatchActivity.getWinNumber());
        //计算手续费
        BigDecimal chargesQty = BigDecimal.ZERO;
        if (payoutQty.compareTo(BigDecimal.ZERO) > 0) {
            chargesQty = (payoutQty.multiply(charges).setScale(snatchActivity.getUnit().scale(), BigDecimal.ROUND_HALF_UP));
        }
        //批量更新为未中奖
        int updateAllNotWin = snatchDetailService.updateAllNotWin(snatchActivity.getId());
        AssertHelper.isTrue(updateAllNotWin > 0, "区块开奖失败.detail");

        //获取机器人的投注number
        List<Integer> robotNumbers = snatchDetailService.listRobotNumber(snatchActivity.getId());
        List<Pair<Integer, String>> winHashList = hashList(snatchActivity.getQuantity(), snatchActivity.getWinNumber(), robotNumbers);
        //更新detail的记录中number在winNumbers中的已中奖
        log.info(JsonHelper.writeValueAsString(winHashList));
        for (Pair<Integer, String> item : winHashList){
            //中奖金额要减去手续费
            int flag = snatchDetailService.updateWinOne(snatchActivity.getId(), item.getLeft(), item.getRight(), payoutQty.subtract(chargesQty), chargesQty);
            AssertHelper.isTrue(flag>0, "区块开奖失败.detail.win");
        }
//        List<Integer> winNumbers = winHashList.stream().map(Pair::getRight).collect(Collectors.toList());
//        List<SnatchDetail> winDetails = snatchDetailService.listByActivityIdAndNumber(snatchActivity.getId(), winNumbers);
//        List<SnatchDetail> winDetailsList = winDetails.stream().map(item ->{
//            item.setActivityId(snatchActivity.getId());
//            item.setPayoutQty(payoutQty);
//            item.setStatus(SnatchDetail.Status.WIN.ordinal());
//            item.setHash();
//        }).collect(Collectors.toList());
//        List<Integer> winNumbers = hashList.stream().map(Pair::getRight).collect(Collectors.toList());

//        int updateWin = snatchDetailService.updateWin(snatchActivity.getId(), winHashMapList, payoutQty);
//        AssertHelper.isTrue(updateWin > 0, "区块开奖失败.detail-win");
        //更新中奖用户的余额
        payoutWin(snatchActivity.getId());

        //snatch_config期数增加
        SnatchConfig snatchConfig = snatchConfigService.getById(snatchActivity.getConfigId());
        snatchConfig.setOrdinal(snatchConfig.getOrdinal() + 1);
        boolean updateSnatchConfig = snatchConfigService.updateById(snatchConfig);
        AssertHelper.isTrue(updateSnatchConfig, "区块开奖失败.config");
        //生成下一期的activity记录
        if (snatchConfig.getStatus().equals(SnatchConfig.Status.OPEN.ordinal())) {//是开启状态，新增activity
            addActivityByConfig(snatchConfig);
        }

        log.info("夺宝游戏进行开奖 - configName ={} - activityId={} - winNumber={}", snatchActivity.getConfigName(), snatchActivity.getId(), snatchActivity.getWinNumber());
        //发送开奖播报信息
        ThreadManager.getThreadPollProxy().execute(()->{
            //缓存一份最新的数据到redis
            List<SnatchActivityVo> snatchActivityVos = snatchActivityMapper.listNewlyLottery();
            List<String> strList = snatchActivityVos.stream().map(item -> item.getConfigName() + "第" + item.getOrdinal() + "期已开奖，敬请留意").collect(Collectors.toList());
            redisUtil.setList(RedisKeyConstant.SNATCH_LOTTERY_MSG_KEY, strList);
            String lotteryMsg = snatchActivity.getConfigName()+"第"+snatchActivity.getOrdinal()+"期已开奖，敬请留意";
            redisUtil.getStringRedisTemplate().convertAndSend(WebSocketKeyConstant.SNATCH_LOTTERY_BROADCAST_MSG, lotteryMsg);
        });

    }

    //用户中奖，更新余额
    private void payoutWin(Integer activityId){
        List<SnatchDetail> winSnatchDetails = snatchDetailService.winSnatchDetails(activityId);
        winSnatchDetails.forEach(item ->{
            userCoinService.addBalanceWithType(item.getUserId(), item.getCoinId(), item.getPayoutQty(), item.getId()+"", UserCoinDetailType.SNATCH);
        });
    }

    private void addActivityByConfig(SnatchConfig snatchConfig){
        SnatchActivity activity = new SnatchActivity();
        activity.setConfigId(snatchConfig.getId());
        activity.setConfigName(snatchConfig.getName());
        activity.setOrdinal(snatchConfig.getOrdinal());
        activity.setCoinId(snatchConfig.getCoinId());
        activity.setFinishQuantity(0);
        activity.setQuantity(snatchConfig.getQuantity());
        activity.setMaxQuantity(snatchConfig.getMaxQuantity());
        activity.setWinNumber(snatchConfig.getWinNumber());
        activity.setUnit(snatchConfig.getUnit());
        activity.setLotteryTime(null);
        activity.setRobotBackWin(snatchConfig.getRobotBackWin());
        activity.setRobotWinCount(snatchConfig.getRobotWinCount());
        activity.setCurrRobotWinCount(0);
        activity.setCreateTime(new Date());
        activity.setStatus(SnatchActivity.Status.RUNNING.ordinal());
        activity.setVersion(0);
        snatchActivityMapper.insert(activity);
    }

    //根据
    private List<Pair<Integer, String>> hashList(Integer quantity, Integer winNumber, List<Integer> robotNumbers){
        //获取两个hash，变hashCode取值
        List<Pair<Integer, String>> newList = getHashList(quantity, winNumber, robotNumbers);
        while (newList.size() < winNumber) {//开奖个数不够重新获取
            log.warn("开奖个数不足，重新获取进行开奖");
            newList = getHashList(quantity, winNumber, robotNumbers);
        }
        return newList.subList(0, winNumber);
    }

    private List<Pair<Integer, String>> getHashList(Integer quantity, Integer winNumber, List<Integer> robotNumbers){
        //获取两个hash，变hashCode取值
        List<Pair<Integer, String>> list = getHashNumberList(quantity);
        if (!EmptyHelper.isEmpty(robotNumbers)){
            List<Future<List<Pair<Integer, String>>>> futures = new ArrayList<>();
            for (int i = 0; i < winNumber; i++) {
                Future<List<Pair<Integer, String>>> future = ThreadManager.getThreadPollProxy().submit(new GetHashNumberListCallable(quantity));
                futures.add(future);
                log.info("获取hash i="+i);
            }
            for (Future<List<Pair<Integer, String>>> future : futures) {
                try {
                    list.addAll(future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        Map<Integer, String> map = new HashMap<>();
        list.forEach(item -> map.put(item.getLeft(), item.getRight()));
//        Set<Pair<Integer, String>> set = new HashSet<>(list);
        List<Pair<Integer, String>> newList = new ArrayList<>();
        map.forEach((key, value) -> newList.add(new ImmutablePair<>(key, value)));
        List<Pair<Integer, String>> robotList = new ArrayList<>();
        if (!EmptyHelper.isEmpty(robotNumbers)){//存在机器人
            Collections.shuffle(robotNumbers);
            robotList = newList.stream().filter(pair -> robotNumbers.contains(pair.getLeft())).collect(Collectors.toList());
            newList.removeAll(robotList);
        }
        robotList.addAll(newList);
        return robotList;
    }

//    public List<Pair<Integer, String>> getHashNumberListWithCatch(Integer scope){
//        try {
//            return getHashNumberList(scope);
//        } catch (Exception e) {
//            return getHashNumberList(scope);
//        }
//    }

    public static List<Pair<Integer, String>> getHashNumberList(Integer scope){
        List<Pair<Integer, String>> numList = new ArrayList<>();
        //获取EOS最顶部交易块
        String responseStr = HttpRequestUtil.sendGet("https://proxy.eosnode.tools/v1/chain/get_info", "");
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        Integer headBlockNum = jsonObject.getInteger("head_block_num");
//        log.info("获取当前EOS区块高度={}", headBlockNum);
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
                continue;
            }
            Integer number = getModel(hash, scope);
            if (number >= 0) {
                numList.add(new ImmutablePair<>(number, hash));
            }
        }

        if (EmptyHelper.isEmpty(numList)) {
            log.warn("没有获取到数值，重新获取一次");
            numList = getHashNumberList(scope);
        }
        return numList;
    }

    public static Integer getModel(String hash, Integer scope){
        Integer code = new String(hash).hashCode();
        Integer model = Math.abs(code % scope) + 1;
//        log.info("hash={} - code={} - model={}", hash, code, model);
        return model;
    }

    //返回第一注的编号
    private String saveDetail(SnatchActivity snatchActivity, Integer userId, Integer time, Date createTime, Boolean robot) {
        List<SnatchDetail> snatchDetails = new ArrayList<>();
        Integer activityId = snatchActivity.getId();
        Integer coinId = snatchActivity.getCoinId();
        String dateStr = DateHelper.format(snatchActivity.getCreateTime(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        for (int i=0; i< time; i++){
            Integer number = snatchActivity.getFinishQuantity() + i + 1 ;
            SnatchDetail detail = new SnatchDetail();
            detail.setConfigId(snatchActivity.getConfigId());
            detail.setActivityId(activityId);
            detail.setUserId(userId);
            detail.setBetQty(snatchActivity.getUnit());
            detail.setCoinId(coinId);
            detail.setNumber(number);
            detail.setNumberStr(getNumberStr(dateStr, snatchActivity.getConfigId(), snatchActivity.getOrdinal(), number));
            detail.setRobot(false);
            detail.setCreateTime(createTime);
            detail.setStatus(SnatchDetail.Status.UNKNOWN.ordinal());
            detail.setRobot(robot);
            snatchDetails.add(detail);
        }
        snatchDetailService.saveBatch(snatchDetails);
        return snatchDetails.get(0).getNumberStr();
    }

    //获取编号展示（日期 configId 期号 序号）
    private String getNumberStr(String dateStr, Integer configId, Integer ordinal, Integer number){
        StringBuilder numberStr = new StringBuilder();
        numberStr.append(dateStr)
                .append(configId)
                .append(ordinal)
                .append(number);
        return numberStr.toString();
    }

    //查看某一期的投注记录
    @Override
    @DataSource(DataSourceType.SLAVE)
    public SnatchBetNumberVo listMyDetail(Integer userId, Integer activityId){
        List<SnatchDetailVo> detailVos = snatchDetailService.listVoByUserIdAndActivityId(userId, activityId);
        Integer unknownCount = ((Long)detailVos.parallelStream().filter(item -> item.getStatus().equals(SnatchDetail.Status.UNKNOWN.ordinal())).count()).intValue();
        Integer notWinCount = ((Long)detailVos.parallelStream().filter(item -> item.getStatus().equals(SnatchDetail.Status.NOT_WIN.ordinal())).count()).intValue();
        Integer winCount = ((Long)detailVos.parallelStream().filter(item -> item.getStatus().equals(SnatchDetail.Status.WIN.ordinal())).count()).intValue();
        SnatchBetNumberVo snatchBetNumberVo = new SnatchBetNumberVo();
        snatchBetNumberVo.setUnknownCount(unknownCount);
        snatchBetNumberVo.setNotWinCount(notWinCount);
        snatchBetNumberVo.setWinCount(winCount);
        snatchBetNumberVo.setDetailVoList(detailVos);
        return snatchBetNumberVo;
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public SnatchDetailPageVo detailPage(Integer userId) {
        SnatchDetailPageVo data = new SnatchDetailPageVo();
        List<SnatchCoinVo> allCoins = snatchConfigService.listAllConfigCoin();
        List<SnatchConfigTypeVo> configTypeVos = Lists.newArrayList();
        Page<SnatchDetailAggregateVo> pageDetails = new Page<SnatchDetailAggregateVo>();
        if (allCoins.size() >= 1) {
            configTypeVos = snatchConfigService.listConfigTypeByCoinId(allCoins.get(0).getCoinId());
            if (configTypeVos.size() >= 1) {
                SnatchAggregateForm form = new SnatchAggregateForm();
                form.setConfigId(configTypeVos.get(0).getId());
                pageDetails = snatchDetailService.listAggregateVoByUserIdAndConfigId(userId, form);
            }
        }
        data.setAllCoins(allCoins);
        data.setConfigTypeVos(configTypeVos);
        data.setPageDetails(pageDetails);
        return data;
    }

    @Override
    public List<String> listNewlyLottery(){
        List<String> list = redisUtil.getList(RedisKeyConstant.SNATCH_LOTTERY_MSG_KEY, String.class);
        if (list == null) {
            List<SnatchActivityVo> snatchActivityVos = snatchActivityMapper.listNewlyLottery();
            list = snatchActivityVos.stream().map(item -> item.getConfigName() + "第" + item.getOrdinal() + "期已开奖，敬请留意").collect(Collectors.toList());
        }
        if (EmptyHelper.isEmpty(list)) {
            list.add("夺宝游戏正式上线！");
        }
        return list;
    }

}
