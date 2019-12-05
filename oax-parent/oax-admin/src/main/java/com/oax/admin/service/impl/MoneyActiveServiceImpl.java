package com.oax.admin.service.impl;
import com.oax.entity.front.Active;
import com.oax.entity.front.MovesayMoneyActive;
import com.oax.entity.front.MovesayMoneyActiveExample;
import com.oax.mapper.front.MovesayMoneyActiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.MoneyActiveService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
public class MoneyActiveServiceImpl  implements MoneyActiveService {

    @Autowired
    MovesayMoneyActiveMapper movesayMoneyActiveMapper;

    @Override
    public MovesayMoneyActive selectMoneyActiveById(int id) {
        return movesayMoneyActiveMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateMoneyActive(MovesayMoneyActive movesayMoneyActive) {
        MovesayMoneyActive  movesayMoneyActiveu=movesayMoneyActiveMapper.selectByPrimaryKey(movesayMoneyActive.getId());
        movesayMoneyActive.setStatus(movesayMoneyActiveu.getStatus());
        movesayMoneyActive.setJoinNum(movesayMoneyActiveu.getJoinNum());
        movesayMoneyActive.setJoinMoney(movesayMoneyActiveu.getJoinMoney());
        movesayMoneyActive.setCreateTime(movesayMoneyActiveu.getCreateTime());
        movesayMoneyActive.setUpdateTime(new Date());
        return movesayMoneyActiveMapper.updateByPrimaryKey(movesayMoneyActive);
    }

    public List<MovesayMoneyActive> selectAllRecord()
    {
        MovesayMoneyActiveExample  movesayMoneyActiveExample =new MovesayMoneyActiveExample();
        MovesayMoneyActiveExample.Criteria  criteria=movesayMoneyActiveExample.createCriteria();
        return movesayMoneyActiveMapper.selectByExample(movesayMoneyActiveExample);
    }

    public Integer insertMoneyActiveSys(MovesayMoneyActive movesayMoneyActiveSys)
    {
        movesayMoneyActiveSys.setStatus(1);
        movesayMoneyActiveSys.setJoinNum(0);
        movesayMoneyActiveSys.setJoinMoney(new BigDecimal("0"));
        movesayMoneyActiveSys.setCreateTime(new Date());
        movesayMoneyActiveSys.setUpdateTime(new Date());
        if (movesayMoneyActiveSys.getMoney()==null)
        {
            movesayMoneyActiveSys.setMoney(new BigDecimal("0"));
        }
        if (movesayMoneyActiveSys.getDepositMoney()==null)
        {
            movesayMoneyActiveSys.setDepositMoney(new BigDecimal("0"));
        }
        return movesayMoneyActiveMapper.insert(movesayMoneyActiveSys);
    }

    public  List<Active>  selectActive()
    {
        return movesayMoneyActiveMapper.selectActive();
    }
}
