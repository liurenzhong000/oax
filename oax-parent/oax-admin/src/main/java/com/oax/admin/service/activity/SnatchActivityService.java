package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListSnatchActivityForm;

import java.util.Map;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 19:04
 * @Description:
 */
public interface SnatchActivityService {
    PageInfo list(ListSnatchActivityForm form);

    //投注，手续费，机器人账户
    Map<String, Object> aggregationDetail(Integer coinId);
}
