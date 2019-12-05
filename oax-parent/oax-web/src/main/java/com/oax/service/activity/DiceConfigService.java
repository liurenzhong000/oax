package com.oax.service.activity;

import com.oax.entity.front.vo.DiceConfigVo;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 15:15
 * @Description:
 */
public interface DiceConfigService {

    DiceConfigVo getVoOpenByCoinId(Integer coinId);

    Integer getDefaultCoinId();

    List<DiceConfigVo> diceOpenCoinList();
}
