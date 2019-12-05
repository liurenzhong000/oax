package com.oax.service.impl.activity;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.vo.DiceConfigVo;
import com.oax.mapper.activity.DiceConfigMapper;
import com.oax.service.activity.DiceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 15:16
 * @Description:
 */
@Service
@Transactional
public class DiceConfigServiceImpl implements DiceConfigService {

    @Autowired
    private DiceConfigMapper diceConfigMapper;

    @Override
    @DataSource(DataSourceType.SLAVE)
    public DiceConfigVo getVoOpenByCoinId(Integer coinId) {
        return diceConfigMapper.getVoOpenByCoinId(coinId);
    }

    //获取默认抽奖币种
    @Override
    @DataSource(DataSourceType.SLAVE)
    public Integer getDefaultCoinId() {
        return diceConfigMapper.getDefaultCoinId();
    }

    @Override
    public List<DiceConfigVo> diceOpenCoinList() {
        List<DiceConfigVo> diceConfigVos = diceConfigMapper.listVoOpen();
        diceConfigVos.forEach(item-> {
            item.setMinBetQty(item.getMinBetQty().stripTrailingZeros());
            item.setMaxBetQty(item.getMaxBetQty().stripTrailingZeros());
        });
        return diceConfigMapper.listVoOpen();
    }
}
