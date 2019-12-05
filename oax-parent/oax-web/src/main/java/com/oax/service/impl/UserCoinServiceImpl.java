package com.oax.service.impl;

import com.alibaba.fastjson.JSON;
import com.oax.common.*;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.common.enums.SysConfigEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.*;
import com.oax.exception.VoException;
import com.oax.mapper.front.*;
import com.oax.service.*;
import com.oax.vo.CoinListVO;
import com.oax.vo.UserCoinPropertyListVO;
import com.oax.vo.UserCoinTotalVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName:：UserCoinServiceImpl
 * @author ：xiangwh
 * @date ：2018年6月6日 下午9:31:41
 * 
 */
@Service
public class UserCoinServiceImpl implements UserCoinService {
	@Autowired
	private UserCoinMapper mapper;
	@Autowired
	private CoinMapper coinMapper;

	@Autowired
	private TradeMapper tradeMapper;

	@Autowired
	private WithdrawMapper withdrawMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private MarketMapper marketMapper;

	@Autowired
	private MarketCategoryMapper marketCategoryMapper;

	@Autowired
	private I18nMessageService I18nMessageService;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private RechargeAddressMapper rechargeAddressMapper;
	
	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private MovesayMoneyActiveService movesayMoneyActiveService;

	@Autowired
	private CommonCheckService commonCheckService;

	@Autowired
	private UserCoinDetailService userCoinDetailService;

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> getUserCoinMap(Integer marketId, Integer userId) {
		// 存放结果集
		Map<String, Object> map = new HashMap<>();
		// 存放参数
		Map<String, String> coinBalance = mapper.getUserCoin(marketId, userId);
		// 获取市场对信息
		List<Map<String, String>> orderList = mapper.getOrdersByUserId(marketId, userId);
		map.put("coinBalance", coinBalance);
		map.put("orderList", orderList);
		return map;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> coinList(CoinListVO vo) {
		Map<String, Object> map = new HashMap<>();
		List<UserCoinInfo> list = new ArrayList<>();
		Member member = memberMapper.selectByPrimaryKey(vo.getUserId());

		// 查询当天用户已提现额度
		BigDecimal useredWithdrawal = withdrawMapper.queryUseredWithdraw(vo.getUserId());
		// 查询用户不同等级总额度
		BigDecimal LEVEL1_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL1_BTC.getName()));
		BigDecimal LEVEL2_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL2_BTC.getName()));
		BigDecimal LEVEL3_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL3_BTC.getName()));

		// 获取ethPrice,cnyPrice,usdtPrice   用户转换为各个币种的总值
		Map<String, BigDecimal> exchangePrice = mapper.selectEthPrice(vo.getUserId(), vo.getCoinName());
		BigDecimal cnyPrice = new BigDecimal("0");

		if (exchangePrice != null) {
			cnyPrice = exchangePrice.get("cnyPrice");
			// 获取eth/btc市场的最新价格
			BigDecimal usdtBtcPrice = mapper.getLastPriceUsdtBtc();
			BigDecimal price=mapper.getPrice();
			if (usdtBtcPrice == null) {
				usdtBtcPrice = new BigDecimal("0");
			}
			exchangePrice.put("btcPrice", cnyPrice.divide(price.multiply(usdtBtcPrice),6, BigDecimal.ROUND_HALF_UP));
		} else {
			exchangePrice = new HashMap<>();
			exchangePrice.put("btcPrice", new BigDecimal("0"));
			exchangePrice.put("ethPrice", new BigDecimal("0"));
			exchangePrice.put("cnyPrice", new BigDecimal("0"));
			exchangePrice.put("usdtPrice", new BigDecimal("0"));
		}
		if (vo.getType() == 1) {
			list = mapper.selectByUserId(vo.getUserId(), vo.getCoinName());
		} else {
			list = mapper.selectPropertyByUserIdAndCoinId(vo.getUserId(), null, vo.getCoinName());
		}

		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				UserCoinInfo userCoinInfo=list.get(i);
				List<Map<String, Object>> marketList=this.toTrade(userCoinInfo.getId());
				userCoinInfo.setTradeList(marketList);
			}
		}

		if (member.getLevel() == 1) {
			map.put("withdrawalAmount", LEVEL1_BTC);
		} else if (member.getLevel() == 2) {
			map.put("withdrawalAmount", LEVEL2_BTC);
		} else if (member.getLevel() == 3) {
			map.put("withdrawalAmount", LEVEL3_BTC);
		}
		map.put("useredWithdrawal", useredWithdrawal);
		map.put("coinList", list);
		map.put("total", exchangePrice);
		return map;
	}

	/**
	 * 重写用户资金获取
	 * @param vo
	 * @return
	 */
	@Override
	@DataSource(DataSourceType.SLAVE)
	public UserCoinPropertyListVO coinListNew(CoinListVO vo) {
		//获取已提现金额和等级提现金额数据
		Member member = memberMapper.selectByPrimaryKey(vo.getUserId());
		// 查询当天用户已提现额度
		BigDecimal useredWithdrawal = withdrawMapper.queryUseredWithdraw(vo.getUserId());
		// 查询用户不同等级总额度
		BigDecimal LEVEL1_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL1_BTC.getName()));
		BigDecimal LEVEL2_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL2_BTC.getName()));
		BigDecimal LEVEL3_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL3_BTC.getName()));
		BigDecimal withdrawalAmount = BigDecimal.ZERO;
		if (member.getLevel() == 1) {
			withdrawalAmount = LEVEL1_BTC;
		} else if (member.getLevel() == 2) {
			withdrawalAmount = LEVEL2_BTC;
		} else if (member.getLevel() == 3) {
			withdrawalAmount = LEVEL3_BTC;
		}

		//ETH/USDT 的比例
		BigDecimal ethUsdtRatio = tradeMapper.getRatioByLeftIdAndRightId(1, 10);
		if (ethUsdtRatio == null) {
			ethUsdtRatio = BigDecimal.ZERO;
		}
		//BTC/USDT 的比例
		BigDecimal btcUsdtRatio = tradeMapper.getRatioByLeftIdAndRightId(2, 10);
		if (btcUsdtRatio == null) {
			btcUsdtRatio = BigDecimal.ZERO;
		}
		//USDT,BTC,ETH的人民币价格 T
		BigDecimal usdtCnyPrice = marketCategoryMapper.getUsdtCnyPrice();
		if (usdtCnyPrice == null) {
			usdtCnyPrice = BigDecimal.ZERO;
		}
		BigDecimal ethCnyPrice = ethUsdtRatio.multiply(usdtCnyPrice);
		BigDecimal btcCnyPrice = btcUsdtRatio.multiply(usdtCnyPrice);

		//获取用户的资金列表
		List<UserCoin> userCoins = new ArrayList<>();
		if (vo.getType() == 1) {
			userCoins = mapper.selectAllUserCoinByUserId(vo.getUserId(), vo.getCoinName());
		} else {
			userCoins = mapper.selectUserCoinByUserIdNoZero(vo.getUserId(), vo.getCoinName());
		}
//		List<UserCoin> userCoins = mapper.selectUserCoinByUserIdNoZero(member.getId());
		List<UserCoinInfo> userCoinInfoList = new ArrayList<>();
		BigDecimal allCnyValue = BigDecimal.ZERO;
		for (UserCoin userCoin : userCoins) {
			Integer coinId = userCoin.getCoinId();
			BigDecimal qty = userCoin.getBanlance().add(userCoin.getFreezingBanlance());
			//获取该币的市场
			CoinWithBLOBs coinWithBLOBs = coinMapper.selectById(coinId);
			Integer coinType = coinWithBLOBs.getType();//币种类型：1 ETH 2 BTC 3 EHT_TOKEN 4 USDT
			BigDecimal cnyValue = BigDecimal.ZERO;
			if (coinType == 1) {
				cnyValue = qty.multiply(ethCnyPrice);
			} else if (coinType == 2) {
				cnyValue = qty.multiply(btcCnyPrice);
			} else if (coinType == 4) {//为usdt
				cnyValue = qty.multiply(usdtCnyPrice);
			} else {
				//获取该币在USDT交易区的最新价格
				BigDecimal coinUsdtRatio = tradeMapper.getRatioByLeftIdAndRightId(coinId, 10);
				if (coinUsdtRatio == null) {
					coinUsdtRatio = BigDecimal.ZERO;
				}
				cnyValue = qty.multiply(coinUsdtRatio).multiply(usdtCnyPrice);
			}

			//完善userCoinInfoList
			UserCoinInfo userCoinInfo = new UserCoinInfo();

			userCoinInfo.setAllowRecharge(coinWithBLOBs.getAllowRecharge());
			userCoinInfo.setAllowWithdraw(coinWithBLOBs.getAllowWithdraw());
			userCoinInfo.setBanlance(userCoin.getBanlance());
			userCoinInfo.setFreezingBanlance(userCoin.getFreezingBanlance());
			userCoinInfo.setFullName(coinWithBLOBs.getFullName());
			userCoinInfo.setImage(coinWithBLOBs.getImage());
			userCoinInfo.setMaxOutQty(coinWithBLOBs.getMaxOutQty());
			userCoinInfo.setMinOutQty(coinWithBLOBs.getMinOutQty());
			userCoinInfo.setShortName(coinWithBLOBs.getShortName());
			userCoinInfo.setType(coinWithBLOBs.getType());
			userCoinInfo.setUserId(userCoin.getUserId());
			userCoinInfo.setWithdrawFee(coinWithBLOBs.getWithdrawFee());
//			BeanHepler.copySrcToDest(coinWithBLOBs, userCoinInfo);
//			BeanHepler.copySrcToDest(userCoin, userCoinInfo);


			userCoinInfo.setId(coinId);
			userCoinInfo.setBtcPrice(cnyValue.divide(btcCnyPrice, 10, BigDecimal.ROUND_HALF_UP));
			userCoinInfo.setCnyPrice(cnyValue);
			userCoinInfo.setTotalBanlance(qty);
			userCoinInfo.setTradeList(marketMapper.selectByCoinId(coinId));
			userCoinInfo.setUsdtPrice(cnyValue.divide(usdtCnyPrice, 10, BigDecimal.ROUND_HALF_UP));
			userCoinInfo.setUseredWithdrawal(useredWithdrawal);
			userCoinInfo.setWithdrawalAmount(withdrawalAmount);

			allCnyValue = allCnyValue.add(cnyValue);
			userCoinInfoList.add(userCoinInfo);
		}
		BigDecimal btcPrice = allCnyValue.divide(btcCnyPrice, 10, BigDecimal.ROUND_HALF_UP);
		BigDecimal cnyPrice = allCnyValue;
		BigDecimal ethPrice = allCnyValue.divide(ethCnyPrice, 10, BigDecimal.ROUND_HALF_UP);
		BigDecimal usdtPrice= allCnyValue.divide(usdtCnyPrice, 10, BigDecimal.ROUND_HALF_UP);
		UserCoinTotalVo totalVo = new UserCoinTotalVo();
		totalVo.setBtcPrice(btcPrice);
		totalVo.setCnyPrice(cnyPrice);
		totalVo.setEthPrice(ethPrice);
		totalVo.setUsdtPrice(usdtPrice);

		//整合返回数据
		UserCoinPropertyListVO listVO = new UserCoinPropertyListVO();
		listVO.setWithdrawalAmount(withdrawalAmount);
		listVO.setTotal(totalVo);
		listVO.setUseredWithdrawal(useredWithdrawal);
		listVO.setCoinList(userCoinInfoList);
		return listVO;
	}



	@Override
	@DataSource(DataSourceType.MASTER)
	public String getAddressQRBarcode(String userId, Integer coinId, String lang) throws VoException {
		CoinWithBLOBs coinBlob = coinMapper.selectByPrimaryKey(coinId);
		Member member = memberMapper.selectByPrimaryKey(Integer.parseInt(userId));

		if (member == null) {
			throw new VoException(I18nMessageService.getMsg(10013, lang));
		} else {
			if (member.getLockStatus() == 1) {
				throw new VoException(I18nMessageService.getMsg(10036, lang));
			}
		}

		if (coinBlob == null) {
			throw new VoException(I18nMessageService.getMsg(10013, lang));
		} else {
			if (coinBlob.getAllowRecharge() == null || coinBlob.getAllowRecharge() != 1) {
				throw new VoException(I18nMessageService.getMsg(10035, lang));
			}

			RechargeAddress rechargeAddress=rechargeAddressMapper.selectByUserIdAndCoinId(Integer.parseInt(userId), coinId);
			if (rechargeAddress != null) {
				return rechargeAddress.getAddress();
			} else {
				Coin coin=coinMapper.selectServerIpAndPort(coinId);
				String createNewAddressUrl = coin.getServerIp() + ":" + coin.getServerPort() + "/api/createNewAddress";
				String password = UUID.randomUUID().toString().replace("-", "");
	
				Map<String, String> map = new HashMap<>();
				map.put("password", password);
				String address = "";
				String json = HttpRequestUtil.sendPost(createNewAddressUrl, map);
				if (StringUtils.isNotBlank(json)) {
					if (JSON.parseObject(json).getBoolean("success")) {
						address = JSON.parseObject(json).getJSONObject("data").getString("newAccount");
					}
				}
				if(StringUtils.isNotBlank(address)) {
					RechargeAddress record=new RechargeAddress();
					record.setUserId(Integer.parseInt(userId));		
					record.setParentCoinId(coin.getId());
					record.setAddress(address);
					record.setPassword(Base64Utils.getBase64(Base64Utils.getBase64(password)));
					record.setCreateTime(new Date());
					record.setUpdateTime(new Date());
					int count = rechargeAddressMapper.insertSelective(record);
					if (count > 0) {
						return record.getAddress();
					} else {
						return null;
					}
				}else {
					return null;
				}
			}
		}
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	@Transactional
	public int withdrawal(Withdraw withdraw, String lang) throws VoException {
		//验证是否可以提现,未进行实名认证，无法提现
        commonCheckService.checkHasAuthentication(withdraw.getUserId());
        commonCheckService.checkFreezing(withdraw.getUserId());//资金冻结无法提现
		commonCheckService.checkGoogleCode(withdraw.getUserId());//未绑定谷歌无法提现
		CoinWithBLOBs coin = coinMapper.selectByPrimaryKey(withdraw.getCoinId());
		UserCoin userCoin = mapper.selectByUserIdAndCoinId(withdraw.getUserId(), withdraw.getCoinId());
		Member member = memberMapper.selectByPrimaryKey(withdraw.getUserId());
		// 该币在eth市场最新价格
		BigDecimal ethPrice = mapper.getLastPriceInEthById(withdraw.getCoinId());
		if (ethPrice == null) {
			ethPrice = new BigDecimal("0");
		}

		// 获取eth/btc市场的最新价格
		BigDecimal ethBtcPrice = mapper.getLastPriceEthBtc();
		if (ethBtcPrice == null) {
			ethBtcPrice = new BigDecimal("0");
		}

		// 获取btc/usdt市场的最新价格
		MarketCategory marketCategory=marketCategoryMapper.selectByCoinId(2);
		BigDecimal btcUsdtPrice =new BigDecimal("0");
		if(marketCategory!=null){
			btcUsdtPrice=marketCategory.getUsdtPrice();
		}

		// 该币在btc市场最新价格
		BigDecimal btcPrice = ethBtcPrice.multiply(ethPrice);
		if (btcPrice == null) {
			btcPrice = new BigDecimal("0");
		}

		BigDecimal btcQty = new BigDecimal("0");
		if (coin.getType()==2) {
			btcQty = withdraw.getQty();
		} else if (coin.getType()==1) {
			btcQty = withdraw.getQty().multiply(ethBtcPrice);
		} else if(coin.getType()==3){
			btcQty = withdraw.getQty().multiply(btcPrice);
		} else if(coin.getType()==4){
			btcQty = withdraw.getQty().divide(btcUsdtPrice,8,BigDecimal.ROUND_DOWN);
		}

		if (coin == null || member == null) {
			throw new VoException(I18nMessageService.getMsg(10013, lang));
		} else {
			if (coin.getAllowWithdraw() == null || coin.getAllowWithdraw() != 1) {
				throw new VoException(I18nMessageService.getMsg(10037, lang));
			}

			if (withdraw.getQty().compareTo(coin.getMaxOutQty()) > 0) {
				throw new VoException(I18nMessageService.getMsg(10038, lang));
			}

			if (withdraw.getQty().compareTo(coin.getMinOutQty()) < 0) {
				throw new VoException(I18nMessageService.getMsg(10039, lang));
			}

			if (member.getLockStatus() == 1) {
				throw new VoException(I18nMessageService.getMsg(10036, lang));
			}

			if (btcQty.compareTo(BigDecimal.ZERO) > 0) {
				// 查询当天用户已提现额度
				BigDecimal useredWithdrawal = withdrawMapper.queryUseredWithdraw(withdraw.getUserId());

				// 查询用户总提现额度
				BigDecimal LEVEL1_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL1_BTC.getName()));
				BigDecimal LEVEL2_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL2_BTC.getName()));
				BigDecimal LEVEL3_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL3_BTC.getName()));

				// 用户剩余额度
				BigDecimal REMAIN_LEVEL1_BTC = LEVEL1_BTC.subtract(useredWithdrawal);
				BigDecimal REMAIN_LEVEL2_BTC = LEVEL2_BTC.subtract(useredWithdrawal);
				BigDecimal REMAIN_LEVEL3_BTC = LEVEL3_BTC.subtract(useredWithdrawal);

				if (member.getLevel() == 1 && btcQty.compareTo(REMAIN_LEVEL1_BTC) > 0) {
					throw new VoException(I18nMessageService.getMsg(10040, lang));
				}

				if (member.getLevel() == 2 && btcQty.compareTo(REMAIN_LEVEL2_BTC) > 0) {
					throw new VoException(I18nMessageService.getMsg(10040, lang));
				}

				if (member.getLevel() == 3 && btcQty.compareTo(REMAIN_LEVEL3_BTC) > 0) {
					throw new VoException(I18nMessageService.getMsg(10040, lang));
				}
			}

		}

		if (userCoin == null || userCoin.getBanlance().compareTo(withdraw.getQty()) < 0) {
			throw new VoException(I18nMessageService.getMsg(10041, lang));
		}

		if(withdraw.getQty().compareTo( coin.getMinOutQty().add(coin.getWithdrawFee())) < 0){
			throw new VoException(I18nMessageService.getMsg(10111,lang));
		}
		withdraw.setFee(coin.getWithdrawFee());
		withdraw.setBtcPrice(btcQty);
		UserCoin oldUserCoin = (UserCoin) BeanHepler.clone(userCoin);
		userCoin.setBanlance(userCoin.getBanlance().subtract(withdraw.getQty()));
		//TODO user_coin锁
		int count=mapper.updateByPrimaryKeySelective(userCoin);
		if(count>0) {
			count=withdrawMapper.insertSelective(withdraw);
			userCoinDetailService.addUserCoinDetail(oldUserCoin, withdraw.getId()+"", UserCoinDetailType.WITHDRAW);
		}
		return count;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<Map<String, Object>> toTrade(Integer coinId) {
		List<Map<String, Object>> list = marketMapper.selectByCoinId(coinId);
		return list;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<MarketCoinVo> list() {
		String key =RedisKeyEnum.COIN_LIST.getKey();
		List<MarketCoinVo> list = redisUtil.getList(key, MarketCoinVo.class);
		if (list == null) {
			list = coinMapper.selectNameAll();
			redisUtil.setList(key, list, -1);
		}
		return list;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> rechargeShow(Integer userId, Integer coinId) {
		Map<String, Object> map = new HashMap<>();
		List<TradeMarket> list =new ArrayList<>();		
		List<Map<String, Object>> marketList=marketMapper.selectByCoinId(coinId);
		
		if(marketList!=null&&marketList.size()>0) {
			for(int i=0;i<marketList.size();i++) {
				Integer id=(Integer) marketList.get(i).get("id");
				MarketInfo marketInfo=this.getMarketInfoById(id);
				
				TradeMarket tradeMarket=new TradeMarket();
				tradeMarket.setId(marketInfo.getMarketId());
				tradeMarket.setName(marketInfo.getCoinName()+"/"+marketInfo.getMarketCoinName());
				tradeMarket.setCnyPrice(marketInfo.getCnyPrice().multiply(marketInfo.getLastTradePrice()));
				tradeMarket.setNewPrice(marketInfo.getLastTradePrice());
				tradeMarket.setRate(marketInfo.getIncRate());
				
				list.add(tradeMarket);
			}
		}
		
		List<UserCoinInfo> userCoinList = mapper.selectPropertyByUserIdAndCoinId(userId, coinId, null);
		if (userCoinList != null && userCoinList.size() > 0) {
			UserCoinInfo userCoinInfo = userCoinList.get(0);
			map.put("userCoin", userCoinInfo);
		} else {
			BigDecimal price=new BigDecimal("0");
			map.put("userCoin", new UserCoinInfo(userId, price, price, price, price, price, price,price));
		}
		map.put("tradeList", list);
		return map;
	}

	private MarketInfo getMarketInfoById(int marketId) {
		MarketInfo marketInfo=new MarketInfo();
		List<MarketInfo> marketInfolist=redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
		if(marketInfolist!=null&&marketInfolist.size()>0) {
			for(int i=0;i<marketInfolist.size();i++) {
				if(marketInfolist.get(i).getMarketId()==marketId) {
					marketInfo=marketInfolist.get(i);
					break;
				}
			}
		}
		return marketInfo;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public CoinWithBLOBs selectCoinById(Integer coinId) {
		CoinWithBLOBs coin = coinMapper.selectByPrimaryKey(coinId);
		return coin;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public int updateBalanceInfoByUserId(UserCoin userCoin)throws VoException
	{
		int ret = mapper.updateByPrimaryKey(userCoin);
		return ret;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public UserCoinInfo queryCoinInfoByUserId(Integer userId, Integer coinId,String lang) throws VoException {
		Member member = memberMapper.selectByPrimaryKey(userId);
		if (member == null) {
			throw new VoException(I18nMessageService.getMsg(10062, lang));
		} 
		
		List<UserCoinInfo> userCoinList = mapper.selectPropertyByUserIdAndCoinId(userId, coinId, null);
		if (userCoinList != null && userCoinList.size() > 0) {
			// 查询当天用户已提现额度
			BigDecimal useredWithdrawal = withdrawMapper.queryUseredWithdraw(userId);
			// 查询用户不同等级总额度
			BigDecimal LEVEL1_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL1_BTC.getName()));
			BigDecimal LEVEL2_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL2_BTC.getName()));
			BigDecimal LEVEL3_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL3_BTC.getName()));
			
			UserCoinInfo userCoinInfo = userCoinList.get(0);
			userCoinInfo.setUseredWithdrawal(useredWithdrawal);
			if (member.getLevel() == 1) {
				userCoinInfo.setWithdrawalAmount(LEVEL1_BTC);
			} else if (member.getLevel() == 2) {
				userCoinInfo.setWithdrawalAmount(LEVEL2_BTC);
			} else if (member.getLevel() == 3) {
				userCoinInfo.setWithdrawalAmount(LEVEL3_BTC);
			}
			return userCoinInfo;
		} else {
			BigDecimal price=new BigDecimal("0");
			return new UserCoinInfo(userId, price, price, price, price, price, price,price);
		}

	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public UserCoin queryBalanceInfoByUserId(Integer userId, Integer coinId) throws VoException {
		BigDecimal balance=new BigDecimal(0);
		UserCoin userCoin = mapper.selectByUserIdAndCoinId(userId, coinId);
		return userCoin;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public List<TradeMarket> selectTradeByCoinId(int coinId) {
		List<TradeMarket> list = marketMapper.selectTradeByCoinId(coinId);
		return list;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public BigDecimal selectCnyPriceByCoinId(Integer coinId) {
		BigDecimal cnyprice=new BigDecimal("0");
		String key =RedisKeyEnum.MARKET_LIST.getKey();
		List<MarketInfo> marketInfoList = redisUtil.getList(key, MarketInfo.class);

		if(marketInfoList!=null&&marketInfoList.size()>0){
			for(int i=0;i<marketInfoList.size();i++){
				if(marketInfoList.get(i).getCoinId()==coinId){
					cnyprice=marketInfoList.get(i).getCnyPrice().multiply(marketInfoList.get(i).getLastTradePrice());
					break;
				}else if(marketInfoList.get(i).getMarketCoinId()==2){
					if(coinId==2){
						cnyprice=marketInfoList.get(i).getCnyPrice();
						break;
					}
				}
			}
		}
		return cnyprice.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public Map<String, Object> beforeWithdrawal(Integer userId) {
		Boolean hasGoogleCode = commonCheckService.hasGoogleCode(userId);
		Boolean hasAuthentication = commonCheckService.hasAuthentication(userId);
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("hasGoogleCode", hasGoogleCode);
		dataMap.put("hasAuthentication", hasAuthentication);
		return dataMap;
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public UserCoin selectAndInsert(Integer userId, Integer coinId) {
		UserCoin userCoin = mapper.selectByUserIdAndCoinId(userId, coinId);
		if (userCoin != null) {
			return userCoin;
		}
		UserCoin entity = UserCoin.newInstance(userId, coinId);
		mapper.insertSelective(entity);
		entity = mapper.selectByUserIdAndCoinId(userId, coinId);
		return entity;
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public void addBalanceWithType(Integer userId, Integer coinId, BigDecimal qty, String targetId, UserCoinDetailType type) {
		UserCoin betUserCoin = selectAndInsert(userId, coinId);
		int addSucc = mapper.addBanlance(qty, coinId, userId, betUserCoin.getVersion());
		AssertHelper.isTrue(addSucc>=1, "系统处理异常，余额未发生变化，请稍后再试");
		userCoinDetailService.addUserCoinDetail(betUserCoin, targetId, type);
	}

}
