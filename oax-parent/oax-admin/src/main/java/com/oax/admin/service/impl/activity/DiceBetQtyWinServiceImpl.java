package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListDiceBetQtyWinForm;
import com.oax.admin.service.activity.DiceBetQtyWinService;
import com.oax.entity.activity.DiceBetQtyWin;
import com.oax.entity.front.vo.DiceBetQtyWinVo;
import com.oax.mapper.activity.DiceBetQtyWinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 14:51
 * @Description:
 */
@Service
@Transactional
public class DiceBetQtyWinServiceImpl implements DiceBetQtyWinService {

    @Autowired
    private DiceBetQtyWinMapper diceBetQtyWinMapper;

    @Override
    public PageInfo list(ListDiceBetQtyWinForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<DiceBetQtyWinVo> diceConfigs = diceBetQtyWinMapper.pageForAdmin(form.getCoinId());
        return new PageInfo<>(diceConfigs);
    }

    @Override
    public void saveOne(DiceBetQtyWin entity) {
        diceBetQtyWinMapper.insert(entity);
    }

    @Override
    public void updateOne(DiceBetQtyWin entity) {
        diceBetQtyWinMapper.updateById(entity);
    }

    @Override
    public void deleteOne(Integer id) {
        diceBetQtyWinMapper.deleteById(id);
    }
}