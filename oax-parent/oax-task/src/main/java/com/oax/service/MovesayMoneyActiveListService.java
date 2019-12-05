package com.oax.service;

import com.oax.entity.front.MovesayMoneyActiveList;
import com.oax.entity.front.UserCoin;

import java.math.BigDecimal;
import java.util.List;

public interface MovesayMoneyActiveListService {

    List<MovesayMoneyActiveList> SelectAllRecord();

    List<MovesayMoneyActiveList> selectRecordByUserId(Integer active_id, Integer user_id);

    int insertMovesayMoney(MovesayMoneyActiveList movesayMoneyActiveList);

    List<MovesayMoneyActiveList> selectRecordById(Integer user_id);

    int updateStatus(String id,Integer status);

    void updateDayMoveMoneyActive(MovesayMoneyActiveList movesayMoneyActiveList,BigDecimal money);

    void updateAllMoveMoneyActive(MovesayMoneyActiveList movesayMoneyActiveList,UserCoin userCoin);

    void updateStaticMoneyActive(UserCoin userCoin, MovesayMoneyActiveList movesayMoneyActiveList);

    void updatePromoteFund(MovesayMoneyActiveList movesayMoneyActiveList);

}
