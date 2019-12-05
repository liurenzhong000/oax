package com.oax.async;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.MarketInfo;
import com.oax.mapper.front.TradeSnapshotMapper;

@Component
public class MarketInfoUtils {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    TradeSnapshotMapper mapper;
    public BigDecimal getTranformCoin(Integer leftCoinId,Integer rightCoinId,Integer marketId){
        BigDecimal lastTradePrice = null;
        List<MarketInfo> marketInfoList = redisUtil.getList(RedisKeyEnum.MARKET_LIST.getKey(), MarketInfo.class);
        for (MarketInfo marketInfo : marketInfoList) {
            boolean flag1 = leftCoinId==marketInfo.getCoinId()&&rightCoinId==marketInfo.getMarketCoinId();
            boolean flag2 = leftCoinId==marketInfo.getMarketCoinId()&&rightCoinId==marketInfo.getCoinId();
            if(flag1){
                lastTradePrice = marketInfo.getLastTradePrice();
                break;
            }
            if(flag2){
                if(marketInfo.getLastTradePrice().compareTo(BigDecimal.ZERO)==0){
                    lastTradePrice = BigDecimal.ZERO;
                    break;
                }
                lastTradePrice = BigDecimal.ONE.divide(marketInfo.getLastTradePrice(), 8,BigDecimal.ROUND_FLOOR);
                break;
            }
        }
        if(lastTradePrice==null){
            if (marketId!=null){
                lastTradePrice = mapper.getLastPrice(marketId);
            }else {
                //判断是左币右币顺序是否颠倒 需要查询出 市场id，左币id，右币id
                MarketInfo marketInfo = mapper.getMarketIdByCoinId(leftCoinId,rightCoinId);
                if(leftCoinId==marketInfo.getCoinId()&&rightCoinId==marketInfo.getMarketCoinId()){
                    lastTradePrice = mapper.getLastPrice(marketId);
                }else{
                    lastTradePrice = BigDecimal.ONE.divide(mapper.getLastPrice(marketId), 8,BigDecimal.ROUND_FLOOR);
                }
                lastTradePrice = mapper.getLastPrice(marketId);
            }
        }
        return lastTradePrice;
    }

}
