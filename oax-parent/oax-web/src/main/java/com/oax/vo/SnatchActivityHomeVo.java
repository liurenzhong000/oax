package com.oax.vo;

import com.oax.entity.front.vo.SnatchActivityVo;
import com.oax.entity.front.vo.SnatchCoinVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/17 11:54
 * @Description:
 */
@Data
public class SnatchActivityHomeVo {

    /**余额*/
    private BigDecimal balance;

    /**奖次列表*/
    private List<SnatchActivityVo> snatchActivityVos;

    /**开放的币种id列表*/
    private List<SnatchCoinVo> snatchCoinVos;
}
