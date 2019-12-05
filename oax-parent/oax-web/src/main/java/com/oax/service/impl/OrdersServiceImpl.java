/**
 * 
 */
package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oax.common.MD5;
import com.oax.common.ResultResponse;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.service.UserCoinDetailService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.oax.Constant;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.Coin;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.MarketOrders;
import com.oax.entity.front.Member;
import com.oax.entity.front.Orders;
import com.oax.entity.front.UserCoin;
import com.oax.exception.VoException;
import com.oax.mapper.front.CoinMapper;
import com.oax.mapper.front.OrdersMapper;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.I18nMessageService;
import com.oax.service.OrdersService;
import com.oax.service.mq.ProRabbitMQService;
import com.oax.vo.CancelOrderVO;
import com.oax.vo.OrdersMqVO;
import com.oax.vo.OrdersVO;

/** 
* @ClassName:：OrdersServiceImpl 
* @Description： 托管订单service实现类
* @author ：xiangwh  
* @date ：2018年6月7日 上午9:28:37 
*  
*/
@Service
public class OrdersServiceImpl implements OrdersService {
	@Autowired
	private OrdersMapper mapper;
	@Autowired
	private UserCoinMapper userCoinMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ProRabbitMQService mqService;
	@Autowired
    private I18nMessageService msgService;
	@Autowired
	private CoinMapper coinMapper;
	@Autowired
	private UserCoinDetailService userCoinDetailService;
	@Autowired
	private CommonCheckServiceImpl commonCheckService;
	
	@Transactional
	@DataSource(DataSourceType.MASTER)
	@Override
	public Integer save(OrdersVO vo,String lang,Integer version) throws VoException{
		commonCheckService.checkFreezing(vo.getUserId());
		Map<String,Object> orderMap= (Map<String,Object>)redisUtil.getObject(RedisKeyEnum.MARKET_ORDERS_MAP.getKey() + vo.getMarketId(), Map.class);
		Orders orders = new Orders();
		orders.setMarketId(vo.getMarketId());
		orders.setType(vo.getType());
		orders.setPrice(new BigDecimal(vo.getPrice()));
		orders.setQty(new BigDecimal(vo.getQty()));
		orders.setUserId(vo.getUserId());
		//获取币的基本信息
		Orders coinOrders =  this.getCoinInfo(vo.getMarketId());
		orders.setLeftCoinId(coinOrders.getLeftCoinId());
		orders.setLeftCoinName(coinOrders.getLeftCoinName());
		orders.setRightCoinId(coinOrders.getRightCoinId());
		orders.setRightCoinName(coinOrders.getRightCoinName());
		//查出订单的深度排行
		//如果是买单  查询市场买单深度数据
		List<MarketOrders> marketOrdersList = null;
		if(vo.getType()==1){
			marketOrdersList = JSON.parseArray(orderMap.get("buyList").toString(), MarketOrders.class);
			if (marketOrdersList==null||marketOrdersList.size()==0){
				orders.setRank(1);
			}else{
				for (int i = 0; i <marketOrdersList.size() ; i++) {
					MarketOrders marketOrders = marketOrdersList.get(i);
					if(new BigDecimal(vo.getPrice()).compareTo(marketOrders.getPrice())>=0){
						orders.setRank(i+1);
						break;
					}
				}
				if(orders.getRank()==null&&marketOrdersList.size()<15){
					orders.setRank(marketOrdersList.size()+1);
				}
			}
			//如果是卖单 查询市场卖单的深度
		}else{
			marketOrdersList = JSON.parseArray(orderMap.get("sellList").toString(), MarketOrders.class);
			if (marketOrdersList==null||marketOrdersList.size()==0){
				orders.setRank(1);
			}else{
				for (int i = marketOrdersList.size()-1; i >=0 ; i--) {
					MarketOrders marketOrders = marketOrdersList.get(i);
					if (new BigDecimal(vo.getPrice()).compareTo(marketOrders.getPrice())<=0){
						orders.setRank(marketOrdersList.size()-i);
						break;
					}
				}
				if(orders.getRank()==null&&marketOrdersList.size()<15){
					orders.setRank(marketOrdersList.size()+1);
				}
			}
		}
		UserCoin oldUserCoin = null;
		if (orders.getType() == 1) {//用户买入
			oldUserCoin = userCoinMapper.selectByUserIdAndCoinId(vo.getUserId(), orders.getRightCoinId());
		} else if (orders.getType() == 2) {//用户卖出
			oldUserCoin = userCoinMapper.selectByUserIdAndCoinId(vo.getUserId(), orders.getLeftCoinId());
		}
		//判断数据版本号，保证数据一致性
		if(oldUserCoin.getVersion().equals(version)){
			Integer rowNum = mapper.insert(orders);
			if (rowNum!=null && rowNum>0) {
				//根据type是买入还是卖出，修改用户该币种的冻结与余额信息
				Integer updateRow = userCoinMapper.updateUserCoin(orders);
				//修改用户资产信息成功，推送mq，保存资金变更记录
				if (updateRow!=null && updateRow>0) {
					//资金变更记录 TODO user_coin 锁
					addUserCoinDetail(oldUserCoin, orders);
					// 推送订单信息到mq中
					OrdersMqVO mqVO = new OrdersMqVO();
					mqVO.setId(orders.getId());
					mqVO.setType(orders.getType());
					mqVO.setMarketId(vo.getMarketId());
					mqService.sendOrder(mqVO);
				}
			}
		}
		return orders.getId();
	}

	private void addUserCoinDetail(UserCoin oldUserCoin, Orders orders) {
		if (oldUserCoin == null) {
			return;
		}
		if (orders.getType() == 1) {//用户买入，冻结rightCoinId右币
			BigDecimal change = orders.getPrice().multiply(orders.getQty());
			userCoinDetailService.addUserCoinDetail(oldUserCoin, orders.getId()+"", UserCoinDetailType.BUY_ENTRUST);
		} else if (orders.getType() == 2) {//用户卖出，冻结leftCoinId左币
			userCoinDetailService.addUserCoinDetail(oldUserCoin, orders.getId()+"", UserCoinDetailType.SELL_ENTRUST);
		}

	}

	@Transactional
	@DataSource(DataSourceType.MASTER)
	@Override
	public boolean cancelOrder(CancelOrderVO vo) throws VoException{
		//修改订单状态前线查询该订单   进行数据校验
		Orders orders = mapper.getOrderById(vo.getId());
		//判断已成交量是否小于订单数量
		BigDecimal qty = orders.getQty();
		BigDecimal tradeQty = orders.getTradeQty();
		//如果订单不是这三种状态或者 已成交量>=订单量 不允许撤单
		if (qty.compareTo(tradeQty)<=0 || (orders.getStatus()!=0 && orders.getStatus()!=1 && orders.getStatus()!=2)) {
			return false;
		}
		// 撤回订单
		// 1.修改订单表状态 
		Integer cancelRows = mapper.cancelOrder(vo.getId(),vo.getUserId(),orders.getVersion());
		if (cancelRows!=null&&cancelRows>0) {
			BigDecimal  unsettledAmount = null;
			//如果撤销订单的类型是买入    修改 右币资金 和 右币冻结资金
			if (orders.getType()==1) {
				//未成交的币金额
				unsettledAmount = orders.getPrice().multiply(orders.getQty().subtract(orders.getTradeQty()));
				//资金记录 //TODO user_coin 锁
				Integer updateUserCoin = mapper.updateBuyUserCoin(unsettledAmount,orders.getUserId(),orders.getMarketId());
				if (updateUserCoin!=null&&updateUserCoin>0) {
					UserCoin oldUserCoin = userCoinMapper.selectByUserIdAndCoinId(vo.getUserId(), orders.getRightCoinId());
					userCoinDetailService.addUserCoinDetail(oldUserCoin, orders.getId()+"", UserCoinDetailType.BUY_ENTRUST);
					return true;
				}
			}
			//如果撤销订单的类型是卖出    修改 左币资金 和左币冻结资金
			if (orders.getType()==2) {
				//未成交的币的金额
				unsettledAmount = orders.getQty().subtract(orders.getTradeQty());
				//资金记录 //TODO user_coin 锁
				Integer updateUserCoin = mapper.updateSellUserCoin(unsettledAmount, orders.getUserId(),orders.getMarketId());
				if (updateUserCoin!=null&&updateUserCoin>0) {
					UserCoin oldUserCoin = userCoinMapper.selectByUserIdAndCoinId(vo.getUserId(), orders.getLeftCoinId());
					userCoinDetailService.addUserCoinDetail(oldUserCoin, orders.getId()+"", UserCoinDetailType.BUY_ENTRUST);
					return true;
				}
			}
		}		
		return false;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> getMarketOrdersList(int marketId) {
		String redisKey = RedisKeyEnum.MARKET_ORDERS_MAP.getKey()+marketId;
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>)redisUtil.getObject(redisKey, Map.class);
		if (result==null || result.size()==0) {
			result = new HashMap<>();
			//查买入 
			List<MarketOrders> buyList = mapper.findMarketOrdersList(marketId,1);
			//查卖出
			List<MarketOrders> sellList = mapper.findMarketOrdersList(marketId,2);
			Collections.reverse(sellList); 
			
			result.put("buyList", buyList);
			result.put("sellList", sellList);
			
			redisUtil.setObject(redisKey, result,Constant.REDISTIME_ONEDAY);
		}	
		return result;
	}
	
	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> getMarketOrdersListFromDB(int marketId) {
		Map<String, Object> result = new HashMap<>();
		//查买入 
		List<MarketOrders> buyList = mapper.findMarketOrdersList(marketId,1);
		//查卖出
		List<MarketOrders> sellList = mapper.findMarketOrdersList(marketId,2);
		Collections.reverse(sellList);
		
		result.put("buyList", buyList);
		result.put("sellList", sellList);
		
		return result;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Map<String, String>> getOrdersByUserId(Integer userId,Integer marketId) {
		List<Map<String, String>> list = userCoinMapper.getOrdersByUserId(marketId,userId);
		return list;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public MarketInfo getPrecision(int marketId) {
		List<MarketInfo> marketInfoList = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
		MarketInfo marketInfo = getMarketInfoFromList(marketInfoList, marketId);
		if (marketInfo==null) {
			marketInfo = mapper.getPrecision(marketId);
		}
		return marketInfo;
	}
	
	public MarketInfo getMarketInfoFromList(List<MarketInfo> list,int marketId) {
		MarketInfo marketInfo = null;
		if(list!=null&&list.size()!=0) {
			for (MarketInfo info : list) {
				if (marketId==info.getMarketId()) {
					marketInfo = info;
					break;
				}
			}
		}
		return marketInfo;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public String isNeedTransactionPassword(Integer userId) {	
		return mapper.isNeedTransactionPassword(userId);
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public ResultResponse checkUserCoin(OrdersVO vo, String lang) throws VoException {
		ResultResponse resultResponse = new ResultResponse();
		boolean flag = false;
		String msg = null;
		UserCoin userCoin = userCoinMapper.selectUserCoin(vo.getUserId(),vo.getMarketId(), vo.getType());
		if (userCoin != null) {
			resultResponse.setCode(userCoin.getVersion().toString());
		}
		//买入
		if (vo.getType()==1) {
			if (userCoin==null||new BigDecimal(vo.getPrice()).multiply(new BigDecimal(vo.getQty())).compareTo(userCoin.getBanlance())>0) {
				msg = msgService.getMsg(10044, lang);
				throw new VoException(msg);
			}
		}
		//卖出
		if (vo.getType()==2) {
			if (userCoin==null||new BigDecimal(vo.getQty()).compareTo(userCoin.getBanlance())>0) {
				msg = msgService.getMsg(10044, lang);
				throw new VoException(msg);
			}
		}
		//如果是app不需要校验密码 1是pc  2是app端
		if (vo.getMachineType()==2) {
			flag = true;
		}else {
			//根据id 查询用户id查询用户基本信息，判断是否开启交易密码检测 如果没开启，则不需要验证交易密码
			Member member = mapper.getUserInfo(vo.getUserId());
			//如果开启交易密码
			if (member.getNeedTransactionPassword()==1) {
				String transactionPassword = MD5.encrypt(vo.getTransactionPassword());
				//如果交易密码正确
				if (transactionPassword.equals(member.getTransactionPassword())) {
					flag = true;
				}else {
					msg = msgService.getMsg(10065, lang);
					throw new VoException(msg);
				}
			}else {
				flag = true;
			}
			
		}
		resultResponse.setSuccess(flag);
		return resultResponse;
	}

	@DataSource(DataSourceType.MASTER)
	@Override
	public void insertUserCoin(OrdersVO vo) {
		/*如：X/ETH  如果是买  查询用户资产表是否有左币X的记录，如果没有，则插入用户X的资产数据
		  如果是卖  查询用户是否有右币ETH的记录，如果没有则插入*/
		Integer row = userCoinMapper.getUserCoinRecord(vo.getUserId(),vo.getMarketId(),vo.getType());
		//如果没有记录
		if (row==0) {
			//查询地址
			Coin coin  = coinMapper.getCoinByMarketId(vo.getMarketId(),vo.getType());
			//查询用户资产表中是否有对应的同类型的币的地址或者密码
			UserCoin uc=new UserCoin();
			uc.setUserId(vo.getUserId());
			uc.setCoinId(coin.getId());							
			uc.setBanlance(new BigDecimal("0"));
			uc.setFreezingBanlance(new BigDecimal("0"));
			uc.setCreateTime(new Date());
			uc.setUpdateTime(new Date());
			userCoinMapper.insertSelective(uc);	
		}
		
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Orders getCoinInfo(Integer marketId) {
		return mapper.getCoinInfo(marketId);
	}

}
