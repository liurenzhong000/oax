package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.oax.entity.front.UserCoin;
import com.oax.mapper.front.UserCoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.common.enums.CoinTypeEnum;
import com.oax.entity.front.Coin;
import com.oax.entity.front.MarketCategory;
import com.oax.mapper.front.CoinMapper;
import com.oax.mapper.front.MarketCategoryMapper;
import com.oax.service.MarketCategoryService;


@Service
public class MarketCategoryServiceImpl implements MarketCategoryService {
	@Autowired
	private MarketCategoryMapper marketCategoryMapper;

	@Autowired
	private CoinMapper coinMapper;


	@Autowired
	private UserCoinMapper userCoinMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateMarketExchangePrice(BigDecimal btc2usd, BigDecimal eth2usd, BigDecimal btc2rbm,BigDecimal eth2rbm,BigDecimal rate) {
		List<Coin> btcCoinList =coinMapper.selectByType(CoinTypeEnum.BTC.getType());
		Coin btcCoin = btcCoinList.get(0);

		List<Coin> ethCoinList=coinMapper.selectByType(CoinTypeEnum.ETH.getType());
		Coin ethCoin = ethCoinList.get(0);

		List<Coin> usdtCoinList=coinMapper.selectByType(CoinTypeEnum.USDT.getType());
		Coin usdtCoin = usdtCoinList.get(0);

		//更新比特币分区的usdtPrice cnyPrice
		MarketCategory btcMarketCategory=new MarketCategory();
		btcMarketCategory.setCoinId(btcCoin.getId());
		btcMarketCategory.setUsdtPrice(btc2usd);
		btcMarketCategory.setCnyPrice(btc2rbm);

		//更新eth分区的usdtPrice cnyPrice
		MarketCategory ethMarketCategory=new MarketCategory();
		ethMarketCategory.setCoinId(ethCoin.getId());
		ethMarketCategory.setUsdtPrice(eth2usd);
		ethMarketCategory.setCnyPrice(eth2rbm);

		//更新usdt分区的usdtPrice cnyPrice
		MarketCategory usdtMarketCategory=new MarketCategory();
		usdtMarketCategory.setCoinId(usdtCoin.getId());
		usdtMarketCategory.setUsdtPrice(new BigDecimal("1"));
		usdtMarketCategory.setCnyPrice(rate);

		int count=marketCategoryMapper.updateByCoinIdSelective(btcMarketCategory);
		if(count>0){
			count=marketCategoryMapper.updateByCoinIdSelective(ethMarketCategory);
		}
		if(count>0){
			count=marketCategoryMapper.updateByCoinIdSelective(usdtMarketCategory);
		}
		return count;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateMarketCategoryPrice()
	{
		BigDecimal  btc_usdt=userCoinMapper.getLastPriceUsdtBtc();
		BigDecimal  eth_usdt=userCoinMapper.getLastPriceEthUsdt();
		BigDecimal  usdt_price=userCoinMapper.getPrice();

		List<Coin> btcCoinList =coinMapper.selectByType(CoinTypeEnum.BTC.getType());
		Coin btcCoin = btcCoinList.get(0);

		List<Coin> ethCoinList=coinMapper.selectByType(CoinTypeEnum.ETH.getType());
		Coin ethCoin = ethCoinList.get(0);

		MarketCategory btcMarketCategory=new MarketCategory();
		btcMarketCategory.setCoinId(btcCoin.getId());
		btcMarketCategory.setUsdtPrice(btc_usdt);
		btcMarketCategory.setCnyPrice(btc_usdt.multiply(usdt_price));

		//更新eth分区的usdtPrice cnyPrice
		MarketCategory ethMarketCategory=new MarketCategory();
		ethMarketCategory.setCoinId(ethCoin.getId());
		ethMarketCategory.setUsdtPrice(eth_usdt);
		ethMarketCategory.setCnyPrice(eth_usdt.multiply(usdt_price));

		int count=marketCategoryMapper.updateByCoinIdSelective(btcMarketCategory);
		if(count>0){
			count=marketCategoryMapper.updateByCoinIdSelective(ethMarketCategory);
		}

		return count;
	}
}