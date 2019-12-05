package com.oax.entity.admin.param;

import com.oax.entity.enums.CtcAppealStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CtcAppealParam extends PageParam{
    /**
     * ctc订单号
     */
    private Integer ctcOrderId;

    /**用户id*/
    private Integer userId;

    /**用户名*/
    private String userName;

    /**
     * 申诉状态（申诉中，处理中，处理完成，已撤销）
     */
    private CtcAppealStatus status;

}
