package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.UserRemark;

/**
 * @Auther: hyp
 * @Date: 2019/2/26 16:48
 * @Description: 用户标注
 */
public interface UserRemarkService {
    PageInfo<UserRemark> page(PageParam param, Integer userId);

    void saveOne(Integer userId, String remark);
}
