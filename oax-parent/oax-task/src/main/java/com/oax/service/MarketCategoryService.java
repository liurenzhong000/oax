package com.oax.service;

import java.math.BigDecimal;


public interface MarketCategoryService {

	/**
	 * 更新市场btc cny eth undt
	 * @param eth2rbm 
	 * @param btc2rbm 
	 * @param eth2usd 
	 * @param btc2usd 
	 * @return
	 */
	int updateMarketExchangePrice(BigDecimal btc2usd, BigDecimal eth2usd, BigDecimal btc2rbm, BigDecimal eth2rbm,BigDecimal rate);

	int updateMarketCategoryPrice();
}
