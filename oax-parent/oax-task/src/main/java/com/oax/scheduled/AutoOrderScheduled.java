//package com.oax.scheduled;
//
//
//import com.oax.common.HttpRequestUtil;
//import com.oax.common.SignUtil;
//import com.oax.entity.front.Orders;
//import com.oax.entity.front.Trade;
//import com.oax.mapper.front.OrdersMapper;
//import com.oax.service.TradeService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.TreeMap;
//
///**
// * @author ：zl
// * @ClassName:：AutoOrderScheduled
// * @Description： 每1分钟自动刷单
// */
//@Slf4j
//@Controller
//public class AutoOrderScheduled {
//
//    @Value("${oax.api.api_key}")
//    private String oaxApiKey;
//
//    @Value("${oax.api.user_id}")
//    private String oaxApiUserId;
//
//    @Value("${oax.api.api_secret}")
//    private String oaxApiSecret;
//
//    @Value("${oax.api.url.add_order}")
//    private String addOrderUrl;
//
//
//    @Value("${oax.api.url.get_depth}")
//    private String getDepthUrl;
//
//    @Autowired
//    private TradeService tradeService;
//
//    @Autowired
//    private OrdersMapper ordersMapper;
//
//    /**
//     * 下单机器人
//     */
//    @Async
//    @Scheduled(fixedRate = 1000 * 60 * 10)
//    public void autoMakeOrder() {
//
//
//        //下单数量下限
//        int startNumber = 700;
//        //下单数量上限
//        int endNumber = 1300;
//        //价格下限,盘低
//        int startPrice = 269;
//        //价格上限，盘高
//        double endPrice = 0.03;
//
//
//
//
//        //1小时价格波动范围
//        int priceAreaHour = 4;
//        int timeArea =10;
//        Random random = new Random();
//        //时间分隔线
//        int separate = random.nextInt(timeArea)+1;
//
//        try {
//            for(int minute=0;minute<timeArea;minute++){
//                Trade trade = tradeService.selectByMarketId(35);
//                Orders top1SellOrder=ordersMapper.getMatchOrder(35,2,1);
//                Orders top1BuyOrder=ordersMapper.getMatchOrder(35,1,1);
//                //每分钟下买单数
//                int buyNumber=3;
//                //每分钟下卖单数
//                int saleNumber=3;
//                //价格
//                int price;
//                //数量整数位
//                int qty;
//                //数量小数位
//                int qty_decimal;
//                //签名
//                String sign;
//                //下单返回结果
//                String result;
//
//                List<BigDecimal> buyPriceList = new ArrayList<>();
//                List<BigDecimal> salePriceList = new ArrayList<>();
//                List<BigDecimal> buyQtyList = new ArrayList<>();
//                if(minute<separate+1){//前一半时间下单,价格下降
//                    //1分钟下单情况
//                    for (int i = 0; i < buyNumber; i++) {
//                        //下买单
//                        Random random1 = new Random();
//                        TreeMap<String, String> params = new TreeMap<String, String>();
//                        params.put("apiKey", oaxApiKey);
//                        params.put("market", "XBTC/USDT");
//                        params.put("type", "1");
//                        price =random1.nextInt(priceAreaHour);
//                        if (i == 0) {//下的第一笔买单
//                            if(top1SellOrder != null){//存在卖1单
//                                if(trade.getPrice().compareTo(top1SellOrder.getPrice())==0){//最后成交的价格=卖1价格
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1SellOrder.getQty().compareTo(top1SellOrder.getTradeQty())>=0){
//                                        params.put("qty", top1SellOrder.getQty().subtract(top1SellOrder.getTradeQty()).divide(new BigDecimal(2)).toString());
//                                    }
//                                }else if(trade.getPrice().compareTo(top1SellOrder.getPrice())<0) {//最后成交的价格<卖1价格//交易时，现在的卖1为卖2
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1SellOrder.getQty().compareTo(top1SellOrder.getTradeQty())>=0){
//                                        params.put("qty",top1SellOrder.getQty().subtract(top1SellOrder.getTradeQty()).multiply(new BigDecimal(1.5)).toString());
//                                    }
//                                }else {
//                                    if(new BigDecimal(endPrice).compareTo(top1SellOrder.getPrice())>0){//价格上限>top1Sell价格
//                                        params.put("price", top1SellOrder.getPrice().toString());//price=top1Sell价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    params.put("price", top1SellOrder.getPrice().toString());
//                                    qty_decimal = random1.nextInt(10000);
//                                    qty = random1.nextInt(endNumber - startNumber) + startNumber;
//                                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                                }
//                            }else {//不存在卖1单
//                                if(startPrice !=0){//取设置的起始价格
//                                    params.put("price",new BigDecimal(startPrice).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(price).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//                                }else {//未设置起始价格，取上次成交价格
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                }
//                                qty_decimal = random1.nextInt(10000);
//                                qty = random1.nextInt(endNumber - startNumber) + startNumber;
//                                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                            }
//                            buyPriceList.add(new BigDecimal(params.get("price")));//记录每次的买单价格
//                            buyQtyList.add(new BigDecimal(params.get("qty")));//记录每次的买单数量
//                        } else {//下的其他笔买单
//                                //价格为，前一笔买单的价格减去0.000i，后面的价格会更低
//                                params.put("price", buyPriceList.get(buyPriceList.size()-1).subtract(new BigDecimal(1).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//                            }
//                            buyPriceList.add(new BigDecimal(params.get("price")));//记录买单价格
//                            qty = random1.nextInt(endNumber - startNumber) + startNumber;
//                            qty_decimal = random1.nextInt(10000);
//                            params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                            buyQtyList.add(new BigDecimal(params.get("qty")));
//
//
//                        sign = SignUtil.sign(oaxApiSecret, params);
//                        params.put("sign", sign);
//
//                        result = HttpRequestUtil.sendPost(addOrderUrl, params);
//                        log.info(params.get("market") + "买盘" + result);
//                        Thread.sleep(100);
//
//                        //下卖单
//                        Random random2 = new Random();
//                        params.put("apiKey", oaxApiKey);
//                        params.put("market", "XBTC/USDT");
//                        params.put("type", "2");
//                        price = random2.nextInt(priceAreaHour);
//                        if (i == 0 ) {
//                            if(top1BuyOrder != null){//有买1记录
//                                if(trade.getPrice().compareTo(top1BuyOrder.getPrice())==0){//最后成交的价格=买1价格
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1BuyOrder.getQty().compareTo(top1BuyOrder.getTradeQty())>=0){
//                                        params.put("qty", top1BuyOrder.getQty().subtract(top1BuyOrder.getTradeQty()).divide(new BigDecimal(4)).toString());
//                                    }
//                                }else if(trade.getPrice().compareTo(top1BuyOrder.getPrice())<0) {//最后成交的价格<买1价格//交易时，现在的买1为买2
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1BuyOrder.getQty().compareTo(top1BuyOrder.getTradeQty())>=0){
//                                        params.put("qty", top1BuyOrder.getQty().subtract(top1BuyOrder.getTradeQty()).multiply(new BigDecimal(1.5)).toString());
//                                    }
//                                }else {
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    qty = random2.nextInt(endNumber - startNumber) + startNumber;
//                                    qty_decimal = random2.nextInt(10000);
//                                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                                }
//                            }else {//没有买1记录
//                                if(startPrice !=0){//取设置的起始价格
//                                    params.put("price",new BigDecimal(startPrice).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(price).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//                                }else {//取上一笔成交的价格
//                                    params.put("price", trade.getPrice().toString());
//                                }
//                                qty = random2.nextInt(endNumber - startNumber) + startNumber;
//                                qty_decimal = random2.nextInt(10000);
//                                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                            }
//                            salePriceList.add(new BigDecimal(params.get("price")));//记录卖单价格
//                        } else {//后一半卖单
//                                //价格为，前一笔买单的价格减去0.0001，后面的价格会更低
//                                params.put("price",salePriceList.get(salePriceList.size()-1).subtract(new BigDecimal(1).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//
//                            salePriceList.add(new BigDecimal(params.get("price")));
//                            qty = random2.nextInt(endNumber - startNumber) + startNumber;
//                            qty_decimal = random2.nextInt(10000);
//                            params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//
//                        }
//                        params.put("sign", "");
//                        sign = SignUtil.sign(oaxApiSecret, params);
//                        params.put("sign", sign);
//
//                        result = HttpRequestUtil.sendPost(addOrderUrl, params);
//                        log.info(params.get("market") + "卖盘" + result);
//                        Thread.sleep(100);
//                    }
//                    Thread.sleep(55000);
//                }else {//后一半时间下单，价格上升
//                    //1分钟下单情况
//                    for (int i = 0; i < buyNumber; i++) {
//                        //下买单
//                        Random random1 = new Random();
//                        TreeMap<String, String> params = new TreeMap<>();
//                        params.put("apiKey", oaxApiKey);
//                        params.put("market", "XBTC/USDT");
//                        params.put("type", "1");
//                        price =random1.nextInt(priceAreaHour);
//                        if (i == 0) {//下的第一笔买单
//                            if(top1SellOrder != null){//存在卖1单
//                                if(trade.getPrice().compareTo(top1SellOrder.getPrice())==0){//最后成交的价格=卖1价格
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1SellOrder.getQty().compareTo(top1SellOrder.getTradeQty())>=0){
//                                        params.put("qty", top1SellOrder.getQty().add(top1SellOrder.getTradeQty()).divide(new BigDecimal(2)).toString());
//                                    }
//                                }else if(trade.getPrice().compareTo(top1SellOrder.getPrice())<0) {//最后成交的价格<卖1价格//交易时，现在的卖1为卖2
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1SellOrder.getQty().compareTo(top1SellOrder.getTradeQty())>=0){
//                                        params.put("qty",top1SellOrder.getQty().add(top1SellOrder.getTradeQty()).multiply(new BigDecimal(1.5)).toString());
//                                    }
//                                }else {
//                                    if(new BigDecimal(endPrice).compareTo(top1SellOrder.getPrice())>0){//价格上限>top1Sell价格
//                                        params.put("price", top1SellOrder.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    qty_decimal = random1.nextInt(10000);
//                                    qty = random1.nextInt(endNumber - startNumber) + startNumber;
//                                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                                }
//                            }else {//不存在卖1单
//                                if(startPrice !=0){//取设置的起始价格
//                                    params.put("price",new BigDecimal(startPrice).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(price).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//                                }else {//未设置起始价格，取上次成交价格
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                }
//                                qty_decimal = random1.nextInt(10000);
//                                qty = random1.nextInt(endNumber - startNumber) + startNumber;
//                                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                            }
//                            buyPriceList.add(new BigDecimal(params.get("price")));//记录每次的买单价格
//                            buyQtyList.add(new BigDecimal(params.get("qty")));//记录每次的买单数量
//                        } else {//下的其他笔买单
//                            //价格为，前一笔买单的价格减去0.0001，后面的价格会更低
//                            params.put("price", buyPriceList.get(buyPriceList.size()-1).add(new BigDecimal(1).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//                        }
//                        buyPriceList.add(new BigDecimal(params.get("price")));//记录买单价格
//                        qty = random1.nextInt(endNumber - startNumber) + startNumber;
//                        qty_decimal = random1.nextInt(10000);
//                        params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                        buyQtyList.add(new BigDecimal(params.get("qty")));
//
//                        sign = SignUtil.sign(oaxApiSecret, params);
//                        params.put("sign", sign);
//                        result = HttpRequestUtil.sendPost(addOrderUrl, params);
//                        log.info(params.get("market") + "买盘" + result);
//                        Thread.sleep(100);
//
//                        //下卖单
//                        Random random2 = new Random();
//                        params.put("apiKey", oaxApiKey);
//                        params.put("market", "XBTC/USDT");
//                        params.put("type", "2");
//                        price = random2.nextInt(priceAreaHour);
//                        if (i == 0 ) {
//                            if(top1BuyOrder != null){//有买1记录
//                                if(trade.getPrice().compareTo(top1BuyOrder.getPrice())==0){//最后成交的价格=买1价格
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1BuyOrder.getQty().compareTo(top1BuyOrder.getTradeQty())>=0){
//                                        params.put("qty", top1BuyOrder.getQty().add(top1BuyOrder.getTradeQty()).divide(new BigDecimal(4)).toString());
//                                    }
//                                }else if(trade.getPrice().compareTo(top1BuyOrder.getPrice())<0) {//最后成交的价格<买1价格//交易时，现在的买1为买2
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    if(top1BuyOrder.getQty().compareTo(top1BuyOrder.getTradeQty())>=0){
//                                        params.put("qty", top1BuyOrder.getQty().add(top1BuyOrder.getTradeQty()).multiply(new BigDecimal(1.5)).toString());
//                                    }
//                                }else {
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                    qty = random2.nextInt(endNumber - startNumber) + startNumber;
//                                    qty_decimal = random2.nextInt(10000);
//                                    params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                                }
//                            }else {//没有买1记录
//                                if(startPrice !=0){//取设置的起始价格
//                                    params.put("price",new BigDecimal(startPrice).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(price).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//                                }else {//取上一笔成交的价格
//                                    if(new BigDecimal(endPrice).compareTo(trade.getPrice())>0){//价格上限>交易价格
//                                        params.put("price", trade.getPrice().toString());//price=交易价格
//                                    }else {
//                                        params.put("price", String.valueOf(endPrice));//price=价格上限
//                                    }
//                                }
//                                qty = random2.nextInt(endNumber - startNumber) + startNumber;
//                                qty_decimal = random2.nextInt(10000);
//                                params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//                            }
//                            salePriceList.add(new BigDecimal(params.get("price")));//记录卖单价格
//                        } else {//后一半卖单
//                            //取前一单价格加0.000(i-buyNumber/2)，后面的价格会更高
//                            params.put("price",salePriceList.get(salePriceList.size()-1).add(new BigDecimal(1).divide(new BigDecimal(10000)).setScale(4, BigDecimal.ROUND_HALF_UP)).toString());
//
//                            salePriceList.add(new BigDecimal(params.get("price")));
//                            qty = random2.nextInt(endNumber - startNumber) + startNumber;
//                            qty_decimal = random2.nextInt(10000);
//                            params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));
//
//                        }
//                        params.put("sign", "");
//                        sign = SignUtil.sign(oaxApiSecret, params);
//                        params.put("sign", sign);
//
//                        result = HttpRequestUtil.sendPost(addOrderUrl, params);
//                        log.info(params.get("market") + "卖盘" + result);
//                        Thread.sleep(100);
//                    }
//                    Thread.sleep(55000);
//                }
//
//            }
//            } catch(Exception e){
//                e.printStackTrace();
//            }
//
//        }
//
//    }