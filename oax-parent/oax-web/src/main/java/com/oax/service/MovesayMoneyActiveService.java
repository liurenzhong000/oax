package com.oax.service;


import com.oax.entity.front.MovesayMoneyActive;
import com.oax.entity.front.UserCoin;
import com.oax.exception.VoException;

import java.util.List;

public interface MovesayMoneyActiveService {

    MovesayMoneyActive selectByPrimaryKey(int id);

    List<MovesayMoneyActive> selectAllRecord();

    int updateMoveMoneyActive(MovesayMoneyActive movesayMoneyActive);
}
