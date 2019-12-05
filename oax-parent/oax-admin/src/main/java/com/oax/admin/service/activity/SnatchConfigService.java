package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.entity.activity.SnatchConfig;
import com.oax.entity.admin.param.PageParam;

/**
 * @Auther: hyp
 * @Date: 2019/1/21 18:08
 * @Description:
 */
public interface SnatchConfigService {
    void saveOne(SnatchConfig entity);

    PageInfo list(Integer coinId, PageParam pageParam);

    void updateOne(SnatchConfig entity);
}
