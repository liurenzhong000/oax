package com.oax.form;

import com.oax.entity.admin.param.PageParam;
import lombok.Data;

/**
 * @Auther: hyp
 * @Date: 2019/1/19 18:54
 * @Description:
 */
@Data
public class SnatchAggregateForm extends PageParam {

    /**用户id*/
    private Integer userId;

    /**奖池类别配置id*/
    private Integer configId;

}
