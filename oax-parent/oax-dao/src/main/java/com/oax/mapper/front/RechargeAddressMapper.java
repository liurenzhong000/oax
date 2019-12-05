package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.RechargeAddress;

public interface RechargeAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeAddress record);

    int insertSelective(RechargeAddress record);

    RechargeAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeAddress record);

    int updateByPrimaryKey(RechargeAddress record);

    List<RechargeAddress> selectByCoinType(Integer type);

    RechargeAddress selectByUserIdAndCoinId(@Param("userId")Integer userId, @Param("coinId")Integer coinId);

    /**
     * @Title：getRechargeAddress
     * @Description：根据父币id和userid获取用户的币的钱包地址信息
     * @throws
     */
    Integer getCount(@Param("parentCoinId")Integer parentCoinId,@Param("userId") Integer userId);


    List<RechargeAddress> selectByAddress(String toAddress);

    RechargeAddress selectByAddressAndParentCoinId(@Param("toAddress") String toAddress,@Param("parentId") Integer parentId);
}