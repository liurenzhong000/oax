package com.oax.admin.service;

import com.oax.entity.front.Active;
import com.oax.entity.front.MovesayMoneyActive;

import java.util.List;



public interface MoneyActiveService {


    /*
     * 获取余利宝(活动)项目
     */
   /* PageInfo<MovesayMoneyActive> selectMoneyActiveList(MoneyActiveParam moneyActiveParam);*/


    MovesayMoneyActive selectMoneyActiveById(int id);

    Integer updateMoneyActive(MovesayMoneyActive movesayMoneyActive);

    List<MovesayMoneyActive> selectAllRecord();

    Integer insertMoneyActiveSys(MovesayMoneyActive movesayMoneyActive);

    List<Active>  selectActive();


}
