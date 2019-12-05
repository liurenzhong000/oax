package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListDiceRandomRateForm;
import com.oax.admin.service.activity.DiceRandomRateService;
import com.oax.entity.activity.DiceRandomRate;
import com.oax.mapper.activity.DiceRandomRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 18:24
 * @Description:
 */
@Service
@Transactional
public class DiceRandomRateServiceImpl implements DiceRandomRateService {

    @Autowired
    private DiceRandomRateMapper diceRandomRateMapper;

    @Override
    public PageInfo list(ListDiceRandomRateForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<DiceRandomRate> diceConfigs = diceRandomRateMapper.pageForAdmin(form.getCoinId());
        return new PageInfo<>(diceConfigs);
    }

    @Override
    public void saveOne(DiceRandomRate entity) {
        diceRandomRateMapper.insert(entity);
    }

    @Override
    public void updateOne(DiceRandomRate entity) {
        diceRandomRateMapper.updateById(entity);
    }

    @Override
    public void deleteOne(Integer id) {
        diceRandomRateMapper.deleteById(id);
    }
}
