package com.oax.entity.admin.param;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/2
 * Time: 17:45
 * 修改coin
 */
@Data
public class UpdateCoinParam {

    private Integer id;

    /**
     * 图标
     */
    @NotEmpty(message = "图片url不能为空")
    private String image;

    /**
     * 英文全称
     */
    @NotEmpty(message = "英文全称不能为空")
    private String fullName;

    @NotEmpty(message = "中文名不能为空")
    private String cnName;

    /**
     * 冷钱包地址
     */
    @NotEmpty(message = "冷钱包地址不能为空")
    private String coldAddress;


    /**
     * 超过多少数量转入冷钱包
     */
    private BigDecimal outQtyToColdAddress;

    /**
     * 允许充值 0：否 1：是
     */
    @NotNull(message = "允许充值状态不能为空")
    private Integer allowRecharge;

    /**
     * 允许提币 0：否 1：是
     */
    @NotNull(message = "允许提币状态不能为空")
    private Integer allowWithdraw;

    /**
     * 预警余额
     */
    @NotNull(message = "预警余额不能为空")
    private BigDecimal warningQty;

    /**
     * 转出手续费
     */
    @NotNull(message = "转出手续费不能为空")
    private BigDecimal withdrawFee;

    /**
     * 提币最大限制
     */
    @NotNull(message = "提币最大限制不能为空")
    private BigDecimal maxOutQty;

    /**
     * 提币最小限制
     */
    @NotNull(message = "提币最小限制不能为空")
    private BigDecimal minOutQty;

    @NotNull(message = "中文描述不能为空")
    private String cnDescription;

    @NotNull(message = "英文描述不能为空")
    private String enDescription;

}
