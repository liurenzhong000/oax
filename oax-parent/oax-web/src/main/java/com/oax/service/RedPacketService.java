package com.oax.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.vo.RedPacketVo;
import com.oax.entity.front.Member;
import com.oax.entity.front.RedPacket;
import com.oax.entity.front.RedPacketLimit;
import com.oax.entity.front.RedPacketLimitInfo;
import com.oax.exception.VoException;
import com.oax.vo.RedPacketVO;
import com.oax.vo.UserRedPacketVO;

public interface RedPacketService {


    RedPacketVo selectById(Integer redPacketId);

    PageInfo<RedPacketVo> selectByUserId(Integer userId, Integer pageNum, Integer pageSize);

    public RedPacket awardRedPacket (RedPacket redPacket) throws VoException;

    public BigDecimal getBanlance(Integer coinId,Integer userId);

    RedPacketLimit getLimit(Integer type, Integer coinId);

    BigDecimal getCNY(Integer coinId);

    Member registByRedPacket(String accountNumber,int sourceType);

    Member checkUser(String accountNumber);

    void insertUserCoin(Integer userId, Integer coinId);

    Map<String,Object> takeRedPacket(Integer userId, RedPacketVO vo,String lang) throws VoException;

    Map<String,Object> checkTakeRedPacket(Integer userId, Integer redPacketId);

    Map<String,Object> findRedPacketRecordByUserId(UserRedPacketVO vo);

    Map<String,Object> takeRedPacketDetails(UserRedPacketVO vo);

    Map<String,Object> grabRedPacketRecord(UserRedPacketVO vo);

    Integer getCoinByRedPacketId(Integer redPacketId);

    Map<String,Object> initRedPacket(int id);

    List<RedPacketLimitInfo> index(int type);

    List<Map<String,Object>> selectRedPacketUserCoin(List<RedPacketLimitInfo> list, int userId);
}
