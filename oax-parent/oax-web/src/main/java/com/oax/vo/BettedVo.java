package com.oax.vo;

import lombok.Data;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 17:46
 * @Description: 我的奖池的投注
 */
@Data
public class BettedVo {

    private Integer id;

    private Integer bettedUnits;

    public BettedVo(Integer id, Integer bettedUnits){
        this.id = id;
        this.bettedUnits = bettedUnits;
    }
}
