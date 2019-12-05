package com.oax.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.front.Kline;
import com.oax.entity.front.KlineInfo;
import com.oax.mapper.front.KlineMapper;
import com.oax.service.KlineService;

/** 
* @ClassName:：KlineServiceImpl 
* @Description： k线查询业务
* @author ：xiangwh  
* @date ：2018年7月3日 下午3:19:12 
*  
*/
@Service
public class KlineServiceImpl implements KlineService {
	@Autowired
	private KlineMapper mapper;
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public List<Kline> findListByMarketId(Map<String, Object> params) {
		return mapper.findListByMarketId(params);
	}
	@Override
	public List<Kline> findListByMarketIdGtOrLt(Integer marketId) {
		return mapper.findListByMarketIdGtOneDay(marketId);
	}
	@Override
	public List<Kline> pullKline(String beginDate, String endDate,Integer minType) {	
		return mapper.pullKline(beginDate, endDate, minType);		
	}
	@Transactional
	@Override
	public boolean batchSave(List<Kline> list) {
		Integer rows = null;
		if (list.get(0).getMinType()>=1440) {
			for (Kline kline : list) {
				kline.setCreateTime(getDate(new Date()));
			}
			rows = mapper.batchSaveGt1440(list);
		}else {
			rows = mapper.batchSave(list);
		}
		if (rows>0) {
			return true;
		}
		return false;
	}
	@Transactional
	@Override
	public void removeKline(List<KlineInfo> list) {
		for (KlineInfo klineInfo : list) {
			if (klineInfo.getAmount()>1500) {
				//查询该市场该类型的最新的1500条的k线的id
				List<Integer> idsList = mapper.getKlineIds(klineInfo.getMarketId(), klineInfo.getMinType());
				//将旧数据移到历史表中
				mapper.removeKline(klineInfo.getMarketId(), klineInfo.getMinType(),idsList);
				//删除旧数据
				mapper.deleteKline(klineInfo.getMarketId(), klineInfo.getMinType(),idsList);
			}
		}
		
	}
	@Override
	public List<KlineInfo> selectList() {
		return mapper.selectListGT1500();
	}

	@Override
	public void setKlineRedis(List<Kline> list) {
		for (Kline kline : list) {
//			 List<Kline> klineList = mapper.getKlineList(kline.getMarketId(),kline.getMinType());

			//限制最多查询出1440个，后期优化成按时间范围查询缓存
			List<Kline> klineList = mapper.getKlineListLimit(kline.getMarketId(),kline.getMinType());
			Collections.reverse(klineList);//顺序反转，查询出来的数据是id倒叙的
			klineList.forEach(item->{//去0
				item.setOpen(item.getOpen().stripTrailingZeros());
				item.setClose(item.getClose().stripTrailingZeros());
				item.setHigh(item.getHigh().stripTrailingZeros());
				item.setLow(item.getLow().stripTrailingZeros());
				item.setQty(item.getQty().stripTrailingZeros());
			});

			redisUtil.setList(RedisKeyEnum.MARKET_KLINE_LIST.getKey()+kline.getMarketId()+"_"+kline.getMinType(), klineList,-1);
		}
	}	
	
	public Date getDate(Date date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf1.format(date);
		Date newDate = null;
		try {
			newDate = sdf2.parse(format+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
	}


	public Kline findYesterdayKline(Integer marketId) {
		return mapper.findYesterdayKline(marketId);
	}
}
