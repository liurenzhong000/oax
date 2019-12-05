package com.oax.scheduled;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.oax.common.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.oax.service.MarketCategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExchangePriceScheduled {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MarketCategoryService marketCategoryService;

    private static final String BATH_PATH = "https://www.okex.com/api/v1";

    private static final String BTC_USD_URL = BATH_PATH + "/future_index.do?symbol=btc_usd";

    private static final String EHT_USD_URL = BATH_PATH + "/future_index.do?symbol=eth_usd";

    private static final String EXCHANGE_RATE = BATH_PATH + "/exchange_rate.do";

    @Async
    @Scheduled(fixedRate = 6000)  //(fixedRate = 6*100*1000)
    public void updateMarketExchangePrice() {
    /*   HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("contentType", "application/x-www-form-urlencoded");
        httpHeaders.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(null, httpHeaders);*/
        try {
            log.info("-------定时任务updateMarketExchangePrice开启--------");
           /* JSONObject btc2usdResult = restTemplate.exchange(BTC_USD_URL, HttpMethod.GET, stringHttpEntity, JSONObject.class, new HashMap<>()).getBody();
            JSONObject eth2usdResult = restTemplate.exchange(EHT_USD_URL, HttpMethod.GET, stringHttpEntity, JSONObject.class, new HashMap<>()).getBody();
            JSONObject exchangeRateResult = restTemplate.exchange(EXCHANGE_RATE, HttpMethod.GET, stringHttpEntity, JSONObject.class, new HashMap<>()).getBody();
            BigDecimal bigDecimal = exchangeRateResult.getBigDecimal("rate");

            BigDecimal btc2usd = btc2usdResult.getBigDecimal("future_index");
            BigDecimal eth2usd = eth2usdResult.getBigDecimal("future_index");

            BigDecimal btc2rbm = btc2usd.multiply(bigDecimal);
            BigDecimal eth2rbm = eth2usd.multiply(bigDecimal);

            marketCategoryService.updateMarketExchangePrice(btc2usd,eth2usd,btc2rbm,eth2rbm,bigDecimal);*/
            marketCategoryService.updateMarketCategoryPrice();
            log.info("---------定时任务updateMarketExchangePrice结束---------");
        } catch (Exception e) {
            log.error("--------定时任务updateMarketExchangePrice请求失败--------");
            e.printStackTrace();
        }


    }

}