package com.oax.scheduled;

import com.oax.common.HttpRequestUtil;
import com.oax.common.SignUtil;
import com.oax.entity.front.*;
import com.oax.mapper.front.KlineMapper;
import com.oax.mapper.front.MarketMapper;
import com.oax.mapper.front.OrdersMapper;
import com.oax.mapper.front.RobotMapper;
import com.oax.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：zl
 * @ClassName:：AutoOrderScheduled
 * @Description： XBTC/USDT每1分钟自动刷单,线上版本
 */


@Slf4j
@Controller
public class AutoRobotOrderScheduled {

    @Value("${oax.api.url.add_order}")
    private String addOrderUrl;

    @Value("${oax.api.url.get_depth}")
    private String getDepthUrl;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private KlineMapper klineMapper;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private MarketMapper marketMapper;

    /**创建线程池*/
    final ExecutorService threadPool = Executors.newFixedThreadPool(8);

     /**
     * 下单机器人
     */
    @Async
    @Scheduled(fixedRate = 1000 * 30)
    public void autoMakeOrder() {
        log.info("=================ROBOT START==================");
        List<Robot> robots = robotMapper.selectOpen();
        robots.forEach(item -> threadPool.execute(() -> autoMakeOrder(item)));
    }

    public void autoMakeOrder(Robot robot){
        log.info(Thread.currentThread().getName() +" - "+ robot.toString());
        int buyStartNumber = robot.getBuyStartNumber().intValue();
        int buyEndNumber = robot.getBuyEndNumber().intValue();
        double startPrice = robot.getStartPrice().doubleValue();
        double endPrice = robot.getEndPrice().doubleValue();
        int sellStartNumber = robot.getSellStartNumber().intValue();
        int sellEndNumber = robot.getSellEndNumber().intValue();
        double precision = robot.getAccuracy().doubleValue();
        int markerId = robot.getMarketId();
        String tradeName = robot.getMarketName();
        String oaxApiKey= robot.getApiKey();
        String oaxApiSecret= robot.getApiSecret();
        Integer dynamicSleep = robot.getDynamicSleep();
        Integer fixSleep = robot.getFixSleep();

        Random random = new Random();

        //数量整数位
        int qty;
        //数量小数位
        int qty_decimal;
        //签名
        String sign;
        //下单返回结果
        String result;
        //在盘高和盘低之间，以最小精度补挂单
        Market market = marketMapper.selectByPrimaryKey(markerId);
        ArrayList sellPriceList = new ArrayList();
        ArrayList buyPriceList = new ArrayList();
        List<MarketOrders> sellList=ordersMapper.findMarketOrders(markerId,2,null);
        sellList.forEach(item->sellPriceList.add(item.getPrice().doubleValue()));
        List<MarketOrders> buyList=ordersMapper.findMarketOrders(markerId,1,null);
        buyList.forEach(item->buyPriceList.add(item.getPrice().doubleValue()));
        BigDecimal sellPrice = new BigDecimal(Double.toString(endPrice));
        log.info("sellPrice"+sellPrice);
        BigDecimal buyPrice = new BigDecimal(Double.toString(startPrice));
        log.info("buyPrice"+buyPrice);
        BigDecimal buy1Price = new BigDecimal(Double.toString(buyList.get(0).getPrice().doubleValue()));
        log.info("buy1Price"+buy1Price);
        BigDecimal value = sellPrice.subtract(buyPrice).divide(new BigDecimal(Double.toString(precision)),market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP);
        TreeMap<String, String> tempParams = new TreeMap<String, String>();
        tempParams.put("apiKey", oaxApiKey);
        tempParams.put("market", tradeName);
        log.info("自动补单开始");
        for(int i=1;i<value.intValue();i++){
            BigDecimal price = buyPrice.add(new BigDecimal(Double.toString(precision)).multiply(new BigDecimal(i))).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP);
            //判断买挂单是否存在该价格
            if(!buyPriceList.contains(price.doubleValue())){
                //判断该价格是否小于买1价格
                if(price.compareTo(buy1Price)<0){
                    //补买单
                    log.info("买单"+price.toString());
                    tempParams.put("type", "1");
                    tempParams.put("price", price.toString());
                    qty_decimal = random.nextInt(10000);
                    qty = random.nextInt(buyEndNumber - buyStartNumber) + buyStartNumber;
                    tempParams.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                    tempParams.put("sign", "");
                    sign = SignUtil.sign(oaxApiSecret, tempParams);
                    tempParams.put("sign", sign);
                    result = HttpRequestUtil.sendPost(addOrderUrl, tempParams);
                    log.info(tempParams.get("market") + "自动补单买盘" + result +"qty={} - price={}", tempParams.get("qty"), tempParams.get("price"));
                }else {
                    //补卖单
                    if(!sellPriceList.contains(price.doubleValue())){
                        log.info("卖单"+price.toString());
                        tempParams.put("type", "2");
                        tempParams.put("price", price.toString());
                        qty_decimal = random.nextInt(10000);
                        qty = random.nextInt(sellEndNumber - sellStartNumber) + sellStartNumber;
                        tempParams.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                        tempParams.put("sign", "");
                        sign = SignUtil.sign(oaxApiSecret, tempParams);
                        tempParams.put("sign", sign);
                        result = HttpRequestUtil.sendPost(addOrderUrl, tempParams);
                        log.info(tempParams.get("market") + "自动补单卖盘" + result +"qty={} - price={}", tempParams.get("qty"), tempParams.get("price"));
                    }
                }
            }
        }

        try{
            boolean temp = random.nextBoolean();
            ArrayList klineHighPrice = new ArrayList();
            ArrayList klineLowerPrice = new ArrayList();
            Trade trade = tradeService.selectByMarketId(markerId);
            List<Kline> klineList = klineMapper.getKlineListLast(markerId,1,3);
            klineList.forEach(item->klineHighPrice.add(item.getHigh().doubleValue()));
            klineList.forEach(item->klineLowerPrice.add(item.getLow().doubleValue()));
            if(trade == null || klineHighPrice.contains(endPrice)){
                temp=true;
            }else if(klineLowerPrice.contains(startPrice)){
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
                params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                params.put("qty", marketOrders.getQty().divide(new BigDecimal(random.nextInt(7)+2),4, BigDecimal.ROUND_HALF_UP).toString());
                params.put("sign", "");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                if(!buyPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "买盘" + result +"qty={} - price={}", params.get("qty"), params.get("price"));
                }

                dormant(random, dynamicSleep, fixSleep);
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
                        params.put("price", new BigDecimal(startPrice).add(new BigDecimal(Double.toString(precision))).toString());
                    } else {//取上一笔成交的价格
                        params.put("price", trade.getPrice().toString());
                    }
                    qty_decimal = random.nextInt(10000);
                    qty = random.nextInt(sellEndNumber - sellStartNumber) + sellStartNumber;
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                }
                params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                params.put("sign", "");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                if(!sellPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "卖盘" + result+"qty={} - price={}", params.get("qty"), params.get("price"));
                }

                dormant(random, dynamicSleep, fixSleep);
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
                qty = random.nextInt(sellEndNumber - sellStartNumber) + sellStartNumber;
                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                params.put("qty", list.get(0).getQty().add(new BigDecimal(params.get("qty"))).toString());
                params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                params.put("sign","");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                if(!sellPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "卖盘" + result+"qty={} - price={}", params.get("qty"), params.get("price"));
                }

                dormant(random, dynamicSleep, fixSleep);
                //下买单,补买挂单
                list=ordersMapper.findMarketOrdersList(markerId,1);
                if(list.size()<15){
                    marketOrders = list.get(list.size()-1);
                    params.put("apiKey", oaxApiKey);
                    params.put("market", tradeName);
                    params.put("type", "1");
                    params.put("price", marketOrders.getPrice().subtract(new BigDecimal(Double.toString(precision))).toString());
                    qty = random.nextInt(buyEndNumber - buyStartNumber) + buyStartNumber;
                    qty_decimal = random.nextInt(10000);
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                    params.put("sign","");
                    params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                    sign = SignUtil.sign(oaxApiSecret, params);
                    params.put("sign", sign);
                    if(!buyPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                        result = HttpRequestUtil.sendPost(addOrderUrl, params);
                        log.info(params.get("market") + "买盘" + result+"qty={} - price={}", params.get("qty"), params.get("price"));
                    }

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
                params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                params.put("sign", "");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                if(!sellPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "卖盘" + result+"qty={} - price={}", params.get("qty"), params.get("price"));
                }

                dormant(random, dynamicSleep, fixSleep);

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
                        params.put("price", new BigDecimal(startPrice).add(new BigDecimal(Double.toString(precision))).toString());
                    } else {//未设置起始价格，取上次成交价格
                        if (new BigDecimal(endPrice).compareTo(trade.getPrice()) > 0) {//价格上限>交易价格
                            params.put("price", trade.getPrice().toString());//price=交易价格
                        } else {
                            params.put("price", String.valueOf(endPrice));//price=价格上限
                        }
                    }
                    qty = random1.nextInt(buyEndNumber - buyStartNumber) + buyStartNumber;
                    qty_decimal = random1.nextInt(10000);
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                }
                params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                params.put("sign","");
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                if(!buyPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "买盘" + result+"qty={} - price={}", params.get("qty"), params.get("price"));
                }
                dormant(random, dynamicSleep, fixSleep);

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
                qty = random.nextInt(buyEndNumber - buyStartNumber) + buyStartNumber;
                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                params.put("qty", list.get(0).getQty().add(new BigDecimal(params.get("qty"))).toString());
                params.put("sign","");
                params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                sign = SignUtil.sign(oaxApiSecret, params);
                params.put("sign", sign);

                if(!buyPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                    result = HttpRequestUtil.sendPost(addOrderUrl, params);
                    log.info(params.get("market") + "买盘" + result+"qty={} - price={}", params.get("qty"), params.get("price"));
                }
                dormant(random, dynamicSleep, fixSleep);
                //下卖单,补卖挂单
                list=ordersMapper.findMarketOrdersList(markerId,2);
                if(list.size()<15){
                    marketOrders = list.get(list.size()-1);
                    params.put("apiKey", oaxApiKey);
                    params.put("market", tradeName);
                    params.put("type", "2");
                    params.put("price", marketOrders.getPrice().add(new BigDecimal(Double.toString(precision))).toString());//price=交易价格
                    qty = random.nextInt(sellEndNumber - sellStartNumber) + sellStartNumber;
                    qty_decimal = random.nextInt(10000);
                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
                    params.put("sign","");
                    params.put("price", new BigDecimal(params.get("price")).setScale(market.getPriceDecimals(), BigDecimal.ROUND_HALF_UP).toString());
                    sign = SignUtil.sign(oaxApiSecret, params);
                    params.put("sign", sign);

                    if(!sellPriceList.contains(new BigDecimal(params.get("price")).doubleValue())){
                        result = HttpRequestUtil.sendPost(addOrderUrl, params);
                        log.info(params.get("market") + "卖盘" + result+"qty={} - price={}", params.get("qty"), params.get("price"));
                    }

                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程休眠
     * @param random
     * @param fixSleep
     * @param dynamicSleep
     * @throws InterruptedException
     */
    public void dormant(Random random, Integer fixSleep, Integer dynamicSleep) throws InterruptedException {
        Thread.sleep((random.nextInt(dynamicSleep) + fixSleep) * 1000);
    }

}

