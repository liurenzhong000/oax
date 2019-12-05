package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.param.TradePageParam;
import com.oax.entity.admin.param.TradesParam;
import com.oax.entity.admin.vo.TradeFeeVo;
import com.oax.entity.admin.vo.TradePageVo;
import com.oax.entity.admin.vo.TradesVo;

public interface ITradeService {
    /**
     * 详情: 交易记录  分页 高级查询
     */
    PageInfo<TradesVo> getByUserIdQueryTrade(TradesParam tradesParam);

    /**
     * 根据 交易记录参数 查询交易记录
     *
     * @param tradePageParam userId      用户id
     *                       marketId    市场id
     *                       orderType   交易记录状态
     *                       pageNo      页码
     *                       pageSize    一页展示数
     *                       startTime   开始
     *                       endTime     结束
     * @return
     */
    PageInfo<TradePageVo> selectByTradePageParam(TradePageParam tradePageParam);

    PageInfo<TradeFeeVo> countTradeFee(SimpleCoinParam simpleCoin);
}
