package com.oax.service;


import com.oax.entity.front.MovesayMoneyActive;

import java.util.List;

public interface MovesayMoneyActiveService {

    MovesayMoneyActive selectByPrimaryKey(int id);

    List<MovesayMoneyActive> selectAllRecord();

    int updateMoveMoneyActive(Integer id,Integer status);
}
