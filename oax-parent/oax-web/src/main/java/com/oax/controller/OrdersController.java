/**
 * 
 */
package com.oax.controller;


import com.oax.Constant;
import com.oax.common.BaseController;
import com.oax.common.DeviceUtil;
import com.oax.common.RedisUtil;
import com.oax.common.ResultResponse;
import com.oax.context.HttpContext;
import com.oax.entity.front.MarketInfo;
import com.oax.exception.VoException;
import com.oax.service.I18nMessageService;
import com.oax.service.OrdersService;
import com.oax.vo.CancelOrderVO;
import com.oax.vo.OrdersVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/** 
* @ClassName:：OrdersController 
* @Description： 添加订单入口
* @author ：xiangwh  
* @date ：2018年6月7日 上午9:49:16 
*  
*/
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController{
	@Autowired
	private OrdersService ordersService;
	@Autowired
    private I18nMessageService msgService;

	@Autowired
	private RedisUtil redisUtil;

	private final static String LOCK_KEY = "save_order:";
	private final Logger mLogger = LoggerFactory.getLogger(OrdersController.class);
	
	@PostMapping("add")
	public ResultResponse saveOrder(@RequestBody OrdersVO vo,HttpServletRequest request)throws VoException {
		Integer userId = HttpContext.getUserId();
		boolean lock = redisUtil.tryLock(LOCK_KEY + userId);
		if (lock) {
			try {
				return saveOrderWithLock(vo, request);
			} finally {
				redisUtil.unLock(LOCK_KEY + userId);
			}
		} else {
			throw new IllegalArgumentException("重复提交！");
		}
	}

	private ResultResponse saveOrderWithLock(OrdersVO vo,HttpServletRequest request) throws VoException {
		String msg = null;
		//1.校验是否是大于0的数字
		if (StringUtils.isBlank(vo.getPrice())) {
			msg = msgService.getMsg(10049, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(false,msg);
		}
		if (StringUtils.isBlank(vo.getQty())) {
			msg = msgService.getMsg(10050, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(false,msg);
		}
		BigDecimal priceParam = null;
		BigDecimal qtyParam = null;
		try {
			priceParam = new BigDecimal(vo.getPrice());
		} catch (Exception e) {
			msg = msgService.getMsg(10051, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(false, msg);
		}
		try {
			qtyParam = new BigDecimal(vo.getQty());
		} catch (Exception e) {
			msg = msgService.getMsg(10052, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(false, msg);
		}

		if (priceParam.compareTo(BigDecimal.ZERO)<=0) {
			msg = msgService.getMsg(10051, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(false, msg);
		}
		if (qtyParam.compareTo(BigDecimal.ZERO)<=0) {
			msg = msgService.getMsg(10052, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(false, msg);
		}
		//2.价格精度，数量精度校验
		MarketInfo marketInfo = ordersService.getPrecision(vo.getMarketId());
		Integer priceDecimals = marketInfo.getPriceDecimals();
		Integer qtyDecimals = marketInfo.getQtyDecimals();
		BigDecimal minPrice = BigDecimal.ONE.divide(BigDecimal.TEN.pow(priceDecimals));
		BigDecimal minQty = BigDecimal.ONE.divide(BigDecimal.TEN.pow(qtyDecimals));
//		if (vo.getMarketId() == 110 && System.currentTimeMillis() <= 1548072000000L){
//			msg = msgService.getMsg(10116, request.getHeader(Constant.api_header_lang));
//			return new ResultResponse(false, msg);
//		}
		String price = vo.getPrice();
//		mLogger.info("---------------------------------标准比例价格"+price+"------------------------------------------");
		String qty = vo.getQty();
		if (price.indexOf(".")>0) {
			Integer priceLength = price.substring(price.indexOf(".")+1).length();
			//如果价格小数点精度过大
			if (priceLength>priceDecimals) {
				msg = msgService.getMsg(10066, request.getHeader(Constant.api_header_lang));
				String msg1 = msgService.getMsg(10068, request.getHeader(Constant.api_header_lang));
				msg = msg + priceDecimals+msg1;
				return new ResultResponse(false, msg);
			}
		}
		if (qty.indexOf(".")>0) {
			Integer qtyLength = qty.substring(qty.indexOf(".")+1).length();
			//如果数量小数点精度过大
			if (qtyLength>qtyDecimals) {
				msg = msgService.getMsg(10067, request.getHeader(Constant.api_header_lang));
				String msg1 = msgService.getMsg(10068, request.getHeader(Constant.api_header_lang));
				msg = msg + qtyDecimals+msg1;
				return new ResultResponse(false, msg);
			}
		}
		if (priceParam.compareTo(minPrice)<0) {
			msg = msgService.getMsg(10042, request.getHeader(Constant.api_header_lang));
			msg = msg + " " + String.format("%."+priceDecimals+"f", minPrice);
			return new ResultResponse(false, msg);
		}
		if (qtyParam.compareTo(minQty)<0) {
			msg = msgService.getMsg(10043, request.getHeader(Constant.api_header_lang));
			msg = msg + " " + String.format("%."+qtyDecimals+"f", minQty);
			return new ResultResponse(false, msg);
		}

		//添加订单
		vo.setUserId((Integer.valueOf(request.getHeader("userId"))));
		vo.setMachineType(DeviceUtil.getPlatform(request));
		ResultResponse result = ordersService.checkUserCoin(vo, request.getHeader(Constant.api_header_lang));
		boolean checkFlag = result.isSuccess();
		Integer version = Integer.valueOf(result.getCode());
		Integer orderId = null;
		ordersService.insertUserCoin(vo);
		if (checkFlag) {
			orderId = ordersService.save(vo,request.getHeader(Constant.api_header_lang),version);
		}
		if (orderId!=null) {
			int code = vo.getType()==1?10059:10060;
			msg = msgService.getMsg(code, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(true, msg);
		}else {
			msg = msgService.getMsg(10100, request.getHeader(Constant.api_header_lang));
			return new ResultResponse(false, msg);
		}
	}
	
	@GetMapping("/cancel/{id}")
	public ResultResponse cancelOrder(@PathVariable(name="id") int id,@RequestHeader int userId,HttpServletRequest request) throws VoException {
		CancelOrderVO vo = new CancelOrderVO();
		vo.setId(id);
		vo.setUserId(userId);
		boolean flag = ordersService.cancelOrder(vo);
		String msg = null;
		if (flag) {
			System.out.println(request.getHeader(Constant.api_header_lang));
			msg = msgService.getMsg(10033, request.getHeader(Constant.api_header_lang));
		}else {
			msg = msgService.getMsg(10100, request.getHeader(Constant.api_header_lang));
		}
		return new ResultResponse(flag, msg);
	}
	public static void main(String[] args) {
//		double pow = Math.pow(10, -6);
//		
//		System.out.println(String.format("%."+6+"f", pow));
//		System.out.println(pow);
//		BigDecimal big = new BigDecimal(10);
//		System.out.println(BigDecimal.ONE);
//		System.out.println(BigDecimal.TEN);
//		System.out.println(BigDecimal.ONE.divide(big.pow(8)));
		String a = "5.2314234324";
		System.out.println(a.substring(a.indexOf(".")));
	}
}
