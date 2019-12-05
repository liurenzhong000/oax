package com.oax.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.*;
import com.oax.exception.VoException;
import com.oax.vo.CoinListVO;
import com.oax.vo.UserCoinPropertyListVO;

/** 
* @ClassName:：UserCoinService 
* @Description： 用户资产service接口
* @author ：xiangwh  
* @date ：2018年6月6日 下午9:29:08 
*  
*/
public interface UserCoinService {
	Map<String, Object> getUserCoinMap(Integer marketId, Integer userId);

	Map<String, Object> coinList(CoinListVO vo) ;

	UserCoinPropertyListVO coinListNew(CoinListVO vo) ;

	String getAddressQRBarcode(String userId, Integer coinId, String lang) throws VoException;

	int withdrawal(Withdraw withdraw, String lang)throws VoException;


	List<Map<String, Object>> toTrade(Integer coinId);


	List<MarketCoinVo> list();


	Map<String, Object> rechargeShow(Integer userId, Integer coinId);


	CoinWithBLOBs selectCoinById(Integer coinId);


	UserCoinInfo queryCoinInfoByUserId(Integer userId, Integer coinId, String lang)throws VoException;


	List<TradeMarket> selectTradeByCoinId(int coinId);

	BigDecimal selectCnyPriceByCoinId(Integer coinId);

	UserCoin queryBalanceInfoByUserId(Integer userId, Integer coinId)throws VoException ;

	int updateBalanceInfoByUserId(UserCoin userCoin)throws VoException;

	Map<String, Object> beforeWithdrawal(Integer userId);

	/**查询用户对应的币种余额记录，如果为空，插入数据*/
	UserCoin selectAndInsert(Integer userId, Integer coinId);

	void addBalanceWithType(Integer userId, Integer coinId, BigDecimal qty, String targetId, UserCoinDetailType type);
}
