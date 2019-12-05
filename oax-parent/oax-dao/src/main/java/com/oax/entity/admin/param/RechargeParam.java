package com.oax.entity.admin.param;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/8
 * Time: 12:03
 * 虚拟币转入页面 参数
 */
@Data
public class RechargeParam extends PageParam {

    private Integer userId;

    private String username;

    private Integer coinId;

    /**
     * 0 正序
     * 1 倒序
     * null 不按数量排序
     */
    private Integer sortQty;
}
