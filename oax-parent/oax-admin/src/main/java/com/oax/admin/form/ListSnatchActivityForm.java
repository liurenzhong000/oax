package com.oax.admin.form;

import com.oax.entity.admin.param.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: hyp
 * @Date: 2019/1/23 11:05
 * @Description: 奖池历史查询
 */
@Getter
@Setter
public class ListSnatchActivityForm extends PageParam {

    /**id*/
    private Integer id;

    /**币种id*/
    private Integer coinId;

    /**奖池配置id*/
    private Integer configId;

}
