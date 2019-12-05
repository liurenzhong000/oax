package com.oax.mapper.front;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oax.entity.admin.param.ActivityCoinParam;
import com.oax.entity.admin.vo.UserCoinDetailVo;
import com.oax.entity.front.vo.MemberCoinVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.Orders;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.UserCoinInfo;

@Mapper
public interface UserCoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCoin record);

    int insertSelective(UserCoin record);

    UserCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCoin record);

    int updateByPrimaryKey(UserCoin record);

    BigDecimal getCountBHBORBCB(Integer coinId);

    List<UserCoin> getByUserIdQueryUerCoin(UserCoin userCoin);
    /**
     * 根据userId
     * @Title：getUserCoin
     * @Description：根据userId跟coinId获取用户余额信息
     * @param ：@param userId
     * @param ：@return
     * @return ：Map<String,String>
     * @throws
     */
    Map<String, String> getUserCoin(@Param("marketId") int marketId,@Param("userId") int userId);
    /**
     *
     * @Title：getMarketInfo
     * @Description：根据markertId获取交易对基本信息(代币，分区币，数量和价格的精度信息)
     * @param ：@param marketId
     * @param ：@return
     * @return ：Map<String,String>
     * @throws
     */
    // Map<String, String> getMarketInfo(Integer marketId);
    /**
     * 获取用户对应的交易对的托管订单信息
     * @Title：getOrdersByUserId
     * @Description：TODO
     * @param ：@param map
     * @param ：@return
     * @return ：List<Orders>
     * @throws
     */
    List<Map<String, String>>  getOrdersByUserId(@Param("marketId") int marketId,@Param("userId") int userId);

    /**
     * 获取用户资产列表
     * @param userId
     * @return
     */
	List<UserCoinInfo> selectByUserId(@Param("userId") Integer userId,@Param("coinName") String coinName);	

    /**
     * btc和cny汇率
     * @param userId
     * @return
     */
	Map<String,BigDecimal> selectEthPrice(@Param("userId") Integer userId,@Param("coinName") String coinName);


	UserCoin selectByUserIdAndCoinId(@Param("userId") Integer userId,@Param("coinId") Integer coinId);
	/**
	 * @param userId
	 * @return
	 */
	List<UserCoinInfo> selectPropertyByUserIdAndCoinId(@Param("userId") Integer userId,@Param("coinId") Integer coinId,@Param("coinName") String coinName);


    /**
     * @Title：updateUserCoin
     * @Description：修改用户资产信息
     * @param ：@param orders
     * @param ：@return
     * @return ：Integer
     * @throws
     */
    Integer updateUserCoin(Orders orders);

    /**
     * 通过地址获取 用户
     * @param toAddress
     * @return
     */
    List<UserCoin> selectByAddress(String toAddress);

    /**
     *
     * @Title：selectUserCoin
     * @Description：下订单时用户查询资产信息，校验是否余额不足
     * @param ：@param userId
     * @param ：@param marketId
     * @param ：@param type
     * @param ：@return
     * @return ：UserCoin
     * @throws
     */
    UserCoin selectUserCoin(@Param("userId")Integer userId,@Param("marketId")Integer marketId,@Param("type")Integer type);
    /**
     *
     * @Title：updateFreezing
     * @Description：撮合成功后 修改买家右币冻结资金(减少)
     * @param ：@param params
     * @param ：@return
     * @return ：Integer
     * @throws
     */
    Integer updateBuyerFreezing(@Param("userId")Integer userId,@Param("rightCoinId")Integer rightCoinId,@Param("freezingBanlance")BigDecimal freezingBanlance);
    /**
     *
     * @Title：updateBuyerBanlance
     * @Description：撮合成功后 修改买家左币可用资金(增加)
     * @param ：@param params
     * @param ：@return
     * @return ：Integer
     * @throws
     */
    Integer updateBuyerBanlance(@Param("userId")Integer userId,@Param("leftCoinId")Integer leftCoinId,@Param("banlance")BigDecimal banlance);
    /**
     *
     * @Title：updateSellerFreezing
     * @Description：撮合成功后 修改卖家左币冻结资金(减少)
     * @throws
     */
    Integer updateSellerFreezing(@Param("userId")Integer userId,@Param("leftCoinId")Integer leftCoinId,@Param("freezingBanlance")BigDecimal freezingBanlance);
    /**
     *
     * @Title：updateSellerBanlance
     * @Description：撮合成功后 修改卖家右币可用资金(增加)
     * @throws
     */
    Integer updateSellerBanlance(@Param("userId")Integer userId,@Param("rightCoinId")Integer rightCoinId,@Param("banlance")BigDecimal banlance);

	BigDecimal getLastPriceEthBtc();

	BigDecimal getLastPriceUsdtBtc();

	BigDecimal getPrice();

    /**
     * 根据 币种type 获取 用户
     * @param type
     * @return
     */
    List<UserCoin> selectByCoinType(int type);

	/** 
	* @Title：getUserCoinRecord 
	* @Description：校验查询看用户是否含有资产记录数据
	* @throws 
	*/
	Integer getUserCoinRecord(@Param("userId")Integer userId, @Param("marketId")Integer marketId, @Param("type")Integer type);

	/** 
	* @Title：getCoinName 
	* @Description：获取币的所在市场分区的币的名称
	* @throws 
	*/
	String getCoinName(@Param("marketId")Integer marketId);

	BigDecimal getLastPriceInEthById(Integer coinId);

    BigDecimal countAllBanlanceByCoinId(Integer coinId);

	/** 
	* @Title：getPasswordAndAddress 
	* @Description：根据币类型查询用户同类型的币种的地址和密码
	* @throws 
	*/
	Integer getUserCoinByUserIdAndCoinId(@Param("coinId")Integer coinId,@Param("userId")Integer userId);

    int insertIgnore(UserCoin userCoin);

	/** 
	* @Title：updateBuyerRightCoinBanlance 
	* @Description：主动撮合方为买家时,也需要修改买家右币的资产
	* @throws 
	*/
	Integer updateBuyerRightCoinBanlance(@Param("userId")Integer userId, @Param("rightCoinId")Integer rightCoinId, @Param("banlance")BigDecimal balance);

	/**
	 * 获取用户的某个币种的余额
	 */
	BigDecimal getBanlanceByCoinIdAndUserId(@Param("userId")Integer userId, @Param("coinId")Integer coinId);

	/**
	 * 冻结用户对应币种的余额
	 * freezingQty：冻结的个数
	 * coinId：币种id
	 * userId：用户id
	 */
	int freezingByUserIdAndCoinId(@Param("freezingQty")BigDecimal freezingQty, @Param("coinId")Integer coinId, @Param("userId")Integer userId,  @Param("version") Integer version);

	/**
	 * 解冻用户对应币种的余额
	 * cancelFreezingQty：冻结的个数
	 * coinId：币种id
	 * userId：用户id
	 */
	int cancelFreezingByUserIdAndCoinId(@Param("cancelFreezingQty")BigDecimal cancelFreezingQty, @Param("coinId")Integer coinId, @Param("userId")Integer userId, @Param("version") Integer version);

	/**
	 * 添加用户余额
	 */
	int addBanlance(@Param("addBanlanceQty")BigDecimal addBanlanceQty, @Param("coinId")Integer coinId, @Param("userId")Integer userId, @Param("version") Integer version);

	/**
	 * 减少用户冻结金额
	 */
	int addFreezing(@Param("addFreezingQty")BigDecimal freezingQty, @Param("coinId")Integer coinId, @Param("userId")Integer userId, @Param("version") Integer version);

	/**
	 * 减少用户冻结金额
	 */
	int subtractFreezing(@Param("subtractFreezingQty")BigDecimal freezingQty, @Param("coinId")Integer coinId, @Param("userId")Integer userId, @Param("version") Integer version);

	/**
	 * 获取用户资产列表,之返回用户拥有的
	 */
	List<UserCoinInfo> listUserCoinByUserId(@Param("userId") Integer userId,@Param("coinName") String coinName);
	/**
	 * 根据coinId查询所有用户
	 */
	ArrayList<UserCoin> selectByCoinId(Integer coinId);

	List<MemberCoinVo> selectByCoinIdAndCount(@Param("coinId") Integer coinId, @Param("thresholdBHBCount") Integer thresholdBHBCount,
											  @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	List<MemberCoinVo> selectMemberCoinVoByBonusLog();

	List<UserCoin> selectUserCoinByUserIdNoZero(@Param("userId") Integer userId, @Param("coinName") String coinName);

	List<UserCoin> selectAllUserCoinByUserId(@Param("userId") Integer userId, @Param("coinName") String coinName);

	List<MemberCoinVo> selectMemberCoinVoByUserCoinSnapshoot(@Param("snapshootStartTime") Date snapshootStartTime,@Param("snapshootEndTime") Date snapshootEndTime);

	List<MemberCoinVo> selectMemberCoinVoByUserCoinSnapshootLight(@Param("snapshootStartTime") Date snapshootStartTime,@Param("snapshootEndTime") Date snapshootEndTime);

	BigDecimal getLastPriceEthUsdt();

	/**
	 * 查询有充值记录或法币记录的用户的user_coin
	 */
	ArrayList<UserCoin> selectHasRechargeOrCtcByCoinId(Integer coinId);

	BigDecimal sumByUserIdsAndCoinId(@Param("userIds")List<Integer> userIds, @Param("coinId") Integer coinId);

	List<MemberCoinVo> listBCBMemberCoinVos();

}