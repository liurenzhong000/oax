package com.oax.entity.front.vo;

import lombok.Data;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 15:04
 * @Description:
 */
@Data
public class DiceRollUnderWinVo {

    private Integer id;

    /**币种id*/
    private Integer coinId;

    /**币种名称*/
    private String coinName;

    /**投注数下限*/
    private Integer minRollUnder;

    /**投注数上限*/
    private Integer maxRollUnder;

    /**后台调控概率*/
    private Integer backWin;

    /**是否开启：0关闭 1开启
     * */
    private Integer status;

}
