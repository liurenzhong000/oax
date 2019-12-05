package com.oax.entity.admin.vo;

import com.oax.entity.enums.UserCoinDetailType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserCoinDetailVo {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 币种id
     */
    private Integer coinId;

    private String coinName;

    /**对应业务表的id（eg：ctc订单id，撮合记录id等）*/
    private String targetId;

    /**余额变化量*/
    private BigDecimal changeBalance;

    /**冻结余额变化量*/
    private BigDecimal changeFreezing;

    /**
     * 创建时间
     */
    private Date createTime;

    /**变化类型*/
    private UserCoinDetailType type;

    private String typeDeac;
}
