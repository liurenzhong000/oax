package com.oax.entity.admin.param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PanicBuyHelpParam extends PageParam {

    private Integer activityId;

    private Integer userId;

    private String phone;
}
