package com.oax.entity.admin.param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantParam extends PageParam{

    private Integer id;

    private Integer userId;

    private Long ctcOrderId;

}
