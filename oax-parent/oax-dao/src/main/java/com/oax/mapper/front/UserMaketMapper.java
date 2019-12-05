package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.UserMaket;

@Mapper
public interface UserMaketMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * @param ：@param  record
     * @param ：@return
     * @return ：int
     * @throws
     * @Title：insert
     * @Description：用户添加收藏交易对
     */
    int insert(UserMaket record);

    int insertSelective(UserMaket record);

    UserMaket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMaket record);

    int updateByPrimaryKey(UserMaket record);

    /**
     * @param ：@param  coinId
     * @param ：@param  userId
     * @param ：@return
     * @return ：List<TradeCoin>
     * @throws
     * @Title：getTradeCoinListByUser
     * @Description：用户根据coinId 和userId 查询用户自选的交易对信息
     */
    List<Integer> getTradeCoinListByUser(int userId);

    /**
     * @param ：@param  userMaket
     * @param ：@return
     * @return ：Integer
     * @throws
     * @Title：getCountsByMaketId
     * @Description：查询用户是否已关注过该交易对
     */
    Integer getCountsByMaketId(UserMaket userMaket);

    /**
     * @throws
     * @Title：delete
     * @Description：取消关注市场
     */
    Integer delete(@Param("marketId") Integer marketId, @Param("userId") Integer userId);
}