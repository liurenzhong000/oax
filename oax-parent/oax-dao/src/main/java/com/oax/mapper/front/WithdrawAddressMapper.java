package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.WithdrawAddress;

public interface WithdrawAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawAddress record);

    int insertSelective(WithdrawAddress record);

    WithdrawAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithdrawAddress record);

    int updateByPrimaryKey(WithdrawAddress record);

    List<WithdrawAddress> getByUserIdQueryurlList(@Param("userId") Integer userId, @Param("coinId") Integer coinId);
}