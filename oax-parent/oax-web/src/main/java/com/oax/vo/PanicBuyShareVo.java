package com.oax.vo;

import com.oax.entity.front.vo.PanicBuyHelpVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * BHB抢购活动 好友助力页数据
 */
@Data
public class PanicBuyShareVo {

    //好友助力榜
    private List<PanicBuyHelpVo> panicBuyHelpVos;

    //当前进度
    private BigDecimal finishRatio;

    //助力值
    private Integer helpValue;

    //是否有效
    private boolean valid;
}
