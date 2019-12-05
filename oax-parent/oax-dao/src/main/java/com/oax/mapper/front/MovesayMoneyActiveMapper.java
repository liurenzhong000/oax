package com.oax.mapper.front;

import com.oax.entity.front.Active;
import com.oax.entity.front.MovesayMoneyActive;
import com.oax.entity.front.MovesayMoneyActiveExample;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovesayMoneyActiveMapper {
    long countByExample(MovesayMoneyActiveExample example);

    int deleteByExample(MovesayMoneyActiveExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MovesayMoneyActive record);

    int insertSelective(MovesayMoneyActive record);

    List<MovesayMoneyActive> selectByExample(MovesayMoneyActiveExample example);

    MovesayMoneyActive selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MovesayMoneyActive record, @Param("example") MovesayMoneyActiveExample example);

    int updateByExample(@Param("record") MovesayMoneyActive record, @Param("example") MovesayMoneyActiveExample example);

    int updateByPrimaryKeySelective(MovesayMoneyActive record);

    int updateByPrimaryKey(MovesayMoneyActive record);

    int updateStatus(@Param("id") Integer id,
                     @Param("status") Integer status);

    int updateJoinDetail(@Param("joinMoney")BigDecimal joinMoney,
                         @Param("joinNum") Integer joinNum,
                         @Param("id") Integer id);

    List<Active> selectActive();
}