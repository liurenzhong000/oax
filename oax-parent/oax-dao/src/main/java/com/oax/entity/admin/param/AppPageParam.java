package com.oax.entity.admin.param;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 16:45
 * app页面 参数
 */
@Data
public class AppPageParam extends PageParam {

    /**
     * 设备类型
     */
    private Integer appType;

    /**
     * 版本号 模糊查询
     */
    private String version;

}
