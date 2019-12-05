/**
 * 
 */
package com.oax.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.BargainOrdersRecord;
import com.oax.entity.front.OrdersRecord;
import com.oax.mapper.front.BargainOrdersRecordMapper;
import com.oax.mapper.front.OrdersRecordMapper;
import com.oax.service.OrdersRecordService;
import com.oax.vo.OrdersQureyVO;
import com.oax.vo.TradeVO;

/** 
* @ClassName:：OrdersRecordServiceImpl 
* @Description： 委托管理
* @author ：xiangwh  
* @date ：2018年6月7日 下午5:19:18 
*  
*/
@Service
public class OrdersRecordServiceImpl implements OrdersRecordService {
	@Autowired
	private OrdersRecordMapper mapper;
	@Autowired
	private BargainOrdersRecordMapper bargainMapper;
	
	@DataSource(DataSourceType.SLAVE)
	@Override
	public List<OrdersRecord> findList(OrdersQureyVO vo) {
		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", vo.getBeginDate());
		params.put("endDate", vo.getEndDate());
		params.put("marketId", vo.getMarketId());
		params.put("type", vo.getType());
		params.put("status", vo.getStatus());
		params.put("userId", vo.getUserId());
		List<OrdersRecord> list = mapper.findListByUser(params);
		return list;
	}
	
	@DataSource(DataSourceType.SLAVE)
	@Override
	public PageInfo<OrdersRecord> findListByPage(OrdersQureyVO vo) {
		PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", vo.getBeginDate());
		params.put("endDate", vo.getEndDate());
		params.put("marketId", vo.getMarketId());
		params.put("type", vo.getType());
		params.put("status", vo.getStatus());
		params.put("userId", vo.getUserId());
		List<OrdersRecord> list = mapper.findListByUser(params);
		return new PageInfo<>(list);
	}
	
	@DataSource(DataSourceType.SLAVE)
	@Override
	public PageInfo<BargainOrdersRecord> findTradeOrderListByPage(TradeVO vo) {
		PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", vo.getBeginDate());
		params.put("endDate", vo.getEndDate());
		params.put("marketId", vo.getMarketId());
		params.put("type", vo.getType());
		params.put("userId", vo.getUserId());
//		params.put("leftCoinName", vo.getLeftCoinName());
//		params.put("rightCoinId", vo.getRightCoinId());
		List<BargainOrdersRecord> list = bargainMapper.findListByUser(params);
		return new PageInfo<>(list);
	}
	
	

}
