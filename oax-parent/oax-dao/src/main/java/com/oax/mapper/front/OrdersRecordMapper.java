/**
 *
 */
package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.front.OrdersRecord;

/**
 * @author ：xiangwh
 * @ClassName:：OrdersRecordMapper
 * @Description： 查询用户的委托记录
 * @date ：2018年6月7日 下午4:18:55
 */
@Mapper
public interface OrdersRecordMapper {
    List<OrdersRecord> findListByUser(Map<String, Object> params);
}
