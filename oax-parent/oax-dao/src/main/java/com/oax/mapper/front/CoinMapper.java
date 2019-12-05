package com.oax.mapper.front;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.vo.CoinOutQtyCountVo;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.entity.admin.vo.SimpleCoin;
import com.oax.entity.front.Coin;
import com.oax.entity.front.CoinWithBLOBs;
import com.oax.entity.front.Recharge;

@Mapper
public interface CoinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CoinWithBLOBs record);

    int insertSelective(CoinWithBLOBs record);

    CoinWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CoinWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CoinWithBLOBs record);

    int updateByPrimaryKey(Coin record);


    List<SimpleCoin> selectSimpleCoinByParam(@Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime,
                                            @Param("coinId") Integer coinId);

    /**
     * 获取 所有 币种名称及时间
     *
     * @return
     */
    List<MarketCoinVo> selectNameAll();

    List<Recharge> listCurrency();

    List<CoinOutQtyCountVo> selectCoinOutQtyCountVoByPage(@Param("coinId") Integer coinId);

    /**
     * 查询所有符合 name的记录 且排除传入id
     *
     * @param shortName 简称
     * @param fullName  全称
     * @param cnName    中文名
     * @param withOutId 排除id(可为null) 为null时不排除
     * @return
     */
    List<Coin> selectByNameAndWithOutId(@Param("shortName") String shortName,
                                        @Param("fullName") String fullName,
                                        @Param("cnName") String cnName,
                                        @Param("withOutId") Integer withOutId);

    List<Coin> selectByType(int type);

    List<Coin> selectAll();

    List<Coin> selectByContractAddress(String contractAddress);
    int updateAllEHTAndTokenGasPrice(int gasPrice);

	Coin selectByShortName(String shortName);

	/** 
	* @Title：getCoinByMarketId 
	* @Description：根据市场id查询获取钱包地址的url
	* @throws 
	*/
	Coin getCoinByMarketId(@Param("marketId")Integer marketId,@Param("type")Integer type);

	Coin selectServerIpAndPort(Integer coinId);

    CoinWithBLOBs selectById(int coinId);

    List<Coin> selectByPropertyidWithOutCheckCoinId(@Param("propertyid") Integer propertyid, @Param("coinId") Integer coinId);

    String getShortNameById(Integer coinId);
}