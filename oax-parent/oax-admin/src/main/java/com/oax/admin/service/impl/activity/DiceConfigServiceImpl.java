package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.activity.DiceConfigService;
import com.oax.entity.activity.DiceConfig;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.vo.DiceConfigVo;
import com.oax.mapper.activity.DiceConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 17:51
 * @Description:
 */
@Service
@Transactional
public class DiceConfigServiceImpl implements DiceConfigService {

    @Autowired
    private DiceConfigMapper diceConfigMapper;

    @Override
    public PageInfo list(PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<DiceConfigVo> diceConfigs = diceConfigMapper.pageForAdmin();
        return new PageInfo<>(diceConfigs);
    }

    @Override
    public void saveOne(DiceConfig entity) {
        diceConfigMapper.insert(entity);
    }

    @Override
    public void updateOne(DiceConfig entity) {
        diceConfigMapper.updateById(entity);
    }

    @Override
    public void deleteOne(Integer id) {
        diceConfigMapper.deleteById(id);
    }

}
