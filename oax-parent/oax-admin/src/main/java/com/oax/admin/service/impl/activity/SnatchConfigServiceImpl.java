package com.oax.admin.service.impl.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.activity.SnatchConfigService;
import com.oax.entity.activity.SnatchConfig;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.admin.vo.SnatchConfigVo;
import com.oax.mapper.activity.SnatchConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 18:09
 * @Description: 一元夺宝相关配置
 */
@Service
@Transactional
public class SnatchConfigServiceImpl implements SnatchConfigService {

    @Autowired
    private SnatchConfigMapper snatchConfigMapper;

    @Override
    public void saveOne(SnatchConfig entity){
        snatchConfigMapper.insert(entity);
    }

    @Override
    public PageInfo list(Integer coinId, PageParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<SnatchConfigVo> snatchConfigVos = snatchConfigMapper.pageForAdmin(coinId);
        return new PageInfo<>(snatchConfigVos);
    }

    @Override
    public void updateOne(SnatchConfig entity){
        snatchConfigMapper.updateById(entity);
    }

}
