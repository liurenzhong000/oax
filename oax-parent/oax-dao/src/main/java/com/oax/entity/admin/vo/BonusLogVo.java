package com.oax.entity.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/11 17:32
 * @Description: 分红记录
 */
@Data
public class BonusLogVo {

    private Integer userId;

    private String phone;

    private String email;

    private String idName;

    private BigDecimal currQty;

    private BigDecimal bonus;

    private BigDecimal myBonus;

    private Integer oneCount;

    private BigDecimal oneBonus;

    private Integer twoCount;

    private BigDecimal twoBonus;

    private Integer threeCount;

    private BigDecimal threeBonus;

    private String oneUserIds;

    private String twoUserIds;

    private String threeUserIds;

    private Date createTime;
}
