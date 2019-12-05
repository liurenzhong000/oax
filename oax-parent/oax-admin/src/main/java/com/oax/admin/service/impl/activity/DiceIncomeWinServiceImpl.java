package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.activity.DiceIncomeWinService;
import com.oax.common.AssertHelper;
import com.oax.entity.activity.DiceIncomeWin;
import com.oax.entity.admin.param.PageParam;
import com.oax.mapper.activity.DiceIncomeWinMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2018/12/31 19:18
 * @Description:
 */
@Service
@Transactional
@Slf4j
public class DiceIncomeWinServiceImpl implements DiceIncomeWinService {

    @Autowired
    private DiceIncomeWinMapper diceIncomeWinMapper;

    public void saveOne(DiceIncomeWin entity) {
        diceIncomeWinMapper.insert(entity);
    }

    public void updateOne(DiceIncomeWin entity) {
        AssertHelper.notEmpty(entity.getId(), "id不能为空");
        diceIncomeWinMapper.updateById(entity);
    }

    public PageInfo<DiceIncomeWin> list(Integer coinId, PageParam param){
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<DiceIncomeWin> diceIncomeWins = diceIncomeWinMapper.pageForAdmin(coinId);
        return new PageInfo<>(diceIncomeWins);
    }

    @Override
    public void deleteOne(Integer id) {
        diceIncomeWinMapper.deleteById(id);
    }
}
