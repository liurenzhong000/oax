package com.oax;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oax.exception.VoException;
import com.oax.service.OrdersService;
import com.oax.vo.OrdersVO;

/** 
* @ClassName:：MatchTest 
* @Description： 测试类
* @author ：xiangwh  
* @date ：2018年7月2日 上午11:56:32 
*  
*/
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes = OaxApiApplication.class)  
public class MatchTest {
	@Autowired
	private  OrdersService service;
	
	//下卖出订单
	@Test
	public void  addSellOrders() throws VoException{
		String lang = "cn";
		OrdersVO vo = new OrdersVO();
		vo.setUserId(2);
		vo.setMarketId(4);
		vo.setQty("100");
		vo.setPrice("0.1");
		vo.setType(2);
		vo.setTransactionPassword("123456");
		vo.setMachineType(2);
		for (int i = 0; i < 1; i++) {
			Integer orderId = service.save(vo, lang,0);
			if (orderId!=null) {
				System.out.println("下卖出订单成功");
			}			
		}
	}
	
	
	//下买入订单
	@Test
	public void  addBuyOrders() throws VoException{
		String lang = "cn";
		OrdersVO vo = new OrdersVO();
		vo.setUserId(2);
		vo.setMarketId(4);
		vo.setQty("100");
		vo.setPrice("0.1");
		vo.setType(1);
		vo.setTransactionPassword("123456");
		vo.setMachineType(2);
		Integer orderId = service.save(vo, lang,0);
		if (orderId!=null) {
			System.out.println("下买入订单成功");
		}
	}
	
}
