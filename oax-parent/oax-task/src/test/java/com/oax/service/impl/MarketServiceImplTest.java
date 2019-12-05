package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.reader.JsonReader;
import com.oax.common.EmptyHelper;
import com.oax.common.HttpRequestUtil;
import com.oax.common.SignUtil;
import com.oax.common.json.JsonHelper;
import com.oax.common.json.JsonMapAccessor;
import com.oax.common.json.TypeReferences;
import com.oax.entity.front.*;
import com.oax.mapper.front.KlineMapper;
import com.oax.mapper.front.OrdersMapper;
import com.oax.mapper.front.TradeMapper;
import com.oax.service.OrderService;
import com.oax.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.common.enums.MarketStatusEnum;
import com.oax.service.MarketService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/24
 * Time: 16:35
 */
@Component
@Slf4j
public class MarketServiceImplTest extends OaxApiApplicationTest {

    @Autowired
    private MarketService marketService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private TradeService tradeService;




    private String oaxApiKey="PDLXbtcoin201892891";

    private String oaxApiSecret="XBTC201892891";

    @Value("${oax.api.url.add_order}")
    private String addOrderUrl;


    @Value("${oax.api.url.get_depth}")
    private String getDepthUrl;


    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private KlineMapper klineMapper;

    @Test
    public void selectByType() {
        List<Market> markets = marketService.selectByType(MarketStatusEnum.SHOW.getStatus());

        assert markets.size() > 0;
    }

    /**
     * OKEX下单机器人
     */
    @Test
    public void batcOrderByOk() {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "1080");
        // 获取oax的所有开启交易对
        List<MarketCoinInfo> marketInfoList = orderService.selectAutoAddMarket();
        marketInfoList.forEach(item -> item.setMarketCoinName(item.getMarketCoinName().toUpperCase().replace("/","_")));
        log.info("marketInfoList:{}", marketInfoList);
        //去请求okex的交易数据
        marketInfoList.forEach(item ->{
            String responseStr = HttpRequestUtil.sendGet("https://www.okex.com/api/spot/v3/instruments/<instrument-id>/book".replace("<instrument-id>", item.getMarketCoinName()), "size=10");
            if (EmptyHelper.isEmpty(responseStr)) {
                log.info("okex 不存在交易对:{}", item.getMarketCoinName());
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            JSONArray bidsArray = jsonObject.getJSONArray("bids");
            bidsArray.forEach(bid ->{
                JSONArray bids = (JSONArray) bid;
                System.out.println(bids.get(0));
            });

//            Map<String, Object> jsonMap = JsonHelper.convert(responseStr, TypeReferences.REF_MAP_OBJECT);
//            JsonMapAccessor jm = new JsonMapAccessor(jsonMap);
//            List<Object> bids = jm.getList("bids", false);
//            log.info( item.getMarketCoinName()+"=" + JsonHelper.writeValueAsString(bids.get(0)));
//            List<Object> asks = jm.getList("asks", false);
//            log.info(item.getMarketCoinName()+"=" + JsonHelper.writeValueAsString(asks.get(0)));
        });
    }

    @Test
    public void testTradeList(){
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            autoMakeOrder();
        }
    }




    /**
     * 下单机器人
     */

    public void autoMakeOrder() {


        //下单数量下限
        int startNumber = 1000;
        //下单数量上限
        int endNumber = 1500;
        //价格下限,盘低
        double startPrice = 0.0184;
        //价格上限，盘高
        double endPrice = 0.02;
        //价格精度
        double precision = 0.00004;
        //交易对
        int markerId = 35;
        //交易对名称
        String tradeName="XBTC/USDT";

        Random random = new Random();

        //数量整数位
        int qty;
        //数量小数位
        int qty_decimal;
        //签名
        String sign;
        //下单返回结果
        String result;


        try{
            boolean temp = random.nextBoolean();
            Trade trade = tradeService.selectByMarketId(markerId);
            if(trade.getPrice().compareTo(new BigDecimal(endPrice))==0){
                temp=true;
            }else if(trade.getPrice().compareTo(new BigDecimal(startPrice))==0){
                temp=false;
            }
            if (temp) {//true,下降
                //BigDecimal frontClosePrice = klineMapper.getKlineListLast(markerId,1).getClose();
                List<MarketOrders> list=ordersMapper.findMarketOrdersList(markerId,2);
                MarketOrders marketOrders = list.get(0);//买1
                TreeMap<String, String> params = new TreeMap<String, String>();
                //下买单
                params.put("apiKey", oaxApiKey);
                params.put("market", tradeName);
                params.put("type", "1");
                if (new BigDecimal(endPrice).compareTo(marketOrders.getPrice()) > 0) {//价格上限>交易价格
                    if (new BigDecimal(startPrice).compareTo(marketOrders.getPrice()) < 0) {
                        params.put("price", marketOrders.getPrice().toString());//price=交易价格
                    }else {
                        params.put("price", new BigDecimal(startPrice).toString());//price=交易价格
                    }
                } else {
                    params.put("price", String.valueOf(endPrice));//price=价格上限
                }
                params.put("qty", marketOrders.getQty().divide(new BigDecimal(random.nextInt(7)+2),4, BigDecimal.ROUND_HALF_UP).toString());
                params.put("sign", "");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                result = HttpRequestUtil.sendPost(addOrderUrl, params);
                log.info(params.get("market") + "买盘" + result);
                Thread.sleep((random.nextInt(10)+5)*1000);
                //下卖单1
                list=ordersMapper.findMarketOrdersList(markerId,1);
                marketOrders = list.get(0);
                trade = tradeService.selectByMarketId(markerId);
                params.put("apiKey", oaxApiKey);
                params.put("market", tradeName);
                params.put("type", "2");
                if (marketOrders != null) {//有买1记录
                    if (new BigDecimal(endPrice).compareTo(marketOrders.getPrice()) > 0) {//价格上限>交易价格
                        if (new BigDecimal(startPrice).compareTo(marketOrders.getPrice()) < 0) {
                            params.put("price", marketOrders.getPrice().toString());//price=交易价格
                        }else {
                            params.put("price", new BigDecimal(startPrice).toString());//price=交易价格
                        }
                    } else {
                        params.put("price", String.valueOf(endPrice));//price=价格上限
                    }
                    params.put("qty", marketOrders.getQty().divide(new BigDecimal(random.nextInt(7)+2),4, BigDecimal.ROUND_HALF_UP).toString());
                } else {//没有买1记录
                    if (startPrice != 0) {//取设置的起始价格
                        params.put("price", new BigDecimal(startPrice).add(new BigDecimal(precision)).toString());
                    } else {//取上一笔成交的价格
                        params.put("price", trade.getPrice().toString());
                    }
                    qty_decimal = random.nextInt(10000);
                    qty = random.nextInt(endNumber - startNumber) + startNumber;
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                }
                params.put("sign", "");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                result = HttpRequestUtil.sendPost(addOrderUrl, params);
                log.info(params.get("market") + "卖盘" + result);
                Thread.sleep((random.nextInt(10)+5)*1000);
                //下卖单2
                list=ordersMapper.findMarketOrdersList(markerId,1);
                params.put("apiKey", oaxApiKey);
                params.put("market", tradeName);
                params.put("type", "2");
                if (new BigDecimal(endPrice).compareTo(new BigDecimal(params.get("price"))) > 0) {//价格上限>交易价格
                    params.put("price", params.get("price"));//price=交易价格
                } else {
                    params.put("price", String.valueOf(endPrice));//price=价格上限
                }
                qty_decimal = random.nextInt(10000);
                qty = random.nextInt(endNumber - startNumber) + startNumber;
                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                params.put("qty", list.get(0).getQty().add(new BigDecimal(params.get("qty"))).toString());
                params.put("sign","");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                result = HttpRequestUtil.sendPost(addOrderUrl, params);
                log.info(params.get("market") + "卖盘" + result);
                Thread.sleep((random.nextInt(10)+5)*1000);
                //下买单,补买挂单
                list=ordersMapper.findMarketOrdersList(markerId,1);
                if(list.size()<15){
                    marketOrders = list.get(list.size()-1);
                    params.put("apiKey", oaxApiKey);
                    params.put("market", tradeName);
                    params.put("type", "1");
                    params.put("price", marketOrders.getPrice().subtract(new BigDecimal(precision)).toString());
                    qty = random.nextInt(endNumber - startNumber) + startNumber;
                    qty_decimal = random.nextInt(10000);
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                    params.put("sign","");
                    sign = SignUtil.sign(oaxApiSecret, params);
                    params.put("sign", sign);

                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "买盘" + result);
                }
            } else {//false，价格上升
                //下卖单，防止价格跳动
                Random random1 = new Random();
                //BigDecimal frontOpenPrice = klineMapper.getKlineListLast(markerId,1).getClose();
                List<MarketOrders> list=ordersMapper.findMarketOrdersList(markerId,1);
                MarketOrders marketOrders = list.get(0);//买1
                TreeMap<String, String> params = new TreeMap<String, String>();
                params.put("apiKey", oaxApiKey);
                params.put("market", tradeName);
                params.put("type", "2");
                if (new BigDecimal(endPrice).compareTo(marketOrders.getPrice()) > 0) {//价格上限>交易价格
                    if (new BigDecimal(startPrice).compareTo(marketOrders.getPrice()) < 0) {
                        params.put("price", marketOrders.getPrice().toString());//price=交易价格
                    }else {
                        params.put("price", new BigDecimal(startPrice).toString());//price=交易价格
                    }
                } else {
                    params.put("price", String.valueOf(endPrice));//price=价格上限
                }
                params.put("qty", marketOrders.getQty().divide(new BigDecimal(random.nextInt(7)+2),4, BigDecimal.ROUND_HALF_UP).toString());
                params.put("sign", "");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                result = HttpRequestUtil.sendPost(addOrderUrl, params);
                log.info(params.get("market") + "卖盘" + result);
                Thread.sleep((random.nextInt(10)+5)*1000);

                //下买单
                list=ordersMapper.findMarketOrdersList(markerId,2);
                marketOrders = list.get(0);//卖1
                trade = tradeService.selectByMarketId(markerId);
                params.put("apiKey", oaxApiKey);
                params.put("market", tradeName);
                params.put("type", "1");
                if (marketOrders != null) {//存在卖1单
                    if (new BigDecimal(endPrice).compareTo(marketOrders.getPrice()) > 0) {//价格上限>交易价格
                        if (new BigDecimal(startPrice).compareTo(marketOrders.getPrice()) < 0) {
                            params.put("price", marketOrders.getPrice().toString());//price=交易价格
                        }else {
                            params.put("price", new BigDecimal(startPrice).toString());//price=交易价格
                        }
                    } else {
                        params.put("price", String.valueOf(endPrice));//price=价格上限
                    }
                    params.put("qty", marketOrders.getQty().divide(new BigDecimal(random.nextInt(7)+2),4, BigDecimal.ROUND_HALF_UP).toString());
                } else {//不存在卖1单
                    if (startPrice != 0) {//取设置的起始价格
                        params.put("price", new BigDecimal(startPrice).add(new BigDecimal(precision)).toString());
                    } else {//未设置起始价格，取上次成交价格
                        if (new BigDecimal(endPrice).compareTo(trade.getPrice()) > 0) {//价格上限>交易价格
                            params.put("price", trade.getPrice().toString());//price=交易价格
                        } else {
                            params.put("price", String.valueOf(endPrice));//price=价格上限
                        }
                    }
                    qty = random1.nextInt(endNumber - startNumber) + startNumber;
                    qty_decimal = random1.nextInt(10000);
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                }
                params.put("sign","");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                result = HttpRequestUtil.sendPost(addOrderUrl, params);
                log.info(params.get("market") + "买盘" + result);
                Thread.sleep((random.nextInt(10)+5)*1000);

                //下买单
                list=ordersMapper.findMarketOrdersList(markerId,2);
                params.put("apiKey", oaxApiKey);
                params.put("market", tradeName);
                params.put("type", "1");
                if (new BigDecimal(endPrice).compareTo(new BigDecimal(params.get("price"))) > 0) {//价格上限>交易价格
                    params.put("price", params.get("price"));//price=交易价格
                } else {
                    params.put("price", String.valueOf(endPrice));//price=价格上限
                }
                qty_decimal = random.nextInt(10000);
                qty = random.nextInt(endNumber - startNumber) + startNumber;
                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                params.put("qty", list.get(0).getQty().add(new BigDecimal(params.get("qty"))).toString());
                params.put("sign","");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                result = HttpRequestUtil.sendPost(addOrderUrl, params);
                log.info(params.get("market") + "买盘" + result);
                Thread.sleep((random.nextInt(10)+5)*1000);
                //下卖单,补卖挂单
                list=ordersMapper.findMarketOrdersList(markerId,2);
                if(list.size()<15){
                    marketOrders = list.get(list.size()-1);
                    params.put("apiKey", oaxApiKey);
                    params.put("market", tradeName);
                    params.put("type", "2");
                    params.put("price", marketOrders.getPrice().add(new BigDecimal(precision)).toString());//price=交易价格
                    qty = random.nextInt(endNumber - startNumber) + startNumber;
                    qty_decimal = random.nextInt(10000);
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                    params.put("sign","");
                    sign = SignUtil.sign(oaxApiSecret, params);
                    params.put("sign", sign);

                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "卖盘" + result);
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}