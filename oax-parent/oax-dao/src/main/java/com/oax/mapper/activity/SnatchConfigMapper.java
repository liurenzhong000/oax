package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.SnatchConfig;
import com.oax.entity.admin.vo.SnatchConfigVo;
import com.oax.entity.front.vo.SnatchCoinVo;
import com.oax.entity.front.vo.SnatchConfigTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 18:53
 * @Description:
 */
public interface SnatchConfigMapper extends BaseMapper<SnatchConfig> {

    List<SnatchCoinVo> listConfigCoin();

    List<SnatchConfig> listNoActivity();

    Integer defaultCoinId();

    List<SnatchConfigTypeVo> listConfigTypeByCoinId(Integer coinId);

    List<SnatchCoinVo> listAllConfigCoin();

    List<SnatchConfigVo> pageForAdmin(@Param("coinId") Integer coinId);
}
