package com.oax.scheduled;


import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class KlineTaskWeek1 {
	@Autowired
	private KlineService klineService;
	//每周一凌晨一点执行
	@Scheduled(cron="0 0 1 ? * MON")
	@Async
	public void pullKline() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar end = Calendar.getInstance();
		Calendar begin = Calendar.getInstance();
		begin.add(Calendar.DATE, -7);
		String endDate = sdf.format(end.getTime())+" 00:00:00";
		String beginDate = sdf.format(begin.getTime())+" 00:00:00";
		System.out.println("开始时间："+beginDate);
		System.out.println("开始时间："+endDate);
		List<Kline> klineList = klineService.pullKline(beginDate,endDate,KlineTypeEnum.KLINE_1_WEEK.getMinType());
		//批量插入各个交易对的数据
		if (klineList!=null && klineList.size()!=0) {
			klineService.batchSave(klineList);
			//查询k线数据并缓存
			klineService.setKlineRedis(klineList);
		}		
	}
	
	
}

