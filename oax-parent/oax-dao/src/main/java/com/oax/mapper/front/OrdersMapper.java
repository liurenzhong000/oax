package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.param.OrderPageParam;
import com.oax.entity.admin.vo.OrdersPageVo;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.MarketOrders;
import com.oax.entity.front.Member;
import com.oax.entity.front.Orders;

@Mapper
public interface OrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);
    /**
    * @Title：getPwdByUserId 
    * @Description：判断交易密码是否正确
    * @param ：@param id
    * @param ：@return 
    * @return ：Member 
    * @throws
     */
    Member getUserInfo(Integer id);
    
    int cancel(int id);

    /**
     * 通过orderPageParam 获取委托单
     * @param orderPageParam
     *                     userId       用户id
     *                     username     用户名
     *                     marketId     市场id
     *                     orderType    委托单类型 类型： 1 买入 2 卖出
     *                     orderStatus  状态-1已撤单 0待撮合 1撮合中 2已完成撮合
     *                     pageNo       页码
     *                     pageSize     一页展示数
     *                     startTime    开始时间
     *                     endTime      结束时间
     * @return
     */
    List<OrdersPageVo> selectByOrderPageParam(OrderPageParam orderPageParam);
    /**
     * 
    * @Title：findMarketOrdersList 
    * @Description：市场所有的买卖订单
    * @param ：@param params
    * @param ：@return 
    * @return ：List<MarketOrders> 
    * @throws
     */
    List<MarketOrders> findMarketOrdersList(@Param("marketId") int marketId,@Param("type") int type);

	/**
	 * 获取指定条数的type的orders
	 * @param marketId
	 * @param type
	 * @param number
	 * @return
	 */
	List<MarketOrders> findMarketOrders(@Param("marketId") int marketId,@Param("type") int type,@Param("number") Integer number);

	/**
     * 
    * @Title：isNeedTransactionPassword 
    * @Description：查询该用户是否开启交易密码
    * @param ：@param userId
    * @param ：@return 
    * @return ：Integer 
    * @throws
     */
    String isNeedTransactionPassword(int userId);
    /**
     * 
    * @Title：upStatus 
    * @Description：获取订单状态
    * @param ：@param id
    * @param ：@return 
    * @return ：Integer 
    * @throws
     */
    Integer upStatus(int id);
    
    Integer updateStatus(@Param("status")int status,@Param("id")int id,@Param("version")int version);
    
    Integer updateTradeQty(@Param("id")Integer id,@Param("tradeQty")BigDecimal tradeQty,@Param("version")Integer version);
    /**
     * 
    * @Title：getQueueOrderById 
    * @Description：获取单个订单
    * @param ：@return 
    * @return ：Orders 
    * @throws
     */
    Orders getOrderById(int id);
    /**
     * 
    * @Title：getMatchOrder 
    * @Description：获取被撮合的订单
    * @throws
     */
    Orders getMatchOrder(@Param("marketId")Integer marketId,@Param("type")Integer type,@Param("id")Integer id);

    List<Map<String,Object>> getOrdersByUserIdAndStatus(Map<String,Object> params);

	/** 
	* @Title：cancelOrder 
	* @Description：撤销订单修改状态为-1
	* @throws 
	*/
	Integer cancelOrder(@Param("id")Integer id,@Param("userId")Integer userId,@Param("version")Integer version);

	/** 
	* @Title：updateBuyUserCoin 
	* @Description：撤销订单时，修改类型为买入订单的用户资产
	* @throws 
	*/
	Integer updateBuyUserCoin(@Param("unsettledAmount")BigDecimal unsettledAmount,@Param("userId")Integer userId, @Param("marketId")Integer marketId);
	/**
	 * 
	* @Title：updateSellUserCoin 
	* @Description：撤销订单时，修改类型为卖出订单的用户资产
	* @throws
	 */
	Integer updateSellUserCoin(@Param("unsettledAmount")BigDecimal unsettledAmount,@Param("userId")Integer userId, @Param("marketId")Integer marketId);
	/**
	 * 
	* @Title：verifyUserIsLock 
	* @Description：校验用户是否被锁定
	* @throws
	 */
	Integer verifyUserIsLock(int userId);

	/** 
	* @Title：getPrecision 
	* @Description：获取交易对价格数量精度
	* @throws 
	*/
	MarketInfo getPrecision(int marketId);

	/** 
	* @Title：getCoinInfo 
	* @Description：根据订单信息获取交易对币的信息
	* @throws 
	*/
	Orders getCoinInfo(Integer marketId);
	/**
	 * @Title：getOrderByTimeAndMarketId
	 * @Description：根据下单时间和市场id查询订单
	 * @throws
	 */
    List<Orders> getOrderByTimeAndMarketId(@Param("marketId")Integer marketId,@Param("userId")Integer userId, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    List<Orders> getAutoOrders(@Param("marketId")Integer marketId,@Param("userId")Integer userId,@Param("orderId")Integer orderId);

	Integer deleteOrders(@Param("marketId")Integer marketId,@Param("userId")Integer userId,@Param("endTime")String endTime);

	Integer selectLastOrderId();

	List<Map<String, Object>> listBhbUsdtSallOrder();
}