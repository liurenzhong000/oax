package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.DiceConfig;
import com.oax.entity.front.vo.DiceConfigVo;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 17:11
 * @Description:
 */
public interface DiceConfigMapper extends BaseMapper<DiceConfig> {

    List<DiceConfigVo> pageForAdmin();

    DiceConfigVo getVoOpenByCoinId(Integer coinId);

    Integer getDefaultCoinId();

    List<DiceConfigVo> listVoOpen();

}
