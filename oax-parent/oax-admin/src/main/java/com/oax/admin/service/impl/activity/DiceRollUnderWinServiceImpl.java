package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListDiceRollUnderWinForm;
import com.oax.admin.service.activity.DiceRollUnderWinService;
import com.oax.entity.activity.DiceRollUnderWin;
import com.oax.entity.front.vo.DiceRollUnderWinVo;
import com.oax.mapper.activity.DiceRollUnderWinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 15:00
 * @Description:
 */
@Service
@Transactional
public class DiceRollUnderWinServiceImpl implements DiceRollUnderWinService {

    @Autowired
    private DiceRollUnderWinMapper diceRollUnderWinMapper;

    @Override
    public PageInfo list(ListDiceRollUnderWinForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<DiceRollUnderWinVo> diceRollUnderWinVos = diceRollUnderWinMapper.pageForAdmin(form.getCoinId());
        return new PageInfo<>(diceRollUnderWinVos);
    }

    @Override
    public void saveOne(DiceRollUnderWin entity) {
        diceRollUnderWinMapper.insert(entity);
    }

    @Override
    public void updateOne(DiceRollUnderWin entity) {
        diceRollUnderWinMapper.updateById(entity);
    }

    @Override
    public void deleteOne(Integer id) {
        diceRollUnderWinMapper.deleteById(id);
    }
}
