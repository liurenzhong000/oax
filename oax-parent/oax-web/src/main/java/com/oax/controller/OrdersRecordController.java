package com.oax.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.common.BaseController;
import com.oax.common.ResultResponse;
import com.oax.entity.front.BargainOrdersRecord;
import com.oax.entity.front.OrdersRecord;
import com.oax.exception.VoException;
import com.oax.service.OrdersRecordService;
import com.oax.vo.OrdersQureyVO;
import com.oax.vo.TradeVO;

@RestController
@RequestMapping("ordersRecord")
public class OrdersRecordController extends BaseController{
	@Autowired
	private OrdersRecordService ordersRecordService;
	

	/**
	 * @return
	 * @throws VoException
	 */
	@PostMapping("findListByPage")
	public ResultResponse findListByPage(@RequestBody OrdersQureyVO vo,@RequestHeader int userId) throws VoException {
		vo.setUserId(userId);
		PageInfo<OrdersRecord> page = ordersRecordService.findListByPage(vo);
		return new ResultResponse(true, page);
	}
	@PostMapping("exportExcelOrders")
	public void exportExcel(@RequestBody OrdersQureyVO vo,
			@RequestHeader int userId) {
		vo.setUserId(userId);
		List<OrdersRecord> list = ordersRecordService.findList(vo);
		this.setHeadDownloadExcel("ordersRecord", OrdersRecord.class, list);
	}
	
	@PostMapping("findTradeOrderListByPage")
	public ResultResponse findTradeOrderListByPage(@RequestBody TradeVO vo,@RequestHeader int userId) throws VoException{
		vo.setUserId(userId);
		PageInfo<BargainOrdersRecord> page = ordersRecordService.findTradeOrderListByPage(vo);
		return new ResultResponse(true, page);
	}
}
