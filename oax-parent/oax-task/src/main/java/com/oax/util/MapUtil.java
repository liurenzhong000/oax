package com.oax.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oax.entity.front.Kline;
import com.oax.entity.front.MarketInfo;

/** 
* @ClassName:：MapUtil 
* @Description： map处理工具类
* @author ：xiangwh  
* @date ：2018年6月8日 下午5:55:07 
*  
*/
public class MapUtil {
	 //推送不同市场分区的交易对信息
    public static Map<String, List<MarketInfo>> sendMarketInfo(List<MarketInfo> list) {
    	Map<String , List<MarketInfo>> map = new HashMap<>();
    	for (MarketInfo tradeCoin : list) {
    		//如果map中包含了key(市场分区币id) ,就将该对象加入到集合
			if (map.containsKey(tradeCoin.getMarketCoinName())) {
				map.get(tradeCoin.getMarketCoinName()).add(tradeCoin);
			//如果map中不包含key(市场分区币id),直接创建一个list集合put进去
			}else{
				List<MarketInfo> l = new  ArrayList<>();
				l.add(tradeCoin);
				map.put(tradeCoin.getMarketCoinName(), l);
			}
			
		}
    	return map;
    }
    
    //获取不同的时间类型(大于1天的)的kline数据
    
    public static Map<Integer, List<Kline>> sendKlineInfo(List<Kline> list){
    	Map<Integer , List<Kline>> map = new HashMap<>();
    	for (Kline kline : list) {
    		//如果map中包含了key(时间类型) ,就将该对象加入到集合
			if (map.containsKey(kline.getMinType())) {
				map.get(kline.getMinType()).add(kline);
			//如果map中不包含key(时间类型),直接创建一个list集合put进去
			}else{
				List<Kline> l = new  ArrayList<>();
				l.add(kline);
				map.put(kline.getMinType(),l);
			}
		}
    	return map;
    }
}
