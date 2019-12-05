package com.oax.service.impl.activity;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.DiceRollUnderWin;
import com.oax.mapper.activity.DiceRollUnderWinMapper;
import com.oax.service.activity.DiceRollUnderWinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 15:28
 * @Description:
 */
@Service
@Transactional
public class DiceRollUnderWinServiceImpl implements DiceRollUnderWinService {

    private Integer DEFAULT_BACKEND_WIN = 100;

    @Autowired
    private DiceRollUnderWinMapper diceRollUnderWinMapper;

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Integer getBackendWinByRollUnder(Integer rollUnder, Integer coinId) {
        Integer backendWin = DEFAULT_BACKEND_WIN;
        List<DiceRollUnderWin> diceBetQtyWins = diceRollUnderWinMapper.selectOpenByCoinIdOrderRollUnder(coinId);
        for (DiceRollUnderWin diceRollUnderWin : diceBetQtyWins) {
            if (rollUnder >= diceRollUnderWin.getMinRollUnder() && rollUnder < diceRollUnderWin.getMaxRollUnder()){
                backendWin =  diceRollUnderWin.getBackWin();
                break;
            }
        }
        if (backendWin <= 0 || backendWin > 100) {
            backendWin = DEFAULT_BACKEND_WIN;
        }
        return backendWin;
    }


}
