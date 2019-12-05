package com.oax.entity.admin.param;

import com.oax.entity.enums.CtcOrderStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CtcOrderParam extends PageParam {

    private Long id;

    /**用户id*/
    private Integer userId;

    /**用户名*/
    private String userName;

    /**商户id*/
    private Integer merchantId;

    /**订单状态*/
    private CtcOrderStatus status;

}
