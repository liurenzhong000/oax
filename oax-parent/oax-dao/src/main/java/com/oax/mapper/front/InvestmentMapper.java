package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.front.Investment;

@Mapper
public interface InvestmentMapper {

    public List<Investment> findList();

    public int add(Investment investment);

}
