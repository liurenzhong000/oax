package com.oax.entity.admin.param;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * coin
 *
 * @author
 */
@Data
public class EthCoin implements Serializable {

    private Integer id;

    /**
     * 图标
     */
    @NotEmpty(message = "图片url不能为空")
    private String image;

    /**
     * 英文简称
     */
    @NotEmpty(message = "英文简称不能为空")
    private String shortName;

    /**
     * 英文全称
     */
    @NotEmpty(message = "英文全称不能为空")
    private String fullName;

    @NotEmpty(message = "中文名不能为空")
    private String cnName;


    /**
     * 小数位 -> 单位
     */
    @NotNull(message = "小数位不能为空")
    private Integer decimals;

    /**
     * 服务器ip
     */
    @NotEmpty(message = "服务器ip不能为空")
    private String serverIp;

    /**
     * 服务器rpc端口
     */
    @NotNull(message = "服务器rpc端口不能为空")
    private Integer serverPort;

    /**
     * 平台主地址
     */
    @NotEmpty(message = "平台主地址不能为空")
    @Pattern(regexp = "^0x([a-z]|[A-Z]|[0-9]){40}", message = "以太坊地址格式有误")
    private String mainAddress;

    /**
     * 平台主地址密码
     */
    @NotEmpty(message = "平台主地址密码不能为空")
    private String mainAddressPassword;


    /**
     * 冷钱包地址
     */
    @NotEmpty(message = "冷钱包地址不能为空")
    @Pattern(regexp = "^0x([a-z]|[A-Z]|[0-9]){40}", message = "以太坊地址格式有误")
    private String coldAddress;

    /**
     * 超过多少数量转入冷钱包
     */
    @NotNull(message = "冷钱包临界值不能为空")
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


    /**
     * 超过多少传入平台主地址
     */
    @NotNull(message = "超出转入主地址阈值不能为null")
    private BigDecimal outQtyToMainAddress;

    private static final long serialVersionUID = 1L;


}