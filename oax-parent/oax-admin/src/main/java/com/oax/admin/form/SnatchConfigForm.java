package com.oax.admin.form;

import com.oax.entity.activity.SnatchConfig;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 18:12
 * @Description:
 */
@Data
public class SnatchConfigForm {

    /**币种id*/
    @NotNull(message = "币种id不能为空")
    private Integer coinId;

    /**奖池名称*/
    @NotNull(message = "奖池名称不能为空")
    private String name;

    /**奖池数量*/
    @NotNull(message = "奖池数量不能为空")
    private Integer quantity;

    /**最大投注数量*/
    @NotNull(message = "最大投注数量不能为空")
    private Integer maxQuantity;

    /**单位，每次投注多少*/
    @NotNull(message = "单位，每次投注多少不能为空")
    private BigDecimal unit;

    /**中奖人数*/
    @NotNull(message = "中奖人数不能为空")
    private Integer winNumber;

    /**当前期号，序号*/
    @NotNull(message = "当前期号，序号")
    private Integer ordinal;

    /**布局排序序号*/
    @NotNull(message = "布局排序序号")
    private Integer sequence;

    /**后台调控机器人中奖概率*/
    @NotNull(message = "币种id不能为空")
    private Integer robotBackWin;

    /**机器人中奖人数*/
    @NotNull(message = "币种id不能为空")
    private Integer robotWinCount;

    /**是否开启 0关闭，1开启
     * @see SnatchConfig.Status
     * */
    @NotNull(message = "是否开启 0关闭，1开启")
    private Integer status;
}
