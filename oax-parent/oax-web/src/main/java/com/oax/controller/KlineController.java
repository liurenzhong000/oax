package com.oax.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.ResultResponse;
import com.oax.entity.front.Kline;
import com.oax.service.KlineService;
import com.oax.vo.KlineVO;

@RestController
@RequestMapping("kline")
public class KlineController {
	
	@Autowired
	private KlineService klineService;
	
	@PostMapping("findListByMarketId")
	public ResultResponse findListByMarketId(@RequestBody KlineVO vo) {
		List<Kline> list = klineService.findListByMarketId(vo);
		return new ResultResponse(true, list);
	}

	@PostMapping("findListByMarketIdNew")
	public ResultResponse findListByMarketIdNew(@RequestBody KlineVO vo) {
		List<List<Object>> list = klineService.findListByMarketIdNew(vo);
		return new ResultResponse(true, list);
	}

}
