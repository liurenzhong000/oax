package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.TradeRedman;

@Mapper
public interface TradeRedmanMapper {
    List<TradeRedman> getList(@Param("currentMonday") String currentMonday, @Param("nextMonday") String nextMonday);
}
