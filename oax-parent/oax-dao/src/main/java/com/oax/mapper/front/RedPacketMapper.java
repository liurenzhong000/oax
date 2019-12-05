package com.oax.mapper.front;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oax.entity.admin.dto.CountRedPacketDto;
import com.oax.entity.admin.param.RedPacketPageParam;
import com.oax.entity.admin.vo.RedPacketVo;
import com.oax.entity.front.GrabRedPacketLog;
import com.oax.entity.front.Member;
import com.oax.entity.front.RedPacket;
import com.oax.entity.front.RedPacketLimit;
import com.oax.entity.front.RedPacketLimitInfo;
import com.oax.entity.front.RedPacketRecord;

public interface RedPacketMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedPacket record);

    int insertSelective(RedPacket record);

    RedPacket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedPacket record);

    int updateByPrimaryKey(RedPacket record);

    BigDecimal getBanlance(@Param("coinId") Integer coinId, @Param("userId")Integer userId);

    List<RedPacketLimit> getLimit();

    Integer updateUserCoin(RedPacket redPacket);

    BigDecimal getCnyPrice(Integer coinId);

    int insertUserSelective(Member member);

    Integer getUserCoinRecord(@Param("userId") Integer userId,@Param("coinId") Integer coinId);

    RedPacket getRedPacketById(Integer id);

    Integer insertLogs(GrabRedPacketLog log);

    Integer addUserCoin(@Param("coinId")Integer coinId, @Param("userId")Integer userId,@Param("banlance") BigDecimal banlance);

    Integer updateRedPacket(@Param("id") Integer id,@Param("grabCoinQty") BigDecimal grabAmount);

    Map<String,Object> checkTakeRedPacket(@Param("userId")Integer userId, @Param("redPacketId")Integer redPacketId);


    List<RedPacketVo> selectRedPacketByPageParam(RedPacketPageParam redPacketPageParam);

    CountRedPacketDto countRedPacketByParam(RedPacketPageParam redPacketPageParam);



    RedPacketVo selectRedPacketVoById(Integer redPacketId);

    List<RedPacketVo> selectRedPacketVoByUserIdId(Integer userId);

    List<RedPacketRecord> findRedPacketPageByUserId(Integer  userId);

    BigDecimal getSumCnyByUserId(Integer userId);

    Map<String,Object> getRedPacketInfo(int id);

    List<Map<String,Object>> getRedPacketDetails(int redPacketId);

    BigDecimal grabRedPacketTotalCny(int userId);

    List<Map<String,Object>> grabRedPacketRecord(int userId);

    String getCoinName(Integer coinId);

    Integer getCoinByRedPacketId(Integer id);

    Map<String,Object> initRedPacket(int id);

    List<Map<String,Object>> selectRedPacketUserCoin(@Param("list") List<RedPacketLimitInfo> list,@Param("userId") int userId);

    List<Map<String,Object>> findOverdueRedPacket();

    Integer addUserCoinFromRedPacket(Map<String,Object> params);

    Integer saveRecharge(Map<String,Object> params);

    Integer updateRedPacketIsRefund(Map<String, Object> params);
}