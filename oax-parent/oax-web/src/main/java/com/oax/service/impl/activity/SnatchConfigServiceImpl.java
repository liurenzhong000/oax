package com.oax.service.impl.activity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.SnatchConfig;
import com.oax.entity.front.vo.SnatchCoinVo;
import com.oax.entity.front.vo.SnatchConfigTypeVo;
import com.oax.mapper.activity.SnatchConfigMapper;
import com.oax.service.activity.SnatchConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 19:20
 * @Description:
 */
@Service
public class SnatchConfigServiceImpl extends ServiceImpl<SnatchConfigMapper, SnatchConfig> implements SnatchConfigService {

    @Autowired
    private SnatchConfigMapper snatchConfigMapper;

    @Override
    @DataSource(DataSourceType.MASTER)
    public List<SnatchCoinVo> listConfigCoin(){
        return snatchConfigMapper.listConfigCoin();
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<SnatchCoinVo> listAllConfigCoin(){
        return snatchConfigMapper.listAllConfigCoin();
    }

    //那种配置已经开启，但没有生成activity的配置
    @Override
    @DataSource(DataSourceType.MASTER)
    public List<SnatchConfig> listNoActivity() {
        return snatchConfigMapper.listNoActivity();
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public Integer defaultCoinId(){
        return snatchConfigMapper.defaultCoinId();
    }

    //通过币种id获取奖池类别
    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<SnatchConfigTypeVo> listConfigTypeByCoinId(Integer coinId){
        return snatchConfigMapper.listConfigTypeByCoinId(coinId);
    }

}
