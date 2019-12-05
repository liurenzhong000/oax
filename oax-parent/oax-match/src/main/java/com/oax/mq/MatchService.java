package com.oax.mq;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.oax.entity.front.Orders;
import com.oax.entity.front.Trade;
import com.oax.mapper.front.OrdersMapper;
import com.oax.mapper.front.TradeMapper;
import com.oax.mapper.front.UserCoinMapper;

/** 
* @ClassName:：MatchService 
* @Description： 处理撮合业务的service
* @author ：xiangwh  
* @date ：2018年6月26日 下午10:26:00 
*  
*/

@Service
public class MatchService {
	
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private TradeMapper tradeMapper;
	@Autowired
	private UserCoinMapper userCoinMapper;

	@Transactional
	public boolean matchByBuyOrder(Orders indexOrder,BigDecimal tradeFeeRate) {
		Orders top1SellOrder=ordersMapper.getMatchOrder(indexOrder.getMarketId(),2,indexOrder.getId());
		
		//如果被撮合方没数据直接跳出
		if(top1SellOrder==null) return false;
		//如果卖1价格>买1价格直接跳出
		if(top1SellOrder.getPrice().compareTo(indexOrder.getPrice())==1) return false;
		
		//主动撮合的订单未成交数量
		BigDecimal indexWaitTradeQty = indexOrder.getQty().subtract(indexOrder.getTradeQty());	
		
		//卖1未成交的数量
		BigDecimal top1SellOrderWaitTradeQty = top1SellOrder.getQty().subtract(top1SellOrder.getTradeQty());	
		
		//取数量小的为交易数量
		BigDecimal tradeQty = indexWaitTradeQty.compareTo(top1SellOrderWaitTradeQty)<=0?indexWaitTradeQty:top1SellOrderWaitTradeQty;
		
		BigDecimal buyFee = tradeQty.multiply(tradeFeeRate);
		BigDecimal sellFee = tradeQty.multiply(top1SellOrder.getPrice()).multiply(tradeFeeRate);		
		
		//修改买家订单     保存买家成交记录   修改用户资产信息
		Integer buyRows=ordersMapper.updateTradeQty(indexOrder.getId(), tradeQty, indexOrder.getVersion());
		if(buyRows==1) {
			Trade buyTrade = new Trade(indexOrder.getId(),indexOrder.getMarketId(), 1, top1SellOrder.getId(), top1SellOrder.getPrice(), tradeQty, indexOrder.getLeftCoinId(),buyFee, 
					indexOrder.getType(), indexOrder.getLeftCoinId(), indexOrder.getLeftCoinName(), indexOrder.getRightCoinId(), indexOrder.getRightCoinName(), indexOrder.getUserId());
//			Trade buyTrade = new Trade(indexOrder.getId(),indexOrder.getMarketId(), 1, top1SellOrder.getId(), indexOrder.getPrice(), tradeQty, indexOrder.getLeftCoinId(),buyFee);
			tradeMapper.insert(buyTrade);			
			//用户资产表user_coin
			//修改买家右币冻结资金
			userCoinMapper.updateBuyerFreezing(indexOrder.getUserId(),indexOrder.getRightCoinId(),indexOrder.getPrice().multiply(tradeQty));
			//修改买家右币资产 = 右币资产+(indexOrder.getPrice().multiply(tradeQty)-top1SellOrder.getPrice().multiply(tradeQty))
			userCoinMapper.updateBuyerRightCoinBanlance(indexOrder.getUserId(),indexOrder.getRightCoinId(),tradeQty.multiply(indexOrder.getPrice().subtract(top1SellOrder.getPrice())));
			
			//修改买家左币可用资金
			userCoinMapper.updateBuyerBanlance(indexOrder.getUserId(),indexOrder.getLeftCoinId(),tradeQty.subtract(buyFee));	
			
			
			indexOrder = ordersMapper.getOrderById(indexOrder.getId());
			 
			 if(indexOrder.getQty().compareTo(indexOrder.getTradeQty())==0) {
				 //将主动撮合方标记为已完成
				 ordersMapper.updateStatus(3, indexOrder.getId(),indexOrder.getVersion());	
			 }
			 
		}else{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return true;
		}
		
		//修改卖家订单 保存卖家成交记录
		Integer sellRows = ordersMapper.updateTradeQty(top1SellOrder.getId(), tradeQty, top1SellOrder.getVersion());
		if (sellRows==1) {
			Trade sellTrade = new Trade(top1SellOrder.getId(),indexOrder.getMarketId(), 2, indexOrder.getId(), top1SellOrder.getPrice(), tradeQty, top1SellOrder.getRightCoinId(),sellFee, 
					top1SellOrder.getType(), top1SellOrder.getLeftCoinId(), top1SellOrder.getLeftCoinName(), top1SellOrder.getRightCoinId(), top1SellOrder.getRightCoinName(), top1SellOrder.getUserId());
//			Trade sellTrade = new Trade(top1SellOrder.getId(),indexOrder.getMarketId(), 2, indexOrder.getId(), indexOrder.getPrice(), tradeQty, top1SellOrder.getRightCoinId(),sellFee);
			tradeMapper.insert(sellTrade);
			//左币冻结资金  减少 
			//右币资产 增加  价格*数量 - 手续费
			//修改卖家左币冻结资金 
			userCoinMapper.updateSellerFreezing(top1SellOrder.getUserId(),top1SellOrder.getLeftCoinId(),tradeQty);
			//修改卖家右币可用资金
			userCoinMapper.updateSellerBanlance(top1SellOrder.getUserId(),top1SellOrder.getRightCoinId(),tradeQty.multiply(top1SellOrder.getPrice()).subtract(sellFee));
			
			
			top1SellOrder = ordersMapper.getOrderById(top1SellOrder.getId());
			 
			 if (top1SellOrder.getQty().compareTo(top1SellOrder.getTradeQty())==0) {				
					//将卖1订单标记为已完成
					ordersMapper.updateStatus(3, top1SellOrder.getId(),top1SellOrder.getVersion());	
				}	
			 
		}else{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return true;
		}
		
		System.out.println("买单完成撮合,id="+indexOrder.getId()+" 价格："+top1SellOrder.getPrice()+" 数量："+tradeQty+",卖单id="+top1SellOrder.getId());
		
		 
		 return true;
	}
	
	
	@Transactional
	public boolean  matchBySellOrder(Orders indexOrder,BigDecimal tradeFeeRate){
		Orders top1BuyOrder=ordersMapper.getMatchOrder(indexOrder.getMarketId(),1,indexOrder.getId());
		
		//如果被撮合方没数据直接跳出
		if(top1BuyOrder==null) return false;
		//如果卖1价格>买1价格直接跳出
		if(indexOrder.getPrice().compareTo(top1BuyOrder.getPrice())==1) return false;
		
		//主动撮合的订单未成交数量
		BigDecimal indexWaitTradeQty = indexOrder.getQty().subtract(indexOrder.getTradeQty());	
		
		//买1未成交的数量
		BigDecimal top1BuyOrderWaitTradeQty = top1BuyOrder.getQty().subtract(top1BuyOrder.getTradeQty());	
		
		//取数量小的为交易数量
		BigDecimal tradeQty = indexWaitTradeQty.compareTo(top1BuyOrderWaitTradeQty)<=0?indexWaitTradeQty:top1BuyOrderWaitTradeQty;
		//买的手续费=交易量*手续费率     卖的手续费 = 交易量*买家的价格*手续费率
		BigDecimal buyFee = tradeQty.multiply(tradeFeeRate);
		BigDecimal sellFee = tradeQty.multiply(top1BuyOrder.getPrice()).multiply(tradeFeeRate);		
		
		//修改买家订单     保存买家成交记录   修改用户资产信息
		Integer buyRows=ordersMapper.updateTradeQty(top1BuyOrder.getId(), tradeQty, top1BuyOrder.getVersion());
		if(buyRows==1) {
			Trade buyTrade = new Trade(top1BuyOrder.getId(),top1BuyOrder.getMarketId(), 2, indexOrder.getId(), top1BuyOrder.getPrice(), tradeQty, top1BuyOrder.getLeftCoinId(),buyFee, 
					top1BuyOrder.getType(), top1BuyOrder.getLeftCoinId(), top1BuyOrder.getLeftCoinName(), top1BuyOrder.getRightCoinId(), top1BuyOrder.getRightCoinName(), top1BuyOrder.getUserId());
			tradeMapper.insert(buyTrade);
			//用户资产表user_coin
			//修改买家右币冻结资金
			userCoinMapper.updateBuyerFreezing(top1BuyOrder.getUserId(),top1BuyOrder.getRightCoinId(),top1BuyOrder.getPrice().multiply(tradeQty));
			//修改买家左币可用资金
			userCoinMapper.updateBuyerBanlance(top1BuyOrder.getUserId(),top1BuyOrder.getLeftCoinId(),tradeQty.subtract(buyFee));	
			//TODO 资金记录
			top1BuyOrder = ordersMapper.getOrderById(top1BuyOrder.getId());
			 
			 if (top1BuyOrder.getQty().compareTo(top1BuyOrder.getTradeQty())==0) {				
					//将卖1订单标记为已完成
					ordersMapper.updateStatus(3, top1BuyOrder.getId(),top1BuyOrder.getVersion());	
			}
			 
		}else{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return true;
		}
		
		//修改卖家订单 保存卖家成交记录
		Integer sellRows = ordersMapper.updateTradeQty(indexOrder.getId(), tradeQty, indexOrder.getVersion());
		if (sellRows==1) {		
			Trade sellTrade = new Trade(indexOrder.getId(),indexOrder.getMarketId(), 1, top1BuyOrder.getId(), top1BuyOrder.getPrice(), tradeQty, indexOrder.getRightCoinId(),sellFee, 
					indexOrder.getType(), indexOrder.getLeftCoinId(), indexOrder.getLeftCoinName(), indexOrder.getRightCoinId(), indexOrder.getRightCoinName(), indexOrder.getUserId());
			tradeMapper.insert(sellTrade);

			//左币冻结资金  减少 
			//右币资产 增加  价格*数量 - 手续费
			//修改卖家左币冻结资金 
			userCoinMapper.updateSellerFreezing(indexOrder.getUserId(),indexOrder.getLeftCoinId(),tradeQty);
			//修改卖家右币可用资金
			userCoinMapper.updateSellerBanlance(indexOrder.getUserId(),indexOrder.getRightCoinId(),tradeQty.multiply(top1BuyOrder.getPrice()).subtract(sellFee));
			//TODO 资金记录
			indexOrder = ordersMapper.getOrderById(indexOrder.getId());
			 
			 if(indexOrder.getQty().compareTo(indexOrder.getTradeQty())==0) {
				 //将主动撮合方标记为已完成
				 ordersMapper.updateStatus(3, indexOrder.getId(),indexOrder.getVersion());	
			 }
		}else{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return true;
		}
		
		System.out.println("卖单完成撮合 id="+indexOrder.getId()+" ,价格："+top1BuyOrder.getPrice()+" 数量："+tradeQty+",买单id="+top1BuyOrder.getId());
		 
		 return true;
			 
	}
	@Transactional
	public void cancel() {
		ordersMapper.cancelOrder(14, 2, 4);
		System.out.println(1111);
	}
}
