package com.oax.entity.admin.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 15:09
 */
@Data
public class SimpleCoin {

    private int id;

    /**
     * 英文简称
     */
    private String shortName;

    /**
     * 英文全称
     */
    private String fullName;

    /**
     * 中文名
     */
    private String cnName;

    /**
     * 图片
     */
    private String image;

    /**
     * 热钱包地址
     */
    private String mainAddress;

    /**
     * 冷钱包地址
     */
    private String coldAddress;

    /**
     * 允许充值 1是 0否,默认0
     */
    private Integer allowRecharge;

    /**
     * 是否允许提币 1是 0否 默认0
     */
    private Integer allowWithdraw;

    /**
     * 提币最小限制
     */
    private BigDecimal minOutQty;

    /**
     * 提币最大限制
     */
    private BigDecimal maxOutQty;

    /**
     * 转出手续费
     */
    private BigDecimal withdrawFee;

    /**
     * 更新时间
     */
    private Date updateTime;


}
