package com.oax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.Investment;
import com.oax.mapper.front.InvestmentMapper;

@Service
public class InvestmentService {

	@Autowired
	private InvestmentMapper investmentMapper;
	
	@DataSource(DataSourceType.SLAVE)
	public List<Investment> getList() {

		return investmentMapper.findList();
	}

	@Transactional
	@DataSource(DataSourceType.MASTER)
	public List<Investment> add() {
		Investment i = new Investment();
		i.setName("1");
		i.setEmail("2");
		i.setType("3");
		i.setSuggestion("4");

		investmentMapper.add(i);
		return investmentMapper.findList();
	}
}
