package com.oax.admin.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.vo.CoinOutQtyCountVo;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.entity.admin.vo.SimpleCoin;
import com.oax.entity.front.Coin;
import com.oax.entity.front.CoinWithBLOBs;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 14:54
 */
public interface CoinService {
    /**
     * 根据参数 查询SimpleCoin 且分页
     *
     * @param simpleCoinParam pageNo      页码
     *                        pageSize    一页展示数
     *                        startTime   开始时间
     *                        endTime     结束时间
     *                        coinId      币id
     * @return
     */
    PageInfo<SimpleCoin> selectSimpleCoinByParam(SimpleCoinParam simpleCoinParam);

    /**
     * 通过id 获取coin
     *
     * @param coinId
     * @return
     */
    CoinWithBLOBs selectById(int coinId);

    /**
     * 获取所有 币种名称及id
     *
     * @return
     */
    List<MarketCoinVo> selectNameAll();

    /**
     * 更新币种信息
     *
     * @param coinWithBLOBs
     * @return
     */
    int updateCionBySelect(CoinWithBLOBs coinWithBLOBs);

    /**
     * 添加币种
     *
     * @param coinWithBLOBs
     * @return
     */
    int addCionBySelect(CoinWithBLOBs coinWithBLOBs);

    /**
     * 查询平台 地址
     *
     * @param coinId   为null查询所有
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<CoinOutQtyCountVo> selectCoinOutQtyCountVoByPage(Integer coinId, int pageNo, int pageSize);

    /**
     * 查询所有符合 name的记录 且排除传入id
     *
     * @param shortName 简称
     * @param fullName  全称
     * @param cnName    中文名
     * @param withOutId 排除id(可为null) 为null时不排除
     * @return
     */
    List<Coin> selectByNameAndWithOutId(String shortName, String fullName, String cnName, Integer withOutId);

    /**
     * 根据type 查询对应的 币种
     *
     * @param type 1 eth 2 BTC 3 EHT_TOKEN代币
     * @return
     */
    List<Coin> selectByType(int type);

    int insertParentCoin(CoinWithBLOBs coinWithBLOBs);

    Coin selectByShortName(String coinShortName);

    List<Coin> selectByPropertyidWithOutCheckCoinId(Integer propertyid, Integer coinId);
}
