package com.oax.scheduled;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.api.AccountsResponse;
import com.oax.api.ApiClient;
import com.oax.api.DepthResponse;
import com.oax.api.TradeResponse;
import com.oax.common.EmptyHelper;
import com.oax.common.HttpRequestUtil;
import com.oax.common.SignUtil;
import com.oax.domain.*;
import com.oax.entity.front.MarketCoinInfo;
import com.oax.entity.front.MarketOrders;
import com.oax.entity.front.Orders;
import com.oax.mapper.front.OrdersMapper;
import com.oax.service.OrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Slf4j
@Controller
public class OrdersScheduled{

    @Value("${oax.api.api_key}")
    private String oaxApiKey;

    @Value("${oax.api.user_id}")
    private String oaxApiUserId;

    @Value("${oax.api.api_secret}")
    private String oaxApiSecret;

    @Value("${oax.api.url.add_order}")
    private String addOrderUrl;

    @Value("${oax.api.url.cancel_order}")
    private String cancelOrder;

    @Value("${oax.api.url.get_depth}")
    private String getDepthUrl;

    @Value("${huobi.api.api_key}")
    private String huobiApiKey;

    @Value("${huobi.api.api_secret}")
    private String huobiApiSecret;

    @Value("${huobi.api.host}")
    private String apiHost;

    @Value("${huobi.api.url}")
    private String apiUrl;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrdersMapper ordersMapper;
    /**创建线程池*/
    final ExecutorService threadPool = Executors.newFixedThreadPool(3);

    int startNumber = 400;
    int endNumber = 800;

    /**
     * OKEX下单机器人
     */
//    @Async
//    @Scheduled(cron = "0/30 * * * * ? ")
    public void batchOrderByOk() {
        //本地测试，设置ssr代理
        Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", Integer.valueOf("1080")));
        String baseUrl = "https://www.okex.com/api/spot/v3/instruments/<instrument-id>/book";
        //查询交易订单的个数
        int pageSize = 10;
        Map<MarketCoinInfo, List<OKEXDepthResponse>> bidsDepthMap = new HashMap<>();
        Map<MarketCoinInfo, List<OKEXDepthResponse>> asksDepthMap = new HashMap<>();
        // 获取oax的所有开启交易对
        List<MarketCoinInfo> marketInfoList = orderService.selectAutoAddMarket();
        marketInfoList.forEach(item -> item.setMarketCoinName(item.getMarketCoinName().toUpperCase().replace("/","_")));
        log.info("marketInfoList:{}", marketInfoList);
        //去请求okex的交易数据
        marketInfoList.forEach(item ->{
            List<OKEXDepthResponse> bidsResponseList = new ArrayList<>();
            List<OKEXDepthResponse> asksResponseList = new ArrayList<>();
            String responseStr = HttpRequestUtil.sendGet(baseUrl.replace("<instrument-id>", item.getMarketCoinName()), "size=" + pageSize, proxy);
            if (EmptyHelper.isEmpty(responseStr)) {
                log.info("okex 不存在交易对:{}", item.getMarketCoinName());
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            log.info(jsonObject.toJSONString());
            JSONArray bidsArray = jsonObject.getJSONArray("bids");//买盘
            JSONArray asksArray = jsonObject.getJSONArray("asks");//卖盘
            bidsArray.forEach(bids ->{
                JSONArray bid = (JSONArray) bids;
                OKEXDepthResponse bidsResponse = transitionToOKEXDepthResponse(bid);
                bidsResponseList.add(bidsResponse);

            });
            asksArray.forEach(asks ->{
                JSONArray ask = (JSONArray) asks;
                OKEXDepthResponse bidsResponse = transitionToOKEXDepthResponse(ask);
                asksResponseList.add(bidsResponse);
            });

            bidsDepthMap.put(item, bidsResponseList);
            asksDepthMap.put(item, asksResponseList);
        });

        bidsDepthMap.forEach((marketInfo, bidsResponseList) ->{
            bidsResponseList.forEach(bidsDepth ->{
                addOrder(marketInfo.getMarketCoinName().replace("_","/"), 1+"", bidsDepth.getPrice().toString(), bidsDepth.getSize().toString());
            });
        });

        asksDepthMap.forEach((marketInfo, asksResponseList) ->{
            asksResponseList.forEach(bidsDepth ->{
                addOrder(marketInfo.getMarketCoinName().replace("_","/"), 2+"", bidsDepth.getPrice().toString(), bidsDepth.getSize().toString());
            });
        });

    }

    private OKEXDepthResponse transitionToOKEXDepthResponse(JSONArray array){
        BigDecimal price = new BigDecimal(array.get(0).toString());
        BigDecimal size = new BigDecimal(array.get(1).toString());
        Integer numOrders = Integer.valueOf(array.get(2).toString());
        OKEXDepthResponse bidsResponse = OKEXDepthResponse.newInstance(price, size, numOrders);
        return bidsResponse;
    }

    //下单
    private String addOrder(String market, String type, String price, String qty){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("apiKey", oaxApiKey);
        params.put("market", market);
        params.put("type", type);
        params.put("price", price);
        params.put("qty", qty);
        params.put("appSecret", oaxApiSecret);
        String sign = SignUtil.sign(oaxApiSecret, params);
        params.put("sign", sign);
        String response = HttpRequestUtil.sendPost(addOrderUrl, params);
        log.info("自动下单 市场：{} - 类型：{} - 价格：{} - 个数：{} - 下单返回数据：{}", market, type, price, qty, response);
        return response;
    }


    /**
     * 火币下单机器人
     */
    @Async
    //@Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 365)
    @Scheduled(fixedRate = 1000 * 5)
    public void batchAddOrdersInOax() {
        ApiClient client;
        try {
            // 获取oax的所有开启交易对
            List<MarketCoinInfo> marketInfoList = orderService.selectAutoAddMarket();
            Map<MarketCoinInfo, DepthResponse<?>> maketMap = new HashMap<>();
            client = new ApiClient(huobiApiKey, huobiApiSecret, null, apiHost, apiUrl);
            ApiClient finalClient = client;

            marketInfoList.forEach(item -> threadPool.execute(() -> {
                try {
                    handle(item, finalClient);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /** 挂单 */
    public void handle(MarketCoinInfo market,ApiClient client) throws InterruptedException {
        DepthRequest depthRequest = new DepthRequest();
        depthRequest.setSymbol(market.getMarketCoinName().toLowerCase().replace("/", ""));
        depthRequest.setType("step0");
        DepthResponse<?> depth = client.depth(depthRequest);
        ArrayList<Double> sellPriceList = new ArrayList();
        ArrayList<Double> buyPriceList = new ArrayList();
        List<MarketOrders> sellList=ordersMapper.findMarketOrders(market.getId(),2,null);
        sellList.forEach(item->sellPriceList.add(item.getPrice().doubleValue()));
        List<MarketOrders> buyList=ordersMapper.findMarketOrders(market.getId(),1,null);
        buyList.forEach(item->buyPriceList.add(item.getPrice().doubleValue()));
        if (depth != null) {
            if ("ok".equals(depth.getStatus())) {
                List<List<BigDecimal>> bids = depth.getTick().getBids();
                List<List<BigDecimal>> asks = depth.getTick().getAsks();

                for(int i=0;i<15;i++){
                    //卖盘
                    if (asks != null && asks.size() > 0 && i<asks.size()) {
                        //参数
                        TreeMap<String, String> params = new TreeMap<String, String>();
                        params.put("apiKey", oaxApiKey);
                        params.put("market", market.getMarketCoinName());
                        params.put("type", "2");
                        params.put("price", String.valueOf(asks.get(i).get(0).doubleValue()));
                        String askQty = asks.get(i).get(1).divide(new BigDecimal("5"),4,BigDecimal.ROUND_HALF_UP).toString();
                        params.put("qty", askQty);
                        /*int qty_decimal = random.nextInt(10000);
                        int qty = random.nextInt(endNumber - startNumber) + startNumber;
                        params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));*/
                        String sign1 = SignUtil.sign(oaxApiSecret, params);
                        params.put("sign", sign1);

                        if(!sellPriceList.contains(asks.get(i).get(0).doubleValue())){
                            String sms1 = HttpRequestUtil.sendPost(addOrderUrl, params);
                            log.info(market.getMarketCoinName() + "卖盘" + i + sms1);
                        }

                        Thread.sleep(100);

                        params.put("type", "1");
                        params.put("price", String.valueOf(bids.get(i).get(0).doubleValue()));
                        String bidQty = bids.get(i).get(1).divide(new BigDecimal("5"),4,BigDecimal.ROUND_HALF_UP).toString();
                        params.put("qty", bidQty);
                        /*qty_decimal = random.nextInt(10000);
                        qty = random.nextInt(endNumber - startNumber) + startNumber;
                        params.put("qty", String.valueOf(qty).concat(".").concat(String.valueOf(qty_decimal)));*/
                        params.put("sign", "");
                        String sign2 = SignUtil.sign(oaxApiSecret, params);
                        params.put("sign", sign2);

                        if(!buyPriceList.contains(bids.get(i).get(0).doubleValue())){
                            String sms2 = HttpRequestUtil.sendPost(addOrderUrl, params);
                            log.info(market.getMarketCoinName() + "买盘" + i + sms2);
                        }

                        Thread.sleep(100);
                    }
                }
            }
        }
        autoOrder(market,client,buyList,sellList,buyPriceList,sellPriceList);
    }

    /** 下单 */
    public void autoOrder(MarketCoinInfo market,ApiClient client,List<MarketOrders> buyList,List<MarketOrders> sellList,ArrayList<Double> buyPriceList,ArrayList<Double> sellPriceList){
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setSymbol(market.getMarketCoinName().toLowerCase().replace("/", ""));
        tradeRequest.setSize(1);
        TradeResponse<?> trade = client.trade(tradeRequest);
        if(trade != null){
            if("ok".equals(trade.getStatus())){
                TradeData tradeData = trade.getTick();
                TradeDetail data = (TradeDetail) tradeData.getData().get(0);
                TreeMap<String, String> params = new TreeMap<String, String>();
                params.put("apiKey", oaxApiKey);
                params.put("market", market.getMarketCoinName());
                params.put("price", String.valueOf(data.getPrice().doubleValue()));
                String askQty = data.getAmount().divide(new BigDecimal("5"),4,BigDecimal.ROUND_HALF_UP).toString();
                params.put("qty", askQty);
                List<MarketOrders> tempList = new ArrayList<>();
                if(buyPriceList.get(0)<data.getPrice().doubleValue()){
                    if(!buyPriceList.contains(data.getPrice().doubleValue())){
                        tempList = sellList.stream().filter(item->item.getPrice().compareTo(data.getPrice())<0).collect(Collectors.toList());
                        if(tempList.size()>0){
                            params.put("qty", tempList.stream().map(MarketOrders::getQty).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                        }
                        params.put("type", "1");
                        String sign = SignUtil.sign(oaxApiSecret, params);
                        params.put("sign", sign);
                        String sms1 = HttpRequestUtil.sendPost(addOrderUrl, params);
                        log.info(market.getMarketCoinName() + "实时价格" +  data.getPrice().toString() + sms1);
                    }
                }else if(buyPriceList.get(0)>data.getPrice().doubleValue()){
                    if(!sellPriceList.contains(data.getPrice().doubleValue())){
                        tempList = buyList.stream().filter(item->item.getPrice().compareTo(data.getPrice())>0).collect(Collectors.toList());
                        if(tempList.size()>0){
                            params.put("qty", tempList.stream().map(MarketOrders::getQty).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                        }
                        params.put("type", "2");
                        String sign = SignUtil.sign(oaxApiSecret, params);
                        params.put("sign", sign);
                        String sms1 = HttpRequestUtil.sendPost(addOrderUrl, params);
                        log.info(market.getMarketCoinName() + "实时价格" +  data.getPrice().toString()+ sms1);
                    }
                }
            }
        }
    }

    /**5分钟撤单*/
    @Async
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void cannel() {
    	 List<MarketCoinInfo> marketInfoList = orderService.selectAutoAddMarket();
    	 Integer maxOrderId= orderService.selectLastOrderId()-2500;

    	 if(marketInfoList!=null&&marketInfoList.size()>0){
             List<Orders> orderAutoList;
             String sms;
             for(MarketCoinInfo marketInfo:marketInfoList){
                 orderAutoList= orderService.selectAutoOrders(marketInfo.getId(),oaxApiUserId, maxOrderId);
                 // 撤销订单
                 if (orderAutoList != null && orderAutoList.size() > 0) {
                     for (int i = 0; i < orderAutoList.size(); i++) {
                         TreeMap<String, String> params = new TreeMap<String, String>();
                         params.put("apiKey", oaxApiKey);
                         params.put("id", orderAutoList.get(i).getId().toString());
                         params.put("userId", oaxApiUserId);
                         String sign = SignUtil.sign(oaxApiSecret, params);
                         params.put("sign", sign);
                         sms = HttpRequestUtil.sendPost(cancelOrder, params);

                         log.info(orderAutoList.get(i).getLeftCoinName()+"/"+ orderAutoList.get(i).getRightCoinName()+ " 撤单" + sms);
                         params=null;
                     }
                 }
             }

         }
    }
//
    /**
     * 每隔60秒在huobi下单，真实用户的订单
     */
  //  @Async
  //  @Scheduled(fixedRate = 1 * 60 * 1000)
    public void batchAddOrdersInHuobi() {
        ApiClient client = new ApiClient(huobiApiKey, huobiApiSecret, null, apiHost, apiUrl);
        AccountsResponse<?> accounts = client.accounts();
        @SuppressWarnings("unchecked")
		List<Accounts> list = (List<Accounts>) accounts.getData();
        Integer accountId = list.get(0).getId();
        // 获取oax的所有开启交易对
        List<MarketCoinInfo> marketInfoList = orderService.selectAutoAddMarket();

        // 查询交易对下生成的订单
        if (marketInfoList != null && marketInfoList.size() > 0) {
            for (int i = 0; i < marketInfoList.size(); i++) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, -60);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:59");
                String beginTime = sdf1.format(c.getTime());
                String endTime = sdf2.format(c.getTime());

                List<Orders> ordersList = orderService.selectOrdersByTimeAndMarketId(marketInfoList.get(i).getId(), oaxApiUserId, beginTime, endTime);
                if (ordersList != null && ordersList.size() > 0) {
                    for (int j = 0; j < ordersList.size(); j++) {
                        CreateOrderRequest createOrderReq = new CreateOrderRequest();
                        createOrderReq.accountId = String.valueOf(accountId);
                        createOrderReq.amount = String.valueOf(ordersList.get(j).getQty());
                        createOrderReq.price = String.valueOf(ordersList.get(j).getPrice());
                        createOrderReq.symbol = marketInfoList.get(i).getMarketCoinName().toLowerCase().replace("/", "");
                        if (ordersList.get(j).getType().intValue() == 1) {
                            createOrderReq.type = CreateOrderRequest.OrderType.BUY_LIMIT;
                        } else if (ordersList.get(j).getType().intValue() == 2) {
                            createOrderReq.type = CreateOrderRequest.OrderType.SELL_LIMIT;
                        }
                        createOrderReq.source = "api";
                        Long orderId = client.createOrder(createOrderReq);
                        String data = client.placeOrder(orderId);
                        log.info("火币下单" + data);
                    }
                }
            }
        }
    }


    /**
     * 每天凌晨1点删除0点之前所有自动下单撤销的订单
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public void batchDelOrdersInOax() {
        // 获取oax的所有开启交易对
        List<MarketCoinInfo> marketInfoList = orderService.selectAutoAddMarket();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String endTime = sdf.format(c.getTime());

        if(marketInfoList!=null&&marketInfoList.size()>0){
            for(int i=0;i<marketInfoList.size();i++){
                // 删除的订单
                Integer counts = orderService.deleteOrders(marketInfoList.get(i).getId(),oaxApiUserId, endTime);
                log.info(endTime + "删除订单" + counts);
            }
        }
    }

    /**
     * 每隔一段时间自动下单
     */
 //   @Async
 //   @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 365)
    public void addOrdersInOax() throws InterruptedException {
        while (true){
            // 获取oax的所有开启交易对
            Thread.sleep(1);
            List<MarketCoinInfo> marketInfoList = null;
            try {
                ApiClient client = new ApiClient(huobiApiKey, huobiApiSecret, null, apiHost, apiUrl);
                DepthRequest depthRequest = new DepthRequest();

                marketInfoList = orderService.selectAutoAddMarket();
                Random random = new Random(); //创建一个Random类对象实例
                Integer type = random.nextInt(2)+1;
                Integer sleepSecond = random.nextInt(7)+3;
                TreeMap<String, String> params = new TreeMap<String, String>();

                if(marketInfoList!=null&&marketInfoList.size()>0){
                    for(int i=0;i<marketInfoList.size();i++){
                        //获取深度
                        depthRequest.setSymbol(marketInfoList.get(i).getMarketCoinName().toLowerCase().replace("/", ""));
                        depthRequest.setType("step0");
                        DepthResponse<?> huoBiDepth = client.depth(depthRequest);

                        params.put("market",marketInfoList.get(i).getMarketCoinName());
                        String jsonString=HttpRequestUtil.sendPost(getDepthUrl, params);
                        JSONObject jobj= JSONObject.parseObject(jsonString);
                        JSONArray jArray=null;
                        String price=null;
                        String qty=null;
                        if(jobj!=null&&"1".equals(jobj.get("code"))){
                            JSONObject depth=jobj.getJSONObject("data");
                            //下买单
                            if(type==1){
                                List<List<BigDecimal>> asks = huoBiDepth.getTick().getAsks();//获取火币卖盘交易深度

                                jArray=depth.getJSONArray("sellList");
                                if(jArray!=null&&jArray.size()>0){
                                    price=jArray.getJSONObject(0).getString("price");//获取自己卖一价格

                                    log.info("自己价格"+price+"------火币价格"+asks.get(0).get(0).toString());
                                    //如果自己卖一价格大于火币卖一价格的1.1倍，则跳过，不下单。
                                    if(new BigDecimal(price).compareTo(new BigDecimal(asks.get(0).get(0).toString()).multiply(new BigDecimal("1.1")))>0){
                                        log.info("买单价格不合理，跳过");
                                        Thread.sleep(sleepSecond*1000);
                                        continue;
                                    }
                                    qty= new BigDecimal(jArray.getJSONObject(0).getString("qty")).divide(new BigDecimal(2)).setScale(4,BigDecimal.ROUND_HALF_UP).toString();
                                }
                                //下卖单
                            }else if(type==2){
                                List<List<BigDecimal>> bids = huoBiDepth.getTick().getBids();//获取火币买盘交易深度

                                jArray=depth.getJSONArray("buyList");
                                if(jArray!=null&&jArray.size()>0){
                                    price=jArray.getJSONObject(0).getString("price");//获取自己买一价格
                                    log.info("自己价格"+price+"------火币价格"+bids.get(0).get(0).toString());

                                    //如果自己买一价格的1.1倍小于火币买一价格，则跳过，不下单。
                                    if((new BigDecimal(price).multiply(new BigDecimal("1.1"))).compareTo(new BigDecimal(bids.get(0).get(0).toString()))<0){
                                        log.info("卖单价格不合理，跳过");
                                        Thread.sleep(sleepSecond*1000);
                                        continue;
                                    }
                                    qty= new BigDecimal(jArray.getJSONObject(0).getString("qty")).divide(new BigDecimal(2)).setScale(4,BigDecimal.ROUND_HALF_UP).toString();
                                }
                            }
                            TreeMap<String, String> params2 = new TreeMap<String, String>();
                            params2.put("apiKey", oaxApiKey);
                            params2.put("market", marketInfoList.get(i).getMarketCoinName());
                            params2.put("type", type+"");
                            params2.put("price", price);
                            params2.put("qty", qty);
                            String sign2 = SignUtil.sign(oaxApiSecret, params2);
                            params2.put("sign", sign2);

                            String sms2 = HttpRequestUtil.sendPost(addOrderUrl, params2);
                            log.info("卖单价格合理,自动下单"+marketInfoList.get(i).getMarketCoinName()  + type + sms2);

                            params2=null;
                            sign2=null;
                            sms2=null;
                            depth=null;
                            Thread.sleep(sleepSecond*1000);
                        }
                        jobj=null;
                        jsonString=null;
                        jArray=null;
                        price=null;
                        qty=null;
                    }
                }
                depthRequest=null;
                client=null;
                marketInfoList=null;
                random=null;
                type=null;
                sleepSecond=null;
                params=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

@Data
class OKEXDepthResponse{
    /**价格*/
    private BigDecimal price;
    /**数量*/
    private BigDecimal size;
    /**组成此条深度的订单数量*/
    private Integer numOrders;

    public static OKEXDepthResponse newInstance(BigDecimal price, BigDecimal size, Integer numOrders) {
        OKEXDepthResponse response = new OKEXDepthResponse();
        response.setPrice(price);
        response.setSize(size);
        response.setNumOrders(numOrders);
        return response;
    }
}
