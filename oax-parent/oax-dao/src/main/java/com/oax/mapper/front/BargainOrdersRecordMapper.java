package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.front.BargainOrdersRecord;

/**
 * @author ：xiangwh
 * @ClassName:：BargainOrdersRecordMapper
 * @Description： 历史订单
 * @date ：2018年6月7日 下午9:14:23
 */
@Mapper
public interface BargainOrdersRecordMapper {
    List<BargainOrdersRecord> findListByUser(Map<String, Object> params);
}
