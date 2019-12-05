package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.UserCoinSnapshootLightForm;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.UserCoinSnapshootLight;

import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 18:19
 * @Description:
 */
public interface UserCoinSnapshootLightService {
    PageInfo<UserCoinSnapshootLight> pageForAdmin(UserCoinSnapshootLightForm form);
}
