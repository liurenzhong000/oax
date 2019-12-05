package com.oax.mapper.front;

import com.oax.entity.admin.param.profitParam;
import com.oax.entity.front.PromoteFund;
import com.oax.entity.front.PromoteFundExample;
import java.util.List;

import com.oax.entity.front.PromoteProfit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PromoteFundMapper {
    long countByExample(PromoteFundExample example);

    int deleteByExample(PromoteFundExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PromoteFund record);

    int insertSelective(PromoteFund record);

    List<PromoteFund> selectByExample(PromoteFundExample example);

    PromoteFund selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PromoteFund record, @Param("example") PromoteFundExample example);

    int updateByExample(@Param("record") PromoteFund record, @Param("example") PromoteFundExample example);

    int updateByPrimaryKeySelective(PromoteFund record);

    int updateByPrimaryKey(PromoteFund record);

    List<PromoteProfit> selectProfitRecord(Integer userId);

    List<PromoteProfit> selectProfitAll(profitParam profitParam);
}