/**
 *
 */
package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.front.MarketInfo;


/**
 * @author ：xiangwh
 * @ClassName:：TradeCoinMapper
 * @Description： 查询所有交易对的价格，总交易量，涨幅等
 * @date ：2018年6月4日 下午7:22:36
 */
@Mapper
public interface TradeCoinMapper {
    /**
     * @param ：@return
     * @return ：List<TradeCoin>
     * @throws
     * @Title：findList
     * @Description：查询所有交易对的综合信息
     */
    List<MarketInfo> findList();
}
