package com.oax.admin.form;

import com.oax.entity.activity.SnatchConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 18:29
 * @Description:
 */
@Setter
@Getter
public class UpdateSnatchConfigForm {

    /**奖池名称*/
    @NotNull
    private String name;

    /**奖池数量*/
    @NotNull
    private Integer quantity;

    /**最大投注数量*/
    @NotNull
    private Integer maxQuantity;

    /**单位，每次投注多少*/
    @NotNull
    private BigDecimal unit;

    /**中奖人数*/
    @NotNull
    private Integer winNumber;

    /**布局排序序号*/
    @NotNull
    private Integer sequence;

    /**后台调控机器人中奖概率*/
    @NotNull
    private Integer robotBackWin;

    /**机器人中奖人数*/
    @NotNull
    private Integer robotWinCount;

    /**是否开启 0关闭，1开启
     * @see SnatchConfig.Status
     * */
    @NotNull
    private Integer status;

}
