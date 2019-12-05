package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.entity.activity.DiceConfig;
import com.oax.entity.admin.param.PageParam;

/**
 * @Auther: hyp
 * @Date: 2019/1/4 17:24
 * @Description:
 */
public interface DiceConfigService {
    PageInfo list(PageParam pageParam);

    void saveOne(DiceConfig entity);

    void updateOne(DiceConfig entity);

    void deleteOne(Integer id);
}
