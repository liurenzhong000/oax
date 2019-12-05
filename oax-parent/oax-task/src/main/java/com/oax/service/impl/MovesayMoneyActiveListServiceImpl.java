package com.oax.service.impl;

import com.oax.entity.front.*;
import com.oax.mapper.front.MovesayMoneyActiveListMapper;
import com.oax.mapper.front.MovesayOperateLogMapper;
import com.oax.mapper.front.PromoteFundMapper;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.MovesayMoneyActiveListService;
import com.oax.service.PromoteFundService;
import com.oax.service.UserCoinService;
import com.oax.util.GenerateOrderNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MovesayMoneyActiveListServiceImpl implements MovesayMoneyActiveListService {

    @Autowired
    private MovesayMoneyActiveListMapper mapper;

    @Autowired
    private MovesayOperateLogMapper movesayOperateLogMapper;

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private PromoteFundMapper promoteFundMapper;


    @Autowired
    private PromoteFundService promoteFundService;

    @Autowired
    private UserCoinService userCoinService;

    public List<MovesayMoneyActiveList> selectRecordByUserId(Integer active_id, Integer user_id) {
        MovesayMoneyActiveListExample movesayMoneyActiveListExample = new MovesayMoneyActiveListExample();
        MovesayMoneyActiveListExample.Criteria criteria = movesayMoneyActiveListExample.createCriteria();
        criteria.andActiveIdEqualTo(active_id);
        criteria.andUserIdEqualTo(user_id);
        return mapper.selectByExample(movesayMoneyActiveListExample);
    }

    public int insertMovesayMoney(MovesayMoneyActiveList movesayMoneyActiveList) {
        return mapper.insert(movesayMoneyActiveList);
    }

    public List<MovesayMoneyActiveList> SelectAllRecord() {
        MovesayMoneyActiveListExample movesayMoneyActiveListExample = new MovesayMoneyActiveListExample();
        MovesayMoneyActiveListExample.Criteria criteria = movesayMoneyActiveListExample.createCriteria();
        return mapper.selectByExample(movesayMoneyActiveListExample);
    }

    public List<MovesayMoneyActiveList> selectRecordById(Integer user_id) {
        MovesayMoneyActiveListExample movesayMoneyActiveListExample = new MovesayMoneyActiveListExample();
        MovesayMoneyActiveListExample.Criteria criteria = movesayMoneyActiveListExample.createCriteria();
        criteria.andUserIdEqualTo(user_id);
        return mapper.selectByExample(movesayMoneyActiveListExample);
    }

    public int updateStatus(String id,Integer status) {
        return mapper.updateStatus(id,status);
    }

    //每日释放收益更新
    public void updateDayMoveMoneyActive(MovesayMoneyActiveList movesayMoneyActiveList, BigDecimal money) {
        movesayMoneyActiveList.setProfitsMoney(movesayMoneyActiveList.getProfitsMoney().add(money));
        movesayMoneyActiveList.setUpdateTime(new Date());

        MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
        GenerateOrderNo generateOrderNo = new GenerateOrderNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        MoveActiveNo param = new MoveActiveNo();
        param.setCreateTime("%" + sdf.format(new Date()) + "%");
        param.setUserId(movesayMoneyActiveList.getUserId());
        String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
        String operateId = generateOrderNo.getOperateId(sdf2.format(new Date()), maxOrderNo, movesayMoneyActiveList.getUserId());
        movesayOperateLog.setId(operateId);
        movesayOperateLog.setOrderId(movesayMoneyActiveList.getId());
        movesayOperateLog.setUserid(movesayMoneyActiveList.getUserId());
        movesayOperateLog.setMarketId(movesayMoneyActiveList.getMarketId());
        movesayOperateLog.setType(4);
        movesayOperateLog.setMoney(money);
        movesayOperateLog.setCreateTime(new Date());
        movesayOperateLog.setUpdateTime(new Date());
        mapper.updateByPrimaryKey(movesayMoneyActiveList);
        movesayOperateLogMapper.insert(movesayOperateLog);

    }

    //动态余利宝到期本金利息全部释放
    public void updateAllMoveMoneyActive(MovesayMoneyActiveList movesayMoneyActiveList, UserCoin userCoin) {
        BigDecimal money = movesayMoneyActiveList.getCumulativeJoin().add(movesayMoneyActiveList.getProfitsMoney());
        //更新抢购记录表
        movesayMoneyActiveList.setDepositMoney(movesayMoneyActiveList.getDepositMoney().add(movesayMoneyActiveList.getProfitsMoney()));
        movesayMoneyActiveList.setProfitsMoney(new BigDecimal("0"));
        movesayMoneyActiveList.setStatus(3);
        movesayMoneyActiveList.setUpdateTime(new Date());

        //更新抢购明细表
        MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
        GenerateOrderNo generateOrderNo = new GenerateOrderNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        MoveActiveNo param = new MoveActiveNo();
        param.setCreateTime("%" + sdf.format(new Date()) + "%");
        param.setUserId(movesayMoneyActiveList.getUserId());
        String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
        String operateId = generateOrderNo.getOperateId(sdf2.format(new Date()), maxOrderNo, movesayMoneyActiveList.getUserId());
        movesayOperateLog.setId(operateId);
        movesayOperateLog.setOrderId(movesayMoneyActiveList.getId());
        movesayOperateLog.setUserid(movesayMoneyActiveList.getUserId());
        movesayOperateLog.setMarketId(movesayMoneyActiveList.getMarketId());
        movesayOperateLog.setType(5);
        movesayOperateLog.setMoney(money);
        movesayOperateLog.setCreateTime(new Date());
        movesayOperateLog.setUpdateTime(new Date());

        //更新用户资产表
        userCoin.setBanlance(userCoin.getBanlance().add(money));
        userCoin.setFreezingBanlance(userCoin.getFreezingBanlance().subtract(movesayMoneyActiveList.getJoinMoney()));
        userCoin.setUpdateTime(new Date());
        mapper.updateByPrimaryKey(movesayMoneyActiveList);
        movesayOperateLogMapper.insert(movesayOperateLog);
        userCoinMapper.updateByPrimaryKey(userCoin);
    }

    //释放推广收益
    public void updatePromoteFund(MovesayMoneyActiveList movesayMoneyActiveList) {
        List<PromoteFund> promoteFunds = promoteFundService.getPromoteByUser(movesayMoneyActiveList.getUserId());
        for (int i = 0; i < promoteFunds.size(); i++) {
            UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(promoteFunds.get(i).getUserid(), promoteFunds.get(i).getMarketId());
            //更新用户操作明细表
            MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
            GenerateOrderNo generateOrderNo = new GenerateOrderNo();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
            MoveActiveNo param = new MoveActiveNo();
            param.setCreateTime("%" + sdf.format(new Date()) + "%");
            param.setUserId(movesayMoneyActiveList.getUserId());
            String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
            String operateId = generateOrderNo.getOperateId(sdf2.format(new Date()), maxOrderNo, movesayMoneyActiveList.getUserId());
            movesayOperateLog.setId(operateId);
            movesayOperateLog.setUserid(movesayMoneyActiveList.getUserId());
            movesayOperateLog.setMarketId(promoteFunds.get(i).getMarketId());
            movesayOperateLog.setType(6);
            movesayOperateLog.setMoney(promoteFunds.get(i).getMoney());
            movesayOperateLog.setCreateTime(new Date());
            movesayOperateLog.setUpdateTime(new Date());

            //更新推广收益
            promoteFunds.get(i).setStatus(2);
            promoteFunds.get(i).setUpdateTime(new Date());

            movesayOperateLogMapper.insert(movesayOperateLog);
            promoteFundMapper.updateByPrimaryKey(promoteFunds.get(i));

            if (userCoin == null) {
                userCoin = new UserCoin();
                userCoin.setUserId(promoteFunds.get(i).getUserid());
                userCoin.setCoinId(promoteFunds.get(i).getMarketId());
                userCoin.setBanlance(promoteFunds.get(i).getMoney());
                userCoin.setFreezingBanlance(new BigDecimal(0));
                userCoin.setCreateTime(new Date());
                userCoin.setUpdateTime(new Date());
                userCoinMapper.insertSelective(userCoin);
            } else {
                userCoin.setBanlance(userCoin.getBanlance().add(promoteFunds.get(i).getMoney()));
                userCoin.setUpdateTime(new Date());
                userCoinMapper.updateByPrimaryKey(userCoin);
            }
        }

    }

    //释放静态余利宝收益
    public void updateStaticMoneyActive(UserCoin userCoin, MovesayMoneyActiveList movesayMoneyActiveList) {
        //更新抢购记录表
        movesayMoneyActiveList.setStatus(3);
        movesayMoneyActiveList.setUpdateTime(new Date());

        //更新抢购明细表
        MovesayOperateLog movesayOperateLog = new MovesayOperateLog();
        GenerateOrderNo generateOrderNo = new GenerateOrderNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        MoveActiveNo param = new MoveActiveNo();
        param.setCreateTime("%" + sdf.format(new Date()) + "%");
        param.setUserId(movesayMoneyActiveList.getUserId());
        String maxOrderNo = movesayOperateLogMapper.selectMaxOrderNo(param);
        String operateId = generateOrderNo.getOperateId(sdf2.format(new Date()), maxOrderNo, movesayMoneyActiveList.getUserId());
        movesayOperateLog.setId(operateId);
        movesayOperateLog.setOrderId(movesayMoneyActiveList.getId());
        movesayOperateLog.setUserid(movesayMoneyActiveList.getUserId());
        movesayOperateLog.setMarketId(movesayMoneyActiveList.getMarketId());
        movesayOperateLog.setType(5);
        movesayOperateLog.setMoney(movesayMoneyActiveList.getJoinMoney().add(movesayMoneyActiveList.getProfitsMoney()));
        movesayOperateLog.setCreateTime(new Date());
        movesayOperateLog.setUpdateTime(new Date());

        //更新用户资产表
        userCoin.setBanlance(userCoin.getBanlance().add(movesayMoneyActiveList.getJoinMoney().add(movesayMoneyActiveList.getProfitsMoney())));
        userCoin.setFreezingBanlance(userCoin.getFreezingBanlance().subtract(movesayMoneyActiveList.getJoinMoney()));
        userCoin.setUpdateTime(new Date());

        userCoinMapper.updateByPrimaryKey(userCoin);
        mapper.updateByPrimaryKey(movesayMoneyActiveList);
        movesayOperateLogMapper.insert(movesayOperateLog);


    }
}