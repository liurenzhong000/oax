package com.oax.service.impl;

import com.github.pagehelper.PageInfo;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.param.UserInviteParam;
import com.oax.entity.admin.vo.UserInviteVo;
import com.oax.entity.front.*;
import com.oax.exception.VoException;
import com.oax.mapper.front.*;
import com.oax.service.MovesayMoneyActiveListService;
import com.oax.service.MovesayMoneyActiveService;
import com.oax.service.UserCoinService;
import com.oax.service.UserService;
import com.oax.util.DateAndTimeUtil;
import com.oax.util.GenerateOrderNo;
import com.oax.vo.RushToBuyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MovesayMoneyActiveListServiceImpl implements MovesayMoneyActiveListService {

    private final Logger mLogger = LoggerFactory.getLogger(MovesayMoneyActiveListServiceImpl.class);
    @Autowired
    private MovesayMoneyActiveListMapper mapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private MovesayMoneyActiveMapper movesayMoneyActiveMapper;


    @Autowired
    private MovesayOperateLogMapper movesayOperateLogMapper;

    @Autowired
    private PromoteFundMapper promoteFundMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCoinService userCoinService;


    @DataSource(DataSourceType.MASTER)
    public List<MovesayMoneyActiveList> selectRecordByUserId(Integer active_id, Integer user_id) {
        MovesayMoneyActiveListExample movesayMoneyActiveListExample = new MovesayMoneyActiveListExample();
        MovesayMoneyActiveListExample.Criteria criteria = movesayMoneyActiveListExample.createCriteria();
        criteria.andActiveIdEqualTo(active_id);
        criteria.andUserIdEqualTo(user_id);
        return mapper.selectByExample(movesayMoneyActiveListExample);
    }

    //静态余利宝抢购
    @DataSource(DataSourceType.MASTER)
    public void insertStaticMovesayMoney(RushToBuyVO vo) throws VoException {
        BigDecimal profits_money = new BigDecimal("0");
        //查询
        MovesayMoneyActive movesayMoneyActive = movesayMoneyActiveMapper.selectByPrimaryKey(vo.getActiveId());
        UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(vo.getUserId(), movesayMoneyActive.getMarketId());

        MovesayMoneyActiveList movesayMoneyActiveList = new MovesayMoneyActiveList();
        String no = mapper.selectMaxOrderNo(vo.getActiveId());

        //用户抢购记录表
        GenerateOrderNo generateOrderNo = new GenerateOrderNo();
        String orderId = generateOrderNo.getId(movesayMoneyActive, no);
        movesayMoneyActiveList.setId(orderId);
        movesayMoneyActiveList.setActiveId(vo.getActiveId());
        movesayMoneyActiveList.setMarketId(vo.getMarketId());
        movesayMoneyActiveList.setUserId(vo.getUserId());
        movesayMoneyActiveList.setType(1);
        movesayMoneyActiveList.setDepositMoney(new BigDecimal(0));
        movesayMoneyActiveList.setDate(movesayMoneyActive.getDate());
        movesayMoneyActiveList.setStartTime(movesayMoneyActive.getInterstartTime());
        movesayMoneyActiveList.setEndTime(movesayMoneyActive.getInterendTime());
        movesayMoneyActiveList.setProfits(movesayMoneyActive.getProfits());
        BigDecimal rate = (new BigDecimal(movesayMoneyActive.getDate())).divide(new BigDecimal(365), 6, RoundingMode.HALF_UP);
        BigDecimal profits = new BigDecimal(movesayMoneyActive.getProfits()).divide(new BigDecimal(100));
        profits_money = profits_money.add(vo.getJoinMoney().multiply(profits).multiply(rate)).setScale(6, BigDecimal.ROUND_HALF_UP);
        movesayMoneyActiveList.setProfitsMoney(profits_money);
        movesayMoneyActiveList.setJoinMoney(vo.getJoinMoney());
        movesayMoneyActiveList.setCumulativeJoin(vo.getJoinMoney());
        movesayMoneyActiveList.setUpdateTime(new Date());
        movesayMoneyActiveList.setCreateTime(new Date());
        movesayMoneyActiveList.setStatus(1);


        //更新活动参与金额和数量
        movesayMoneyActive.setJoinMoney(movesayMoneyActive.getJoinMoney().add(vo.getJoinMoney()));
        movesayMoneyActive.setJoinNum(movesayMoneyActive.getJoinNum() + 1);
        movesayMoneyActive.setUpdateTime(new Date());

        //更新资金操作明细表
        MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        MoveActiveNo param = new MoveActiveNo();
        param.setCreateTime("%" + sdf.format(new Date()) + "%");
        param.setUserId(vo.getUserId());
        String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
        String operateId = generateOrderNo.getOperateId(maxOrderNo, vo.getUserId());
        movesayOperateLog.setId(operateId);
        movesayOperateLog.setOrderId(orderId);
        movesayOperateLog.setUserid(vo.getUserId());
        movesayOperateLog.setMarketId(vo.getMarketId());
        movesayOperateLog.setType(3);
        movesayOperateLog.setMoney(vo.getJoinMoney());
        movesayOperateLog.setCreateTime(new Date());
        movesayOperateLog.setUpdateTime(new Date());

        //更新余额
        userCoin.setBanlance(userCoin.getBanlance().subtract(movesayMoneyActiveList.getJoinMoney()));
        userCoin.setFreezingBanlance(userCoin.getFreezingBanlance().add(movesayMoneyActiveList.getJoinMoney()));
        userCoin.setUpdateTime(new Date());

        mapper.insert(movesayMoneyActiveList);
        userCoinMapper.updateByPrimaryKey(userCoin);
        movesayMoneyActiveMapper.updateByPrimaryKey(movesayMoneyActive);
        movesayOperateLogMapper.insert(movesayOperateLog);
    }

    //动态余利宝抢购
    @DataSource(DataSourceType.MASTER)
    public void insertActiveMovesayMoney(RushToBuyVO vo) throws VoException, ParseException {
        //查询是否有推荐人
        Boolean flag = false;
        Member user = userService.selectById("" + vo.getUserId());
        MovesayMoneyActive movesayMoneyActive = movesayMoneyActiveMapper.selectByPrimaryKey(vo.getActiveId());
        UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(vo.getUserId(), movesayMoneyActive.getMarketId());

        //邀请人不为空，注册时间在活动开始时间以后，且是邀请人的前五个被邀请者
        if (user.getFromUserId() != null) {
            UserInviteParam userInviteParam = new UserInviteParam();
            userInviteParam.setUserId(user.getFromUserId());
            userInviteParam.setBeginTime(movesayMoneyActive.getStartTime());
            PageInfo<UserInviteVo> userInviteVoList = userService.getByUserIdQueryUserInvite(userInviteParam);
            for (int i = 0; i < userInviteVoList.getList().size(); i++) {
                if (userInviteVoList.getList().get(i).getUserId().intValue() == user.getId().intValue())
                    flag = true;
            }
        }

        //更新用户抢购记录表
        String orderId = "";
        MovesayMoneyActiveList movesayMoneyActiveList = new MovesayMoneyActiveList();
        String no = mapper.selectMaxOrderNo(vo.getActiveId());
        GenerateOrderNo generateOrderNo = new GenerateOrderNo();
        orderId = generateOrderNo.getId(movesayMoneyActive, no);
        //获取第二天凌晨
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String StartTime = DateAndTimeUtil.checkOption("next", sdf.format(new Date()), 1) + " 00:00:00";
        String endTime = DateAndTimeUtil.checkOption("next", StartTime, movesayMoneyActive.getDate() - 1) + " 00:00:00";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        movesayMoneyActiveList.setId(orderId);
        movesayMoneyActiveList.setActiveId(vo.getActiveId());
        movesayMoneyActiveList.setMarketId(movesayMoneyActive.getMarketId());
        movesayMoneyActiveList.setUserId(vo.getUserId());
        movesayMoneyActiveList.setDepositMoney(new BigDecimal(0));
        movesayMoneyActiveList.setType(2);
        movesayMoneyActiveList.setDate(movesayMoneyActive.getDate());
        movesayMoneyActiveList.setStartTime(sd.parse(StartTime));
        movesayMoneyActiveList.setEndTime(sd.parse(endTime));
        movesayMoneyActiveList.setProfits(movesayMoneyActive.getProfits());
        movesayMoneyActiveList.setProfitsMoney(new BigDecimal("0"));
        movesayMoneyActiveList.setJoinMoney(vo.getJoinMoney());
        movesayMoneyActiveList.setCumulativeJoin(vo.getJoinMoney());
        movesayMoneyActiveList.setUpdateTime(new Date());
        movesayMoneyActiveList.setCreateTime(new Date());
        movesayMoneyActiveList.setStatus(2);

        //更新活动参与人数和金额
        BigDecimal joinMoney=movesayMoneyActive.getJoinMoney().add(vo.getJoinMoney());
        Integer joinNum=movesayMoneyActive.getJoinNum() + 1;

        //更新资金操作明细表
        MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        MoveActiveNo param = new MoveActiveNo();
        param.setCreateTime("%" + sdf1.format(new Date()) + "%");
        param.setUserId(vo.getUserId());
        String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
        String operateId = generateOrderNo.getOperateId(maxOrderNo, vo.getUserId());
        movesayOperateLog.setId(operateId);
        movesayOperateLog.setOrderId(orderId);
        movesayOperateLog.setUserid(vo.getUserId());
        movesayOperateLog.setMarketId(movesayMoneyActive.getMarketId());
        movesayOperateLog.setType(3);
        movesayOperateLog.setMoney(vo.getJoinMoney());
        movesayOperateLog.setCreateTime(new Date());
        movesayOperateLog.setUpdateTime(new Date());

        //更新余额
        userCoin.setBanlance(userCoin.getBanlance().subtract(movesayMoneyActiveList.getJoinMoney()));
        userCoin.setFreezingBanlance(userCoin.getFreezingBanlance().add(movesayMoneyActiveList.getJoinMoney()));
        userCoin.setUpdateTime(new Date());

        mapper.insert(movesayMoneyActiveList);
        userCoinMapper.updateByPrimaryKey(userCoin);
        movesayMoneyActiveMapper.updateJoinDetail(joinMoney,joinNum,movesayMoneyActive.getId());
        movesayOperateLogMapper.insert(movesayOperateLog);

        //更新推广收益表
        if (flag == true) {
            PromoteFund promoteFund = new PromoteFund();
            promoteFund.setUserid(user.getFromUserId());
            promoteFund.setMarketId(movesayMoneyActive.getMarketId());
            BigDecimal rate = (new BigDecimal(movesayMoneyActive.getDate())).divide(new BigDecimal(360), 6, RoundingMode.HALF_UP);
            BigDecimal profits = new BigDecimal(movesayMoneyActive.getProfits()).divide(new BigDecimal(100));
            BigDecimal money = vo.getJoinMoney().multiply(profits).multiply(rate).multiply(new BigDecimal(0.1));
            promoteFund.setMoney(money);
            promoteFund.setStatus(1);
            promoteFund.setCreateTime(new Date());
            promoteFund.setUpdateTime(new Date());
            promoteFundMapper.insert(promoteFund);
        }
    }

    @DataSource(DataSourceType.MASTER)
    public List<MovesayMoneyActiveList> selectRecordById(Integer user_id) {
        MovesayMoneyActiveListExample movesayMoneyActiveListExample = new MovesayMoneyActiveListExample();
        MovesayMoneyActiveListExample.Criteria criteria = movesayMoneyActiveListExample.createCriteria();
        criteria.andUserIdEqualTo(user_id);
        return mapper.selectByExample(movesayMoneyActiveListExample);
    }

    @DataSource(DataSourceType.MASTER)
    public String selectMaxOrderNo(Integer active_id) {
        return mapper.selectMaxOrderNo(active_id);
    }

    @DataSource(DataSourceType.MASTER)
    public MovesayMoneyActiveList selectByPrimaryKey(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    @DataSource(DataSourceType.MASTER)
    public int updateMovesayMoneyList(MovesayMoneyActiveList movesayMoneyActiveList) {
        return mapper.updateByPrimaryKey(movesayMoneyActiveList);
    }

    //复投
    @DataSource(DataSourceType.MASTER)
    public void repeatMovesayMoneyList(String id, String num) {
        //更新抢购记录表
        MovesayMoneyActiveList movesayMoneyActiveList = mapper.selectByPrimaryKey(id);
        movesayMoneyActiveList.setCumulativeJoin(movesayMoneyActiveList.getCumulativeJoin().add(new BigDecimal(num)));
        movesayMoneyActiveList.setProfitsMoney(movesayMoneyActiveList.getProfitsMoney().subtract(new BigDecimal(num)));
        movesayMoneyActiveList.setUpdateTime(new Date());

        //更新抢购记录明细表
        MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
        GenerateOrderNo generateOrderNo = new GenerateOrderNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        MoveActiveNo param = new MoveActiveNo();
        param.setUserId(movesayMoneyActiveList.getUserId());
        String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
        String operateId = generateOrderNo.getOperateId(maxOrderNo, movesayMoneyActiveList.getUserId());
        movesayOperateLog.setId(operateId);
        movesayOperateLog.setOrderId(movesayMoneyActiveList.getId());
        movesayOperateLog.setUserid(movesayMoneyActiveList.getUserId());
        movesayOperateLog.setMarketId(movesayMoneyActiveList.getMarketId());
        movesayOperateLog.setType(1);
        movesayOperateLog.setMoney(new BigDecimal(num));
        movesayOperateLog.setCreateTime(new Date());
        movesayOperateLog.setUpdateTime(new Date());
        mapper.updateByPrimaryKey(movesayMoneyActiveList);
        movesayOperateLogMapper.insert(movesayOperateLog);
    }

    //提现
    @DataSource(DataSourceType.MASTER)
    public void updateDepositRecord(String id,String num) throws VoException {
        MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
        MovesayMoneyActiveList movesayMoneyActiveList = mapper.selectByPrimaryKey(id);
        UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(movesayMoneyActiveList.getUserId(), movesayMoneyActiveList.getMarketId());

        //订单操作记录表
        GenerateOrderNo generateOrderNo = new GenerateOrderNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        MoveActiveNo param = new MoveActiveNo();
        param.setCreateTime("%" + sdf.format(new Date()) + "%");
        param.setUserId(movesayMoneyActiveList.getUserId());
        String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
        String operateId = generateOrderNo.getOperateId(maxOrderNo, movesayMoneyActiveList.getUserId());
        movesayOperateLog.setId(operateId);
        movesayOperateLog.setOrderId(movesayMoneyActiveList.getId());
        movesayOperateLog.setUserid(movesayMoneyActiveList.getUserId());
        movesayOperateLog.setMarketId(movesayMoneyActiveList.getMarketId());
        movesayOperateLog.setType(2);
        movesayOperateLog.setMoney(new BigDecimal(num));
        movesayOperateLog.setCreateTime(new Date());
        movesayOperateLog.setUpdateTime(new Date());

        movesayMoneyActiveList.setDepositMoney(new BigDecimal(num));
        movesayMoneyActiveList.setProfitsMoney(movesayMoneyActiveList.getProfitsMoney().subtract(new BigDecimal(num)));
        movesayMoneyActiveList.setUpdateTime(new Date());

        userCoin.setBanlance(userCoin.getBanlance().add(new BigDecimal(num)));
        userCoin.setUpdateTime(new Date());

        mapper.updateByPrimaryKey(movesayMoneyActiveList);
        userCoinMapper.updateByPrimaryKey(userCoin);
        movesayOperateLogMapper.insert(movesayOperateLog);
    }
}
