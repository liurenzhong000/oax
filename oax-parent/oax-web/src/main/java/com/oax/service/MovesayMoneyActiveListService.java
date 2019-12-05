package com.oax.service;

import com.oax.entity.front.MovesayMoneyActive;
import com.oax.entity.front.MovesayMoneyActiveList;
import com.oax.entity.front.UserCoin;
import com.oax.exception.VoException;
import com.oax.vo.RushToBuyVO;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface MovesayMoneyActiveListService {

    List<MovesayMoneyActiveList> selectRecordByUserId(Integer active_id, Integer user_id);

    void insertStaticMovesayMoney(RushToBuyVO vo) throws VoException;

    void insertActiveMovesayMoney(RushToBuyVO vo) throws VoException,ParseException;

    List<MovesayMoneyActiveList> selectRecordById(Integer user_id);

    String selectMaxOrderNo(Integer active_id);

    MovesayMoneyActiveList selectByPrimaryKey(String id);

    int updateMovesayMoneyList(MovesayMoneyActiveList movesayMoneyActiveList);

    void repeatMovesayMoneyList(String id,String num);

    void updateDepositRecord(String id,String num) throws VoException;


}
