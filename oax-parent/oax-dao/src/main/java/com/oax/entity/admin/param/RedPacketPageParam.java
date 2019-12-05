package com.oax.entity.admin.param;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/8/6
 * Time: 15:37
 */
@Data
public class RedPacketPageParam extends PageParam {

    private Integer userId;

    private String username;

    private Integer type;

    private Integer coinId;
}
