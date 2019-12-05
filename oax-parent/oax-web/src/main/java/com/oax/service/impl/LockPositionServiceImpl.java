package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.CoinLockConfig;
import com.oax.entity.front.LockPosition;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.CoinLockConfigMapper;
import com.oax.mapper.front.LockPositionMapper;
import com.oax.service.LockPositionService;
import com.oax.vo.LockPositionRecordVO;
import com.oax.vo.LockPositionVO;

@Service
public class LockPositionServiceImpl implements LockPositionService {
    @Autowired
    private CoinLockConfigMapper coinLockConfigMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LockPositionMapper lockPositionMapper;

    @DataSource(DataSourceType.SLAVE)
    @Override
    public CoinLockConfig selectCoinLockConfigById(Integer id) {
        List<CoinLockConfig> list = redisUtil.getList(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey(), CoinLockConfig.class);
        CoinLockConfig coinLockConfig = getCoinLockConfig(list, id);
        if (coinLockConfig==null){
            return coinLockConfigMapper.selectCoinLockConfigById(id);
        }
        return coinLockConfig;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public BigDecimal getUserCoinByCoinId(Integer userId, Integer coinId) {
        return coinLockConfigMapper.getUserCoinByCoinId(userId,coinId);
    }

    @Transactional
    @DataSource(DataSourceType.MASTER)
    @Override
    public boolean lock(LockPositionVO vo) {
        LockPosition lockPosition = new LockPosition();
        lockPosition.setCoinId(vo.getCoinId());
        lockPosition.setUserId(vo.getUserId());
        lockPosition.setLockQty(vo.getLockQty());
        lockPosition.setLockDays(vo.getLockDays());
        lockPosition.setStatus(0);
        lockPosition.setCreateTime(new Date());
        Integer insertRow = lockPositionMapper.insert(lockPosition);
        if (insertRow!=null&&insertRow>0){
            //修改资产数据
            UserCoin userCoin = new UserCoin();
            userCoin.setUserId(vo.getUserId());
            userCoin.setCoinId(vo.getCoinId());
            userCoin.setBanlance(vo.getLockQty());
            Integer updateRow = lockPositionMapper.subtractUserCoin(userCoin);
            if (updateRow!=null&&updateRow>0){
                return true;
            }
        }
        return false;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public CoinLockConfig selectCoinLockConfigByCoinId(Integer coinId) {
        List<CoinLockConfig> list = redisUtil.getList(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey(), CoinLockConfig.class);
        CoinLockConfig model= null;
        for (CoinLockConfig coinLockConfig : list) {
            if (coinLockConfig.getCoinId()==coinId){
                model = coinLockConfig;
            }
        }
        //如果缓存中没有
        if (model==null){
            model = coinLockConfigMapper.selectCoinLockConfigByCoinId(coinId);
        }
        return model;
    }

    @Transactional
    @DataSource(DataSourceType.MASTER)
    @Override
    public boolean declock(Integer id, Integer userId) {
        Integer declockRow = lockPositionMapper.declock(id,userId);
        if (declockRow!=null&&declockRow>0){
            //解锁之后增加资产
            LockPosition lockPosition = lockPositionMapper.selectLockPositionById(id);
            //修改资产数据
            UserCoin userCoin = new UserCoin();
            userCoin.setUserId(userId);
            userCoin.setCoinId(lockPosition.getCoinId());
            userCoin.setBanlance(lockPosition.getLockQty());
            Integer updateRow = lockPositionMapper.addUserCoin(userCoin);
            if (updateRow!=null&&updateRow>0){
                return true;
            }
        }
        return false;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public PageInfo getPage(LockPositionRecordVO vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        Map<String,Object> map = new HashMap<>();
        map.put("coinId", vo.getCoinId());
        map.put("userId", vo.getUserId());
        map.put("beginTime", vo.getBeginTime());
        map.put("endTime", vo.getEndTime());
        List<Map<String,Object>> list = lockPositionMapper.getPage(map);
        return new PageInfo<>(list);
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<Map<String,Object>> getInterestSharebonusList(Integer lockPositionId) {
        return lockPositionMapper.getInterestSharebonusList(lockPositionId);
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<Map<String, Object>> getMarketInfoList(Integer coinId) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<MarketInfo> list = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
        for (MarketInfo marketInfo : list) {
            if (marketInfo.getCoinId()==coinId||marketInfo.getMarketCoinId()==coinId){
                Map<String,Object> map = new HashMap<>();
                map.put("marketId", marketInfo.getMarketId());
                map.put("marketName", marketInfo.getCoinName()+"/"+marketInfo.getMarketCoinName());
                mapList.add(map);
            }
        }
        return mapList;
    }

    private CoinLockConfig getCoinLockConfig(List<CoinLockConfig> list,Integer id){
        if (list==null||list.size()==0) {
            return null;
        };
        CoinLockConfig model = null;
        for (CoinLockConfig coinLockConfig : list) {
            if (coinLockConfig.getId()==id){
                model = coinLockConfig;
                break;
            }
        }
        return model;

    }
}
