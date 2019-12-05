package com.oax.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.Investment;
import com.oax.service.InvestmentService;

@RestController
@RequestMapping("investment")
public class InvestmentController {

	@Autowired
	private InvestmentService investmentService;

	@RequestMapping("list")
	public ResultResponse getList() {

		List<Investment> investmentList=investmentService.getList();
		return new ResultResponse(true, investmentList);
	}

	@RequestMapping("add")
	public List<Investment> add() {

		return investmentService.add();
	}
}
