package com.oax.scheduled;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.oax.common.enums.KlineTypeEnum;
import com.oax.entity.front.Kline;
import com.oax.service.KlineService;

/**
 * @ClassName:：KlineTask @Description： 1分钟类型
 * @author ：xiangwh
 * @date ：2018年7月3日 下午5:48:40
 * 
 */
@Component
public class KlineTaskMin5 {
	@Autowired
	private KlineService klineService;
	@Scheduled(cron="0 0/5 * * * ?")
	@Async
	//每隔5分钟整分整秒
	public void pullKline() {
		Date end = Calendar.getInstance().getTime();
		Date begin = new Date(end.getTime()-KlineTypeEnum.KLINE_5_MIN.getMinType()*60*1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endDate = sdf.format(end);
		String beginDate = sdf.format(begin);
		System.out.println("开始时间："+beginDate);
		System.out.println("开始时间："+endDate);
		List<Kline> klineList = klineService.pullKline(beginDate,endDate,KlineTypeEnum.KLINE_5_MIN.getMinType());
		//批量插入各个交易对的数据
		if (klineList!=null && klineList.size()!=0) {
			klineService.batchSave(klineList);
			//查询k线数据并缓存
			klineService.setKlineRedis(klineList);
		}		
	}
	
	
}

