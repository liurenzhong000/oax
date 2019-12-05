package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.CoinLockConfig;

@Mapper
public interface CoinLockConfigMapper {
    List<CoinLockConfig> getCoinLockConfig();

    CoinLockConfig selectCoinLockConfigById(Integer id);

    BigDecimal getUserCoinByCoinId(@Param("userId")Integer userId, @Param("coinId") Integer coinId);

    CoinLockConfig selectCoinLockConfigByCoinId(Integer coinId);

    Integer update(CoinLockConfig coinLockConfig);

    Integer save(CoinLockConfig coinLockConfig);

    List<CoinLockConfig> getList();

    CoinLockConfig selectById(Integer id);

    Integer isShow(@Param("id") Integer id, @Param("isShow")Integer isShow);

    List<CoinLockConfig> getAllCoinLockConfig();
}
