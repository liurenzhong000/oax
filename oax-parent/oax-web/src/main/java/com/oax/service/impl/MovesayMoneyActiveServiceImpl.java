package com.oax.service.impl;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.MovesayMoneyActive;
import com.oax.entity.front.MovesayMoneyActiveExample;
import com.oax.mapper.front.MovesayMoneyActiveMapper;
import com.oax.service.MovesayMoneyActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class MovesayMoneyActiveServiceImpl implements MovesayMoneyActiveService {
    @Autowired
    private MovesayMoneyActiveMapper mapper;

    @Override
    @DataSource(DataSourceType.MASTER)
    public MovesayMoneyActive selectByPrimaryKey(int id) {
      return  mapper.selectByPrimaryKey(id);

    }

    @DataSource(DataSourceType.MASTER)
    public List<MovesayMoneyActive> selectAllRecord()
    {
        MovesayMoneyActiveExample movesayMoneyActiveExample=new MovesayMoneyActiveExample();
        MovesayMoneyActiveExample.Criteria criteria=movesayMoneyActiveExample.createCriteria();
        return mapper.selectByExample(movesayMoneyActiveExample);
    }

    @DataSource(DataSourceType.MASTER)
    public int updateMoveMoneyActive(MovesayMoneyActive movesayMoneyActive)
    {
        return mapper.updateByPrimaryKey(movesayMoneyActive);
    }

}
