package com.oax.service.impl.activity;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.activity.DiceBetQtyWin;
import com.oax.mapper.activity.DiceBetQtyWinMapper;
import com.oax.service.activity.DiceBetQtyWinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 15:16
 * @Description:
 */
@Service
@Transactional
public class DiceBetQtyWinServiceImpl implements DiceBetQtyWinService {

    public static Integer DEFAULT_BACKEND_WIN = 100;

    @Autowired
    private DiceBetQtyWinMapper diceBetQtyWinMapper;

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Integer getBackendWinByBetQty(BigDecimal betQty, Integer coinId) {
        Integer backendWin = DEFAULT_BACKEND_WIN;
        List<DiceBetQtyWin> diceBetQtyWins = diceBetQtyWinMapper.selectOpenByCoinIdOrderBetQty(coinId);
        for (DiceBetQtyWin diceBetQtyWin : diceBetQtyWins) {
            if (betQty.compareTo(diceBetQtyWin.getMinBetQty()) >=0 && betQty.compareTo(diceBetQtyWin.getMaxBetQty()) <0){
                backendWin =  diceBetQtyWin.getBackWin();
                break;
            }
        }
        if (backendWin <= 0 || backendWin > 100) {
            backendWin = DEFAULT_BACKEND_WIN;
        }
        return backendWin;
    }
}
