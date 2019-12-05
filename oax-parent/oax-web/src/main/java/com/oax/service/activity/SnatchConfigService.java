package com.oax.service.activity;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.SnatchConfig;
import com.oax.entity.activity.SnatchDetail;
import com.oax.entity.front.vo.SnatchCoinVo;
import com.oax.entity.front.vo.SnatchConfigTypeVo;
import com.oax.vo.SnatchDetailPageVo;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 19:19
 * @Description:
 */
public interface SnatchConfigService extends IService<SnatchConfig> {
    List<SnatchCoinVo> listConfigCoin();

    List<SnatchCoinVo> listAllConfigCoin();

    List<SnatchConfig> listNoActivity();

    Integer defaultCoinId();

    //通过币种id获取奖池类别
    List<SnatchConfigTypeVo> listConfigTypeByCoinId(Integer coinId);

}
